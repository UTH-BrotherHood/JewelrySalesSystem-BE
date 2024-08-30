package com.example.JewelrySalesSystem.service;

import com.example.JewelrySalesSystem.dto.request.ProductRequests.ProductCreationRequest;
import com.example.JewelrySalesSystem.dto.request.ProductRequests.ProductUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.ProductResponse;
import com.example.JewelrySalesSystem.entity.Product;
import com.example.JewelrySalesSystem.exception.AppException;
import com.example.JewelrySalesSystem.exception.ErrorCode;
import com.example.JewelrySalesSystem.mapper.ProductMapper;
import com.example.JewelrySalesSystem.repository.CategoryRepository;
import com.example.JewelrySalesSystem.repository.ProductRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    public ProductResponse createProduct(ProductCreationRequest request) {
        if (!categoryRepository.existsById(request.getCategoryName())) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        if (productRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.PRODUCT_ALREADY_EXISTS);
        }

        Product product = productMapper.toProduct(request);
        Product savedProduct = productRepository.save(product);
        return productMapper.toProductResponse(savedProduct);
    }

    public ProductResponse updateProduct(String productId, ProductUpdateRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        productMapper.updateProduct(product, request);
        Product updatedProduct = productRepository.save(product);
        return productMapper.toProductResponse(updatedProduct);
    }

    public void deleteProduct(String productId) {
        if (!productRepository.existsById(productId)) {
            throw new AppException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        productRepository.deleteById(productId);
    }

    public Page<ProductResponse> getProducts(
            int page, int size, String name, BigDecimal minCostPrice, BigDecimal maxCostPrice, String sortBy, String sortOrder) {

        Specification<Product> spec = Specification.where(null);

        if (name != null && !name.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + name + "%"));
        }

        if (minCostPrice != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("costPrice"), minCostPrice));
        }

        if (maxCostPrice != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("costPrice"), maxCostPrice));
        }

        Sort.Direction direction = Sort.Direction.fromString(sortOrder);
        Sort sort = Sort.by(direction, sortBy);

        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return productRepository.findAll(spec, pageRequest)
                .map(productMapper::toProductResponse);
    }

    public ProductResponse getProduct(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        return productMapper.toProductResponse(product);
    }
}
