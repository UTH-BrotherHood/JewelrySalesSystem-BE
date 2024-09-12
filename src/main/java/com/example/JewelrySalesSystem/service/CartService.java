package com.example.JewelrySalesSystem.service;

import com.example.JewelrySalesSystem.dto.request.CartItemRequest;

import com.example.JewelrySalesSystem.dto.response.CartResponse;
import com.example.JewelrySalesSystem.entity.Cart;
import com.example.JewelrySalesSystem.entity.CartItem;
import com.example.JewelrySalesSystem.entity.Product;
import com.example.JewelrySalesSystem.exception.AppException;
import com.example.JewelrySalesSystem.exception.ErrorCode;
import com.example.JewelrySalesSystem.mapper.CartMapper;
import com.example.JewelrySalesSystem.repository.CartRepository;
import com.example.JewelrySalesSystem.repository.CustomerRepository;
import com.example.JewelrySalesSystem.repository.EmployeeRepository;
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
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;

    public CartResponse getCartByemployeeId(String employeeId) {
        // Check if the cart exists for the given employeeId
        Cart cart = cartRepository.findByEmployeeId(employeeId)
                .orElseGet(() -> {
                    // Create a new empty cart if not found
                    Cart newCart = Cart.builder()
                            .employeeId(employeeId)
                            .items(new ArrayList<>()) // Initialize items list
                            .build();
                    return cartRepository.save(newCart); // Save the new cart
                });

        // Return the cart response
        return cartMapper.toCartResponse(cart);
    }

    public CartResponse addToCart(String employeeId, CartItemRequest request) {
        // Kiểm tra xem khách hàng có tồn tại không
        if (!employeeRepository.existsById(employeeId)) {
            throw new AppException(ErrorCode.EMPLOYEE_NOT_FOUND);
        }

        // Tìm hoặc tạo mới giỏ hàng cho khách hàng
        Cart cart = cartRepository.findByEmployeeId(employeeId)
                .orElseGet(() -> {
                    // Tạo giỏ hàng mới nếu chưa có cho employeeId
                    Cart newCart = Cart.builder()
                            .employeeId(employeeId)
                            .items(new ArrayList<>()) // Khởi tạo danh sách items
                            .build();
                    return cartRepository.save(newCart);
                });

        // Lấy thông tin sản phẩm từ ProductRepository
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        // Tìm CartItem đã tồn tại trong giỏ hàng
        CartItem existingCartItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(request.getProductId()))
                .findFirst()
                .orElse(null);

        if (existingCartItem != null) {
            // Cập nhật số lượng nếu sản phẩm đã tồn tại trong giỏ hàng
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            // Cập nhật giá (nếu cần) và lưu giỏ hàng
            existingCartItem.setPrice(product.getCostPrice());
        } else {
            // Tạo mới CartItem và thêm vào giỏ hàng
            CartItem newCartItem = CartItem.builder()
                    .cart(cart)
                    .productId(request.getProductId())
                    .productName(product.getName()) // Lấy tên sản phẩm từ product
                    .productImageUrl(product.getImageUrl()) // Lấy URL hình ảnh sản phẩm từ product
                    .quantity(request.getQuantity())

                    .price(product.getCostPrice())
                    .build();

            cart.getItems().add(newCartItem);
        }

        Cart savedCart = cartRepository.save(cart);

        return cartMapper.toCartResponse(savedCart);
    }



    public void removeFromCart(String employeeId, String itemId) {
        Cart cart = cartRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        boolean itemExists = cart.getItems().removeIf(item -> item.getItemId().equals(itemId));
        if (!itemExists) {
            throw new AppException(ErrorCode.ITEM_NOT_FOUND_IN_CART); // Custom error code for item not found
        }
        cartRepository.save(cart);
    }
    public void clearCart(String employeeId) {
        Cart cart = cartRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        cart.getItems().clear();
        cartRepository.save(cart);
    }
    public void increaseItemQuantity(String employeeId, String itemId) {
        Cart cart = cartRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        Optional<CartItem> cartItemOpt = cart.getItems().stream()
                .filter(item -> item.getItemId().equals(itemId))
                .findFirst();

        if (cartItemOpt.isEmpty()) {
            throw new AppException(ErrorCode.ITEM_NOT_FOUND_IN_CART);
        }

        CartItem cartItem = cartItemOpt.get();
        cartItem.setQuantity(cartItem.getQuantity() + 1); // Tăng số lượng





        cartRepository.save(cart);
    }

    public void decreaseItemQuantity(String employeeId, String itemId) {
        Cart cart = cartRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        Optional<CartItem> cartItemOpt = cart.getItems().stream()
                .filter(item -> item.getItemId().equals(itemId))
                .findFirst();

        if (cartItemOpt.isEmpty()) {
            throw new AppException(ErrorCode.ITEM_NOT_FOUND_IN_CART);
        }

        CartItem cartItem = cartItemOpt.get();
        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1); // Giảm số lượng
            // Tính lại tổng giá cho item

        } else {
            // Xóa item nếu số lượng bằng 0
            cart.getItems().remove(cartItem);
        }


        cartRepository.save(cart);
    }


    public CartResponse updateCartItemQuantity(String employeeId, String itemId, int newQuantity) {
        // Check if the cart exists for the given employeeId
        Cart cart = cartRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        // Find the cart item by itemId
        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getItemId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.ITEM_NOT_FOUND_IN_CART));

        // Update the quantity of the item
        cartItem.setQuantity(newQuantity);

        // Save the updated cart
        Cart updatedCart = cartRepository.save(cart);

        return cartMapper.toCartResponse(updatedCart);
    }


}
