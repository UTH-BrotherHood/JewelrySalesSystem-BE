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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
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
    private final JavaMailSenderImpl mailSender;

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

        if (request.getCustomername() != null && !request.getCustomername().isEmpty()) {
            customer.setCustomername(request.getCustomername());
        }
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            customer.setEmail(request.getEmail());
        }
        if (request.getPhone() != null && !request.getPhone().isEmpty()) {
            customer.setPhone(request.getPhone());
        }
        if (request.getAddress() != null && !request.getAddress().isEmpty()) {
            customer.setAddress(request.getAddress());
        }

        var updatedCustomer = customerRepository.save(customer);

        return customerMapper.toCustomerResponse(updatedCustomer);
    }


    public void deleteCustomer(String customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        customerRepository.deleteById(customerId);
    }

    public CustomerResponse getCustomerByName(String customerName) {
        // Retrieve the customer by name
        Customer customer = customerRepository.findByCustomername(customerName)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return customerMapper.toCustomerResponse(customer);
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
        String rankLevel = "Bronze"; // Default is Bronze

        if (points >= 10000) {
            rankLevel = "Platinum";
        } else if (points >= 5000) {
            rankLevel = "Gold";
        } else if (points >= 2000) {
            rankLevel = "Silver";
        } else {
            rankLevel = "Bronze";
        }

        customer.setRankLevel(rankLevel);
        customerRepository.save(customer);
    }


    public boolean checkForRankUpgrade(String customerId) {
        // Lấy thông tin khách hàng từ cơ sở dữ liệu
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_NOT_FOUND));

        Integer rewardPoints = customer.getRewardPoints();  // Số điểm thưởng hiện tại của khách hàng
        String currentRank = customer.getRankLevel();  // Cấp bậc hiện tại của khách hàng
        String newRank = currentRank;  // Khởi tạo cấp bậc mới

        // Kiểm tra điều kiện nâng hạng dựa trên số điểm thưởng
        if (rewardPoints >= 10000 && !"Platinum".equals(currentRank)) {
            newRank = "Platinum";
        } else if (rewardPoints >= 5000 && !"Gold".equals(currentRank)) {
            newRank = "Gold";
        } else if (rewardPoints >= 2000 && !"Silver".equals(currentRank)) {
            newRank = "Silver";
        } else if (rewardPoints >= 500 && !"Bronze".equals(currentRank)) {
            newRank = "Bronze";
        }

        // Nếu có sự thay đổi cấp bậc, cập nhật và lưu khách hàng
        if (!newRank.equals(currentRank)) {
            customer.setRankLevel(newRank);
            customerRepository.save(customer);
            return true;  // Đã nâng hạng
        }

        return false;  // Không có sự thay đổi
    }

}
