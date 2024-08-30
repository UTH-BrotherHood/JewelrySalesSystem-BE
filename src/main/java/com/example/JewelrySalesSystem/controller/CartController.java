package com.example.JewelrySalesSystem.controller;

import com.example.JewelrySalesSystem.dto.request.ApiResponse;
import com.example.JewelrySalesSystem.dto.request.CartItemRequest;

import com.example.JewelrySalesSystem.dto.response.CartResponse;
import com.example.JewelrySalesSystem.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/{customerId}")
    public ApiResponse<CartResponse> getCart(@PathVariable String customerId) {
        CartResponse cartResponse = cartService.getCartByCustomerId(customerId);
        ApiResponse<CartResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage("Cart retrieved successfully");
        apiResponse.setResult(cartResponse);
        return apiResponse;
    }

    @PostMapping("/{customerId}/add")
    public ResponseEntity<ApiResponse<CartResponse>> addToCart(
            @PathVariable String customerId,
            @Valid @RequestBody CartItemRequest request) {
        CartResponse cartResponse = cartService.addToCart(customerId, request);
        ApiResponse<CartResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(201);
        apiResponse.setMessage("Item added to cart successfully");
        apiResponse.setResult(cartResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{customerId}/remove/{itemId}")
    public ApiResponse<Void> removeFromCart(@PathVariable String customerId, @PathVariable String itemId) {
        cartService.removeFromCart(customerId, itemId);
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(204);
        apiResponse.setMessage("Item removed from cart successfully");
        apiResponse.setResult(null);
        return apiResponse;
    }

    @DeleteMapping("/{customerId}/clear")
    public ApiResponse<Void> clearCart(@PathVariable String customerId) {
        cartService.clearCart(customerId);
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(204);
        apiResponse.setMessage("Cart cleared successfully");
        apiResponse.setResult(null);
        return apiResponse;
    }
    @PatchMapping("/{customerId}/increase/{itemId}")
    public ApiResponse<Void> increaseItemQuantity(
            @PathVariable String customerId, @PathVariable String itemId) {
        cartService.increaseItemQuantity(customerId, itemId);
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage("Item quantity increased successfully");
        apiResponse.setResult(null);
        return apiResponse;
    }

    @PatchMapping("/{customerId}/decrease/{itemId}")
    public ApiResponse<Void> decreaseItemQuantity(
            @PathVariable String customerId, @PathVariable String itemId) {
        cartService.decreaseItemQuantity(customerId, itemId);
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage("Item quantity decreased successfully");
        apiResponse.setResult(null);
        return apiResponse;
    }

}
