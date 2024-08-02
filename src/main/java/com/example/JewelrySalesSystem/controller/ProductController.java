package com.example.JewelrySalesSystem.controller;

import com.example.JewelrySalesSystem.dto.request.ApiResponse;
import com.example.JewelrySalesSystem.dto.request.ProductCreationRequest;
import com.example.JewelrySalesSystem.dto.request.ProductUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.ProductResponse;
import com.example.JewelrySalesSystem.entity.Product;
import com.example.JewelrySalesSystem.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ApiResponse<Product> createProduct(@RequestBody ProductCreationRequest request) {
        Product product = productService.createProduct(request);
        return ApiResponse.<Product>builder()
                .code(201)
                .message("Product created successfully")
                .result(product)
                .build();
    }

    @PutMapping("/{productId}")
    public ApiResponse<Product> updateProduct(@PathVariable Integer productId, @RequestBody ProductUpdateRequest request) {
        Product product = productService.updateProduct(productId, request);
        return ApiResponse.<Product>builder()
                .code(200)
                .message("Product updated successfully")
                .result(product)
                .build();
    }

    @DeleteMapping("/{productId}")
    public ApiResponse<String> deleteProduct(@PathVariable Integer productId) {
        productService.deleteProduct(productId);
        return ApiResponse.<String>builder()
                .code(200)
                .message("Product deleted successfully")
                .result("Product deleted successfully")
                .build();
    }

    @GetMapping
    public ApiResponse<Page<Product>> getProducts(Pageable pageable) {
        Page<Product> products = productService.getProducts(pageable);
        return ApiResponse.<Page<Product>>builder()
                .code(200)
                .message("Products retrieved successfully")
                .result(products)
                .build();
    }

    @GetMapping("/{productId}")
    public ApiResponse<ProductResponse> getProduct(@PathVariable Integer productId) {
        Product product = productService.getProduct(productId);
        return ApiResponse.<ProductResponse>builder()
                .code(200)
                .message("Product retrieved successfully")
                .result(new ProductResponse(product))
                .build();
    }
}
