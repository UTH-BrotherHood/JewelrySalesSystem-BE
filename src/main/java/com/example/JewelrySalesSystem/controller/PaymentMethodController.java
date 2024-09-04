package com.example.JewelrySalesSystem.controller;

import com.example.JewelrySalesSystem.dto.request.ApiResponse;
import com.example.JewelrySalesSystem.dto.request.PaymentMethodCreationRequest;
import com.example.JewelrySalesSystem.dto.response.PaymentMethodResponse;
import com.example.JewelrySalesSystem.service.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/payment-methods")
@RequiredArgsConstructor
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @GetMapping("/{paymentMethodId}")
    public ApiResponse<PaymentMethodResponse> getPaymentMethod(@PathVariable String paymentMethodId) {
        PaymentMethodResponse paymentMethodResponse = paymentMethodService.getPaymentMethod(paymentMethodId);
        ApiResponse<PaymentMethodResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage("Payment method retrieved successfully");
        apiResponse.setResult(paymentMethodResponse);
        return apiResponse;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<PaymentMethodResponse>> createPaymentMethod(
            @Valid @RequestBody PaymentMethodCreationRequest request) {
        PaymentMethodResponse paymentMethodResponse = paymentMethodService.createPaymentMethod(request);
        ApiResponse<PaymentMethodResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(201);
        apiResponse.setMessage("Payment method created successfully");
        apiResponse.setResult(paymentMethodResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }


}
