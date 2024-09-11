package com.example.JewelrySalesSystem.mapper;

import com.example.JewelrySalesSystem.dto.response.CartItemResponse;
import com.example.JewelrySalesSystem.dto.response.CartResponse;
import com.example.JewelrySalesSystem.entity.Cart;
import com.example.JewelrySalesSystem.entity.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    public CartResponse toCartResponse(Cart cart) {
        List<CartItemResponse> itemResponses = cart.getItems().stream()
                .map(this::toCartItemResponse)
                .collect(Collectors.toList());

        BigDecimal totalAmount = itemResponses.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.builder()
                .cartId(cart.getCartId())
                .employeeId(cart.getEmployeeId())
                .items(itemResponses)
                .totalAmount(totalAmount)
                .build();
    }

    public CartItemResponse toCartItemResponse(CartItem cartItem) {
        return CartItemResponse.builder()
                .itemId(cartItem.getItemId())
                .productId(cartItem.getProductId())
                .productImageUrl(cartItem.getProductImageUrl())
                .productName(cartItem.getProductName())
                .quantity(cartItem.getQuantity())
                .price(cartItem.getPrice())
                .build();
    }
}
