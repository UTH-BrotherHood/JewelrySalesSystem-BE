package com.example.JewelrySalesSystem.service;

import com.example.JewelrySalesSystem.dto.request.ReturnPolicyRequest;
import com.example.JewelrySalesSystem.dto.response.ReturnPolicyResponse;
import com.example.JewelrySalesSystem.entity.ReturnPolicy;
import com.example.JewelrySalesSystem.exception.AppException;
import com.example.JewelrySalesSystem.exception.ErrorCode;
import com.example.JewelrySalesSystem.repository.ReturnPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReturnPolicyService {

    private final ReturnPolicyRepository returnPolicyRepository;

    @Transactional
    public ReturnPolicyResponse createReturnPolicy(ReturnPolicyRequest request) {
        ReturnPolicy returnPolicy = ReturnPolicy.builder()
                .description(request.getDescription())
                .effectiveDate(request.getEffectiveDate())
                .expiryDate(request.getExpiryDate())
                .build();

        ReturnPolicy savedReturnPolicy = returnPolicyRepository.save(returnPolicy);
        return toReturnPolicyResponse(savedReturnPolicy);
    }

    public ReturnPolicyResponse getReturnPolicy(String returnPolicyId) {
        ReturnPolicy returnPolicy = returnPolicyRepository.findById(returnPolicyId)
                .orElseThrow(() -> new AppException(ErrorCode.RETURN_POLICY_NOT_FOUND));
        return toReturnPolicyResponse(returnPolicy);
    }

    private ReturnPolicyResponse toReturnPolicyResponse(ReturnPolicy returnPolicy) {
        return ReturnPolicyResponse.builder()
                .returnPolicyId(returnPolicy.getReturnPolicyId())
                .description(returnPolicy.getDescription())
                .effectiveDate(returnPolicy.getEffectiveDate())
                .expiryDate(returnPolicy.getExpiryDate())
                .build();
    }
}
