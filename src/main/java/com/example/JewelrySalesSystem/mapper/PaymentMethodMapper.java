package com.example.JewelrySalesSystem.mapper;

import com.example.JewelrySalesSystem.dto.response.PaymentMethodResponse;
import com.example.JewelrySalesSystem.entity.PaymentMethod;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMethodMapper {

    PaymentMethodResponse toPaymentMethodResponse(PaymentMethod paymentMethod);
}
