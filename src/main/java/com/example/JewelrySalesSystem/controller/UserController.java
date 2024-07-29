package com.example.JewelrySalesSystem.controller;

import com.example.JewelrySalesSystem.dto.request.ApiResponse;
import com.example.JewelrySalesSystem.dto.request.UserCreationRequest;
import com.example.JewelrySalesSystem.dto.request.UserUpdateRequest;
import com.example.JewelrySalesSystem.entity.User;
import com.example.JewelrySalesSystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        User createdUser = userService.createUser(request);
        apiResponse.setCode(201); // Assuming 201 for created
        apiResponse.setMessage("User created successfully");
        apiResponse.setResult(createdUser);
        return apiResponse;
    }

    @GetMapping
    public ApiResponse<List<User>> getUsers() {
        ApiResponse<List<User>> apiResponse = new ApiResponse<>();
        List<User> users = userService.getUsers();
        apiResponse.setCode(200);
        apiResponse.setMessage("Users retrieved successfully");
        apiResponse.setResult(users);
        return apiResponse;
    }

    @GetMapping("/{userId}")
    public ApiResponse<User> getUser(@PathVariable("userId") String userId) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        User user = userService.getUser(userId);
        apiResponse.setCode(200);
        apiResponse.setMessage("User retrieved successfully");
        apiResponse.setResult(user);
        return apiResponse;
    }

    @PutMapping("/{userId}")
    public ApiResponse<User> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        User updatedUser = userService.updateUser(userId, request);
        apiResponse.setCode(200);
        apiResponse.setMessage("User updated successfully");
        apiResponse.setResult(updatedUser);
        return apiResponse;
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<String> deleteUser(@PathVariable String userId) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        userService.deleteUser(userId);
        apiResponse.setCode(200);
        apiResponse.setMessage("User has been deleted");
        apiResponse.setResult("User has been deleted");
        return apiResponse;
    }
}
