package com.example.JewelrySalesSystem.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(100, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(400, "Invalid validation key", HttpStatus.BAD_REQUEST),
    USER_EXISTED(500, "User already exists", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_EXISTED(500, "User not exists", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_FOUND(404, "User not found", HttpStatus.NOT_FOUND),
    USERNAME_REQUIRED(400, "Username is required", HttpStatus.BAD_REQUEST),
    PASSWORD_REQUIRED(400, "Password is required", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(400, "Username must be at least 4 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(400, "Password must be between 8 and 15 characters", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(400, "Email should be valid", HttpStatus.BAD_REQUEST),
    PHONE_INVALID(400, "Phone number is invalid", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(401, "Unauthorized", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(403, "You do not have permission", HttpStatus.FORBIDDEN),
    ADDRESS_INVALID(400, "Address cannot be blank", HttpStatus.BAD_REQUEST),

    // Product-related errors
    PRODUCT_NOT_FOUND(404, "Product not found", HttpStatus.NOT_FOUND),
    PRODUCT_EXISTED(500, "Product already exists", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_PRODUCT_ID(400, "Invalid product ID", HttpStatus.BAD_REQUEST),
    INVALID_PRODUCT_NAME(400, "Product name is invalid", HttpStatus.BAD_REQUEST),
    INVALID_PRODUCT_DESCRIPTION(400, "Product description is invalid", HttpStatus.BAD_REQUEST),
    INVALID_PRODUCT_PRICE(400, "Product price is invalid", HttpStatus.BAD_REQUEST),
    INVALID_PRODUCT_WEIGHT(400, "Product weight is invalid", HttpStatus.BAD_REQUEST),
    INVALID_PRODUCT_STOCK(400, "Product stock quantity is invalid", HttpStatus.BAD_REQUEST),

    // Promotion errors
    PROMOTION_NOT_FOUND(404, "Promotion not found", HttpStatus.NOT_FOUND),

    // Statistics errors
    STATISTICS_NOT_FOUND(404, "Statistics not found", HttpStatus.NOT_FOUND);

    private final int code;
    private final String message;
    private final HttpStatus statusCode;

    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
