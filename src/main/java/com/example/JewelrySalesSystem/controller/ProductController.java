package com.example.JewelrySalesSystem.controller;

import com.example.JewelrySalesSystem.dto.request.ApiResponse;
import com.example.JewelrySalesSystem.dto.request.ProductRequests.ProductCreationRequest;
import com.example.JewelrySalesSystem.dto.request.ProductRequests.ProductUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.ProductResponse;
import com.example.JewelrySalesSystem.entity.Product;
import com.example.JewelrySalesSystem.service.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductController {

    ProductService productService;

    @PostMapping
    public ApiResponse<Product> createProduct(@Valid @RequestBody ProductCreationRequest request) {
        ApiResponse<Product> apiResponse = new ApiResponse<>();
        Product product = productService.createProduct(request);
        apiResponse.setCode(201);
        apiResponse.setMessage("Product created successfully");
        apiResponse.setResult(product);
        return apiResponse;
    }

    @PutMapping("/{productId}")
    public ApiResponse<Product> updateProduct(@PathVariable String productId, @RequestBody ProductUpdateRequest request) {
        ApiResponse<Product> apiResponse = new ApiResponse<>();
        Product product = productService.updateProduct(productId, request);
        apiResponse.setCode(200);
        apiResponse.setMessage("Product updated successfully");
        apiResponse.setResult(product);
        return apiResponse;
    }

    @DeleteMapping("/{productId}")
    public ApiResponse<String> deleteProduct(@PathVariable String productId) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        productService.deleteProduct(productId);
        apiResponse.setCode(200);
        apiResponse.setMessage("Product deleted successfully");
        apiResponse.setResult("Product deleted successfully");
        return apiResponse;
    }

    @GetMapping
    public ApiResponse<Page<Product>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal minCostPrice,
            @RequestParam(required = false) BigDecimal maxCostPrice,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {

        ApiResponse<Page<Product>> apiResponse = new ApiResponse<>();
        Page<Product> products = productService.getProducts(page, size, name, minCostPrice, maxCostPrice, sortBy, sortOrder);
        apiResponse.setCode(200);
        apiResponse.setMessage("Products retrieved successfully");
        apiResponse.setResult(products);
        return apiResponse;
    }
    @GetMapping("/{productId}")
    public ApiResponse<Product> getProductById(@PathVariable String productId) {
        ApiResponse<Product> apiResponse = new ApiResponse<>();
        Product product = productService.getProduct(productId);
        if (product != null) {
            apiResponse.setCode(200);
            apiResponse.setMessage("Product retrieved successfully");
            apiResponse.setResult(product);
        } else {
            apiResponse.setCode(404);
            apiResponse.setMessage("Product not found");
            apiResponse.setResult(null);
        }
        return apiResponse;
    }



}
