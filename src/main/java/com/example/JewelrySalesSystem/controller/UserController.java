package com.example.JewelrySalesSystem.controller;

import com.example.JewelrySalesSystem.dto.request.UserCreationRequest;
import com.example.JewelrySalesSystem.entity.User;
import com.example.JewelrySalesSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/users")
    User createUser(@RequestBody UserCreationRequest request) {
       return userService.createRequest(request) ;
    }
}
