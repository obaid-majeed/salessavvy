package com.appplication.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.appplication.dto.AddToCartRequest;
import com.appplication.dto.CartItemResponse;
import com.appplication.dto.CartResponse;
import com.appplication.dto.UpdateCartRequest;
import com.appplication.entity.CartItem;
import com.appplication.entity.Products;
import com.appplication.entity.Users;
import com.appplication.repository.CartItemRepository;
import com.appplication.repository.ProductRepository;
import com.appplication.repository.UsersRepository;

@Service
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UsersRepository usersRepository;

    public CartService(
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            UsersRepository usersRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.usersRepository = usersRepository;
    }

    @Transactional(readOnly = true)
    public CartResponse getCart() {
        Users user = getCurrentUser();

        List<CartItemResponse> items =
                cartItemRepository.findByUser(user)
                        .stream()
                        .map(this::mapToResponse)
                        .toList();

        BigDecimal totalAmount = items.stream()
                .map(CartItemResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(items, totalAmount);
    }

    public CartResponse addToCart(AddToCartRequest request) {
        if (request == null || request.quantity() < 1) {
            throw new IllegalArgumentException(
                    "Quantity must be at least 1");
        }

        Users user = getCurrentUser();

        Products product = productRepository.findById(request.productId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Product not found"));

        if (product.getStock() == null || product.getStock() < 1) {
            throw new IllegalArgumentException("Product is out of stock");
        }

        CartItem item = cartItemRepository
                .findByUserAndProduct(user, product)
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setUsers(user);
                    newItem.setProducts(product);
                    newItem.setQuantity(0);
                    return newItem;
                });

        int updatedQuantity = item.getQuantity() + request.quantity();

        if (updatedQuantity > product.getStock()) {
            throw new IllegalArgumentException(
                    "Requested quantity exceeds available stock");
        }

        item.setQuantity(updatedQuantity);
        cartItemRepository.save(item);

        return getCart();
    }

    public CartResponse updateCartItem(
            Long cartItemId,
            UpdateCartRequest request) {
        if (request == null || request.quantity() < 1) {
            throw new IllegalArgumentException(
                    "Quantity must be at least 1");
        }

        Users user = getCurrentUser();

        CartItem item = cartItemRepository
                .findByIdAndUser(cartItemId, user)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Cart item not found"));

        Products product = item.getProduct();

        if (product.getStock() == null
                || request.quantity() > product.getStock()) {
            throw new IllegalArgumentException(
                    "Requested quantity exceeds available stock");
        }

        item.setQuantity(request.quantity());
        cartItemRepository.save(item);

        return getCart();
    }

    public CartResponse removeCartItem(Long cartItemId) {
        Users user = getCurrentUser();

        CartItem item = cartItemRepository
                .findByIdAndUser(cartItemId, user)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Cart item not found"));

        cartItemRepository.delete(item);

        return getCart();
    }

    public void clearCart() {
        cartItemRepository.deleteByUser(getCurrentUser());
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

    private CartItemResponse mapToResponse(CartItem item) {
        Products product = item.getProduct();

        BigDecimal price = product.getPrice();
        BigDecimal subtotal = price.multiply(
                BigDecimal.valueOf(item.getQuantity()));

        return new CartItemResponse(
                item.getId(),
                product.getId(),
                product.getName(),
                price,
                item.getQuantity(),
                subtotal,
                product.getCategory(),
                product.getImageUrl());
    }
}
