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

    @GetMapping("/{employeeId}")
    public ApiResponse<CartResponse> getCart(@PathVariable String employeeId) {
        CartResponse cartResponse = cartService.getCartByemployeeId(employeeId);
        ApiResponse<CartResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage("Cart retrieved successfully");
        apiResponse.setResult(cartResponse);
        return apiResponse;
    }

    @PostMapping("/{employeeId}/add")
    public ResponseEntity<ApiResponse<CartResponse>> addToCart(
            @PathVariable String employeeId,
            @Valid @RequestBody CartItemRequest request) {
        CartResponse cartResponse = cartService.addToCart(employeeId, request);
        ApiResponse<CartResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(201);
        apiResponse.setMessage("Item added to cart successfully");
        apiResponse.setResult(cartResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{employeeId}/remove/{itemId}")
    public ApiResponse<Void> removeFromCart(@PathVariable String employeeId, @PathVariable String itemId) {
        cartService.removeFromCart(employeeId, itemId);
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(204);
        apiResponse.setMessage("Item removed from cart successfully");
        apiResponse.setResult(null);
        return apiResponse;
    }

    @DeleteMapping("/{employeeId}/clear")
    public ApiResponse<Void> clearCart(@PathVariable String employeeId) {
        cartService.clearCart(employeeId);
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(204);
        apiResponse.setMessage("Cart cleared successfully");
        apiResponse.setResult(null);
        return apiResponse;
    }
    @PatchMapping("/{employeeId}/increase/{itemId}")
    public ApiResponse<Void> increaseItemQuantity(
            @PathVariable String employeeId, @PathVariable String itemId) {
        cartService.increaseItemQuantity(employeeId, itemId);
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage("Item quantity increased successfully");
        apiResponse.setResult(null);
        return apiResponse;
    }

    @PatchMapping("/{employeeId}/decrease/{itemId}")
    public ApiResponse<Void> decreaseItemQuantity(
            @PathVariable String employeeId, @PathVariable String itemId) {
        cartService.decreaseItemQuantity(employeeId, itemId);
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage("Item quantity decreased successfully");
        apiResponse.setResult(null);
        return apiResponse;
    }

    @PatchMapping("/{employeeId}/update/{itemId}")
    public ApiResponse<CartResponse> updateCartItemQuantity(
            @PathVariable String employeeId,
            @PathVariable String itemId,
            @Valid @RequestBody CartItemRequest request) {
        CartResponse cartResponse = cartService.updateCartItemQuantity(employeeId, itemId, request.getQuantity());
        ApiResponse<CartResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage("Cart item quantity updated successfully");
        apiResponse.setResult(cartResponse);
        return apiResponse;
    }



}
