package com.appplication.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.appplication.dto.CreatePaymentOrderResponse;
import com.appplication.dto.VerifyPaymentRequest;
import com.appplication.entity.CartItem;
import com.appplication.entity.OrderItem;
import com.appplication.entity.PaymentTransaction;
import com.appplication.entity.Products;
import com.appplication.entity.PurchaseOrder;
import com.appplication.entity.Users;
import com.appplication.repository.CartItemRepository;
import com.appplication.repository.OrderItemRepository;
import com.appplication.repository.PaymentTransactionRepository;
import com.appplication.repository.ProductRepository;
import com.appplication.repository.PurchaseOrderRepository;
import com.appplication.repository.UsersRepository;

@Service
public class PaymentService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final UsersRepository usersRepository;
    private final RazorpayService razorpayService;

    public PaymentService(
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            PurchaseOrderRepository purchaseOrderRepository,
            OrderItemRepository orderItemRepository,
            PaymentTransactionRepository paymentTransactionRepository,
            UsersRepository usersRepository,
            RazorpayService razorpayService) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.orderItemRepository = orderItemRepository;
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.usersRepository = usersRepository;
        this.razorpayService = razorpayService;
    }

    @Transactional
    public CreatePaymentOrderResponse createPaymentOrder() {
        Users user = getCurrentUser();

        List<CartItem> cartItems =
                cartItemRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Your cart is empty");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem item : cartItems) {
            Products product = item.getProduct();

            if (item.getQuantity() < 1
                    || product.getStock() == null
                    || item.getQuantity() > product.getStock()) {
                throw new IllegalStateException(
                        "Insufficient stock for " + product.getName());
            }

            if (product.getPrice() == null
                    || product.getPrice().signum() <= 0) {
                throw new IllegalStateException(
                        "Invalid product price");
            }

            totalAmount = totalAmount.add(
                    product.getPrice().multiply(
                            BigDecimal.valueOf(item.getQuantity())));
        }

        // Razorpay expects integer paise, not rupees.
        long amountInPaise = totalAmount
                .multiply(new BigDecimal("100"))
                .longValueExact();

        PurchaseOrder order = new PurchaseOrder();
        order.setUsers(user);
        order.setTotalAmount(totalAmount);
        order.setCurrency(razorpayService.getCurrency());
        order.setStatus("PENDING_PAYMENT");

        PurchaseOrder savedOrder =
                purchaseOrderRepository.save(order);

        RazorpayService.RazorpayOrderResponse razorpayOrder =
                razorpayService.createOrder(
                        amountInPaise,
                        "sales-savvy-" + savedOrder.getId());

        if (razorpayOrder == null || razorpayOrder.id() == null) {
            throw new IllegalStateException(
                    "Unable to create Razorpay order");
        }

        savedOrder.setRazorpayOrderId(razorpayOrder.id());
        purchaseOrderRepository.save(savedOrder);

        return new CreatePaymentOrderResponse(
                savedOrder.getId(),
                razorpayOrder.id(),
                amountInPaise,
                razorpayService.getCurrency(),
                razorpayService.getKeyId());
    }

    @Transactional
    public String verifyPayment(VerifyPaymentRequest request) {
        if (request == null) {
            throw new IllegalArgumentException(
                    "Payment verification request is required");
        }

        Users user = getCurrentUser();

        PurchaseOrder order = purchaseOrderRepository
                .findByIdAndUser(request.localOrderId(), user)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Order not found for this user"));

        if ("PAID".equalsIgnoreCase(order.getStatus())) {
            return "Payment already verified";
        }

        if (!"PENDING_PAYMENT".equalsIgnoreCase(order.getStatus())) {
            throw new IllegalStateException(
                    "Order is not awaiting payment");
        }

        if (order.getRazorpayOrderId() == null
                || !order.getRazorpayOrderId().equals(
                        request.razorpayOrderId())) {
            throw new IllegalArgumentException(
                    "Razorpay order ID does not match");
        }

        boolean validSignature = razorpayService.verifySignature(
                order.getRazorpayOrderId(),
                request.razorpayPaymentId(),
                request.razorpaySignature());

        if (!validSignature) {
            throw new IllegalArgumentException(
                    "Payment signature verification failed");
        }

        List<CartItem> cartItems =
                cartItemRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new IllegalStateException(
                    "Cart is empty; order cannot be finalized");
        }

        BigDecimal currentTotal = BigDecimal.ZERO;

        // Recheck stock and the cart total before finalizing.
        for (CartItem item : cartItems) {
            Products product = productRepository
                    .findById(item.getProduct().getId())
                    .orElseThrow(() ->
                            new IllegalStateException(
                                    "Product no longer exists"));

            if (product.getStock() == null
                    || item.getQuantity() < 1
                    || item.getQuantity() > product.getStock()) {
                throw new IllegalStateException(
                        "Insufficient stock for " + product.getName());
            }

            currentTotal = currentTotal.add(
                    product.getPrice().multiply(
                            BigDecimal.valueOf(item.getQuantity())));
        }

        if (currentTotal.compareTo(order.getTotalAmount()) != 0) {
            throw new IllegalStateException(
                    "Cart total changed after order creation");
        }

        /*
         * IMPORTANT:
         * Before marking the order as PAID, verify the payment's
         * captured/authorized status and amount through Razorpay's
         * server-side API or a verified webhook.
         * A valid signature alone does not establish capture status.
         */

        for (CartItem item : cartItems) {
            Products product = productRepository
                    .findById(item.getProduct().getId())
                    .orElseThrow(() ->
                            new IllegalStateException(
                                    "Product no longer exists"));

            int quantity = item.getQuantity();

            OrderItem orderItem = new OrderItem(
                    order,
                    product,
                    quantity,
                    product.getPrice());

            orderItemRepository.save(orderItem);

            product.setStock(product.getStock() - quantity);
            productRepository.save(product);
        }

        PaymentTransaction transaction = new PaymentTransaction();
        transaction.setOrder(order);
        transaction.setRazorpayOrderId(request.razorpayOrderId());
        transaction.setRazorpayPaymentId(request.razorpayPaymentId());
        transaction.setRazorpaySignature(request.razorpaySignature());
        transaction.setStatus("SUCCESS");

        paymentTransactionRepository.save(transaction);

        order.setStatus("PAID");
        purchaseOrderRepository.save(order);

        cartItemRepository.deleteByUser(user);

        return "Payment verified successfully";
    }

    private Users getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null
                || "anonymousUser".equals(authentication.getName())) {
            throw new IllegalStateException("User is not authenticated");
        }

        return usersRepository.findByUsername(authentication.getName())
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Authenticated user was not found"));
    }
}
  