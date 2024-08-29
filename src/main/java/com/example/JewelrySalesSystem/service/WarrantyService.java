package com.example.JewelrySalesSystem.service;

import com.example.JewelrySalesSystem.dto.request.WarrantyRequests.WarrantyCreationRequest;
import com.example.JewelrySalesSystem.dto.response.WarrantyResponse;
import com.example.JewelrySalesSystem.entity.Warranty;
import com.example.JewelrySalesSystem.exception.AppException;
import com.example.JewelrySalesSystem.exception.ErrorCode;
import com.example.JewelrySalesSystem.mapper.WarrantyMapper;
import com.example.JewelrySalesSystem.repository.ProductRepository;
import com.example.JewelrySalesSystem.repository.WarrantyRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class WarrantyService {

    WarrantyRepository warrantyRepository;
    WarrantyMapper warrantyMapper;
    ProductRepository productRepository;

    public WarrantyResponse createWarranty(WarrantyCreationRequest request) {
        var product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        Warranty warranty = warrantyMapper.toWarranty(request);
        warranty.setProduct(product);

        return warrantyMapper.toWarrantyResponse(warrantyRepository.save(warranty));
    }

    public WarrantyResponse getWarranty(String warrantyId) {
        return warrantyRepository.findById(warrantyId)
                .map(warrantyMapper::toWarrantyResponse)
                .orElseThrow(() -> new AppException(ErrorCode.WARRANTY_NOT_FOUND));
    }

    public void deleteWarranty(String warrantyId) {
        if (!warrantyRepository.existsById(warrantyId)) {
            throw new AppException(ErrorCode.WARRANTY_NOT_FOUND);
        }
        warrantyRepository.deleteById(warrantyId);
    }

    public List<WarrantyResponse> getWarrantiesByProductId(String productId) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        return warrantyRepository.findByProduct(product)
                .stream()
                .map(warrantyMapper::toWarrantyResponse)
                .toList();
    }

    public List<WarrantyResponse> getAllWarranties() {
        return warrantyRepository.findAll()
                .stream()
                .map(warrantyMapper::toWarrantyResponse)
                .toList();
    }
}
