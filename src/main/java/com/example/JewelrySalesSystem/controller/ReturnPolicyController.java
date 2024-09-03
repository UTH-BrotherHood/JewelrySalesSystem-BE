package com.example.JewelrySalesSystem.controller;

import com.example.JewelrySalesSystem.dto.request.ReturnPolicyRequest;
import com.example.JewelrySalesSystem.dto.response.ReturnPolicyResponse;
import com.example.JewelrySalesSystem.service.ReturnPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/return-policies")
public class ReturnPolicyController {

    private final ReturnPolicyService returnPolicyService;

    @PostMapping
    public ResponseEntity<ReturnPolicyResponse> createReturnPolicy(@RequestBody ReturnPolicyRequest request) {
        ReturnPolicyResponse response = returnPolicyService.createReturnPolicy(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReturnPolicyResponse> getReturnPolicy(@PathVariable("id") String returnPolicyId) {
        ReturnPolicyResponse response = returnPolicyService.getReturnPolicy(returnPolicyId);
        return ResponseEntity.ok(response);
    }
}
