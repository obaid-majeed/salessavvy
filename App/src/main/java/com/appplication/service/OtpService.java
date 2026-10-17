 package com.appplication.service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

    private static final long OTP_VALIDITY_SECONDS = 300;

    private final JavaMailSender mailSender;
    private final SecureRandom secureRandom = new SecureRandom();

    private final ConcurrentHashMap<String, OtpData> otpStore =
            new ConcurrentHashMap<>();

    public OtpService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtp(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        String otp = String.format(
                "%06d", secureRandom.nextInt(1_000_000));

        OtpData otpData = new OtpData(
                otp,
                Instant.now().plusSeconds(OTP_VALIDITY_SECONDS));

        otpStore.put(email, otpData);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Sales Savvy OTP Verification");
            message.setText(
                    "Your Sales Savvy verification code is: " + otp
                    + "\nIt expires in 5 minutes."
                    + "\nDo not share this code with anyone.");

            mailSender.send(message);
        } catch (RuntimeException exception) {
            otpStore.remove(email, otpData);
            throw new IllegalStateException(
                    "Unable to send OTP email", exception);
        }
    }

    public boolean verifyOtp(String email, String submittedOtp) {
        if (email == null || submittedOtp == null
                || submittedOtp.isBlank()) {
            return false;
        }

        OtpData stored = otpStore.get(email);

        if (stored == null) {
            return false;
        }

        if (Instant.now().isAfter(stored.expiresAt())) {
            otpStore.remove(email, stored);
            return false;
        }

        if (!stored.otp().equals(submittedOtp)) {
            return false;
        }

        otpStore.remove(email, stored);
        return true;
    }

    private record OtpData(String otp, Instant expiresAt) {
    }
}
 