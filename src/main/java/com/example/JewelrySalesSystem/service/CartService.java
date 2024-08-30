package com.example.JewelrySalesSystem.service;

import com.example.JewelrySalesSystem.dto.request.CartItemRequest;

import com.example.JewelrySalesSystem.dto.response.CartResponse;
import com.example.JewelrySalesSystem.entity.Cart;
import com.example.JewelrySalesSystem.entity.CartItem;
import com.example.JewelrySalesSystem.exception.AppException;
import com.example.JewelrySalesSystem.exception.ErrorCode;
import com.example.JewelrySalesSystem.mapper.CartMapper;
import com.example.JewelrySalesSystem.repository.CartRepository;
import com.example.JewelrySalesSystem.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    public CartResponse getCartByCustomerId(String customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
        return cartMapper.toCartResponse(cart);
    }

    public CartResponse addToCart(String customerId, CartItemRequest request) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseGet(() -> {
                    // Create a new cart if none exists for the customerId
                    Cart newCart = Cart.builder()
                            .customerId(customerId)
                            .items(new ArrayList<>()) // Initialize items list
                            .build();
                    return cartRepository.save(newCart);
                });

        CartItem cartItem = CartItem.builder()
                .cart(cart)
                .productId(request.getProductId())

                .quantity(request.getQuantity())
                .price(productRepository.findById(request.getProductId())
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND))
                        .getCostPrice())
                .build();

        cart.getItems().add(cartItem);
        Cart savedCart = cartRepository.save(cart);

        return cartMapper.toCartResponse(savedCart);
    }

    public void removeFromCart(String customerId, String itemId) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        boolean itemExists = cart.getItems().removeIf(item -> item.getItemId().equals(itemId));
        if (!itemExists) {
            throw new AppException(ErrorCode.ITEM_NOT_FOUND_IN_CART); // Custom error code for item not found
        }
        cartRepository.save(cart);
    }
    public void clearCart(String customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        cart.getItems().clear();
        cartRepository.save(cart);
    }
    public void increaseItemQuantity(String customerId, String itemId) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        Optional<CartItem> cartItemOpt = cart.getItems().stream()
                .filter(item -> item.getItemId().equals(itemId))
                .findFirst();

        if (cartItemOpt.isEmpty()) {
            throw new AppException(ErrorCode.ITEM_NOT_FOUND_IN_CART);
        }

        CartItem cartItem = cartItemOpt.get();
        cartItem.setQuantity(cartItem.getQuantity() + 1); // Increase the quantity

        cartRepository.save(cart);
    }

    public void decreaseItemQuantity(String customerId, String itemId) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        Optional<CartItem> cartItemOpt = cart.getItems().stream()
                .filter(item -> item.getItemId().equals(itemId))
                .findFirst();

        if (cartItemOpt.isEmpty()) {
            throw new AppException(ErrorCode.ITEM_NOT_FOUND_IN_CART);
        }

        CartItem cartItem = cartItemOpt.get();
        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1); // Decrease the quantity
        } else {
            // Remove the item if the quantity reaches zero
            cart.getItems().remove(cartItem);
        }

        cartRepository.save(cart);
    }


}
