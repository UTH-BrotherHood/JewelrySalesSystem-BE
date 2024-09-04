package com.example.JewelrySalesSystem.service;

import com.example.JewelrySalesSystem.dto.request.PaymentMethodCreationRequest;
import com.example.JewelrySalesSystem.dto.response.PaymentMethodResponse;
import com.example.JewelrySalesSystem.entity.PaymentMethod;
import com.example.JewelrySalesSystem.mapper.PaymentMethodMapper;
import com.example.JewelrySalesSystem.repository.PaymentMethodRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodMapper paymentMethodMapper;

    public PaymentMethodService(PaymentMethodRepository paymentMethodRepository, PaymentMethodMapper paymentMethodMapper) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentMethodMapper = paymentMethodMapper;
    }

    public PaymentMethodResponse createPaymentMethod(PaymentMethodCreationRequest request) {
        PaymentMethod paymentMethod = PaymentMethod.builder()
                .paymentMethodId("PM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .paymentMethodName(request.getPaymentMethodName())
                .details(request.getDetails())
                .active(request.isActive())
                .build();

        paymentMethodRepository.save(paymentMethod);
        return paymentMethodMapper.toPaymentMethodResponse(paymentMethod);
    }

    public PaymentMethodResponse getPaymentMethod(String paymentMethodId) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> new IllegalArgumentException("Payment Method not found"));
        return paymentMethodMapper.toPaymentMethodResponse(paymentMethod);
    }

    // Other methods like update, delete can be added here
}
