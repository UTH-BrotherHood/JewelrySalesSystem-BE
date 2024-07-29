package com.example.JewelrySalesSystem.exception;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(100, "Uncategorized exception"),
    INVALID_KEY(400, "Invalid validation key"),
    USER_EXISTED(500, "User already exists"),
    USER_NOT_FOUND(404, "User not found"),
    USERNAME_INVALID(400, "Username must be at least 4 characters"),
    PASSWORD_INVALID(400, "Password must be between 8 and 15 characters"),
    EMAIL_INVALID(400, "Email should be valid"),
    PHONE_INVALID(400, "Phone number is invalid"),
    ADDRESS_INVALID(400, "Address cannot be blank");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
