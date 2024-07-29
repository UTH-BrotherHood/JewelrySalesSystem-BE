package com.example.JewelrySalesSystem.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserCreationRequest {

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @Size(min = 8, max = 15, message = "Password must be between 8 and 15 characters")
    private String password;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number is invalid")
    private String phone;

    @NotBlank(message = "Address cannot be blank")
    private String address;

    private Integer rewardPoints;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(Integer rewardPoints) {
        this.rewardPoints = rewardPoints;
    }
}
