package com.example.JewelrySalesSystem.exception;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(100, "Uncategorized exception"),
    INVALID_KEY(400, "Invalid validation key"),
    USER_EXISTED(500, "User already exists"),
    USER_NOT_EXISTED(500, "User not exists"),
    USER_NOT_FOUND(404, "User not found"),
    USERNAME_REQUIRED(400, "Username is required"),
    PASSWORD_REQUIRED(400, "Password is required"),
    USERNAME_INVALID(400, "Username must be at least 4 characters"),
    PASSWORD_INVALID(400, "Password must be between 8 and 15 characters"),
    EMAIL_INVALID(400, "Email should be valid"),
    PHONE_INVALID(400, "Phone number is invalid"),
    UNAUTHENTICATED(400, "Unauthenticated"),
    ADDRESS_INVALID(400, "Address cannot be blank"),

    // Product-related errors
    PRODUCT_NOT_FOUND(404, "Product not found"),
    PRODUCT_EXISTED(500, "Product already exists"),
    INVALID_PRODUCT_ID(400, "Invalid product ID"),
    INVALID_PRODUCT_NAME(400, "Product name is invalid"),
    INVALID_PRODUCT_DESCRIPTION(400, "Product description is invalid"),
    INVALID_PRODUCT_PRICE(400, "Product price is invalid"),
    INVALID_PRODUCT_WEIGHT(400, "Product weight is invalid"),
    INVALID_PRODUCT_STOCK(400, "Product stock quantity is invalid"),

    //Promotion errors
    PROMOTION_NOT_FOUND(404,"Promotion not found"),
    // Statistics errors
    STATISTICS_NOT_FOUND(404,"Statics not found");


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
