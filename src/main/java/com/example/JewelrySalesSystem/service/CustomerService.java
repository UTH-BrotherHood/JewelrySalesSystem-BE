package com.example.JewelrySalesSystem.service;

import com.example.JewelrySalesSystem.constant.PredefinedRole;
import com.example.JewelrySalesSystem.dto.request.CustomerRequests.CustomerCreationRequest;
import com.example.JewelrySalesSystem.dto.request.CustomerRequests.CustomerUpdateRequest;
import com.example.JewelrySalesSystem.dto.response.CustomerResponse;
import com.example.JewelrySalesSystem.entity.Customer;
import com.example.JewelrySalesSystem.entity.RewardPointHistory;
import com.example.JewelrySalesSystem.entity.Role;
import com.example.JewelrySalesSystem.exception.AppException;
import com.example.JewelrySalesSystem.exception.ErrorCode;
import com.example.JewelrySalesSystem.mapper.CustomerMapper;
import com.example.JewelrySalesSystem.repository.CustomerRepository;
import com.example.JewelrySalesSystem.repository.RewardPointHistoryRepository;
import com.example.JewelrySalesSystem.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerService {
    CustomerRepository customerRepository;
    CustomerMapper customerMapper;
    RoleRepository roleRepository;
    RewardPointHistoryRepository rewardPointHistoryRepository;

    public CustomerResponse createCustomer(CustomerCreationRequest request) {
        // Check if the customer name already exists
        if (customerRepository.existsBycustomername(request.getCustomername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        // Map the request to the Customer entity
        Customer customer = customerMapper.toCustomer(request);

        if (customer.getRewardPoints() == null) {
            customer.setRewardPoints(0);
        }

        if (customer.getRankLevel() == null) {
            customer.setRankLevel("Bronze");
        }

        // Retrieve the CUSTOMER role
        Role customerRole = roleRepository.findByName(PredefinedRole.CUSTOMER_ROLE)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        // Create a Set<Role> and add the CUSTOMER role to it
        Set<Role> roles = new HashSet<>();
        roles.add(customerRole);
        customer.setRoles(roles);

        // Save and return the customer
        Customer createdCustomer = customerRepository.save(customer);
        return customerMapper.toCustomerResponse(createdCustomer);
    }


    public List<CustomerResponse> getCustomers() {
        return customerRepository.findAll().stream().map(customerMapper::toCustomerResponse).toList();
    }

    public CustomerResponse getCustomer(String customerId) {
        return customerMapper.toCustomerResponse(customerRepository.findById(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    public CustomerResponse updateCustomer(String customerId, CustomerUpdateRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        customerMapper.updateCustomer(customer, request);
        var updatedCustomer = customerRepository.save(customer);
        return customerMapper.toCustomerResponse(updatedCustomer);
    }

    public void deleteCustomer(String customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        customerRepository.deleteById(customerId);
    }



    // Cập nhật điểm thưởng cho khách hàng
    public void addRewardPoints(String customerId, BigDecimal points, String description) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        customer.setRewardPoints(customer.getRewardPoints() + points.intValue());
        customerRepository.save(customer);

        RewardPointHistory history = new RewardPointHistory();
        history.setCustomerId(customerId);
        history.setPoints(points);
        history.setDate(LocalDateTime.now());
        history.setDescription(description);
        rewardPointHistoryRepository.save(history);

        // Cập nhật cấp độ thành viên
        updateCustomerRank(customer);
    }

    // Cập nhật cấp độ thành viên dựa trên điểm thưởng
    private void updateCustomerRank(Customer customer) {
        Integer points = customer.getRewardPoints();
        String rankLever = "Bronze"; // Mặc định là Bronze

        if (points >= 1000) {
            rankLever = "Platinum";
        } else if (points >= 500) {
            rankLever = "Gold";
        } else if (points >= 200) {
            rankLever = "Silver";
        }

        customer.setRankLevel(rankLever);
        customerRepository.save(customer);
    }
}
