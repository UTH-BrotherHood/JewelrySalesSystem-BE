package com.example.JewelrySalesSystem.service;

import com.example.JewelrySalesSystem.dto.request.UserCreationRequest;
import com.example.JewelrySalesSystem.entity.User;
import com.example.JewelrySalesSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createRequest(UserCreationRequest request){
        User user = new User();

        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setRewardPoints(request.getRewardPoints());

        return userRepository.save(user);

    }
}
