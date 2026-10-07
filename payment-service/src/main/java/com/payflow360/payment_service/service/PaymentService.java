package com.payflow360.payment_service.service;

import com.payflow360.payment_service.dto.CreatePaymentRequest;
import com.payflow360.payment_service.dto.PaymentResponse;
import com.payflow360.payment_service.entity.Payment;
import com.payflow360.payment_service.entity.PaymentStatus;
import com.payflow360.payment_service.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public PaymentResponse createPayment(
            CreatePaymentRequest request,
            String idempotencyKey) {

        Optional<Payment> existingPayment =
                paymentRepository.findByIdempotencyKey(idempotencyKey);

        if (existingPayment.isPresent()) {
            Payment payment = existingPayment.get();

            return new PaymentResponse(
                    payment.getTransactionId(),
                    payment.getStatus(),
                    payment.getAmount(),
                    payment.getCurrency()
            );
        }

        Payment payment = Payment.builder()
                .transactionId(generateTransactionId())
                .idempotencyKey(idempotencyKey)
                .sourceAccountId(request.sourceAccountId())
                .destinationAccountId(request.destinationAccountId())
                .amount(request.amount())
                .currency(request.currency().toUpperCase())
                .status(PaymentStatus.INITIATED)
                .description(request.description())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        return new PaymentResponse(
                savedPayment.getTransactionId(),
                savedPayment.getStatus(),
                savedPayment.getAmount(),
                savedPayment.getCurrency()
        );
    }
    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID();
    }
}