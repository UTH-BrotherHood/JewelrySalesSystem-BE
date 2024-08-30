package com.example.JewelrySalesSystem.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // General Errors
    UNCATEGORIZED_EXCEPTION(100, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(400, "Invalid validation key", HttpStatus.BAD_REQUEST),
    INVALID_INPUT(400, "Invalid validation INVALID_INPUT", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(401, "Authentication failed: Token expired or invalid", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(403, "You do not have permission", HttpStatus.FORBIDDEN),

    // User Errors
    USER_EXISTED(500, "User already exists", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_EXISTED(500, "User not exists", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NOT_FOUND(404, "User not found", HttpStatus.NOT_FOUND),
    USERNAME_REQUIRED(400, "Username is required", HttpStatus.BAD_REQUEST),
    PASSWORD_REQUIRED(400, "Password is required", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(400, "Username must be at least 4 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(400, "Password must be between 8 and 15 characters", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(400, "Email should be valid", HttpStatus.BAD_REQUEST),
    PHONE_INVALID(400, "Phone number is invalid", HttpStatus.BAD_REQUEST),
    ADDRESS_INVALID(400, "Address cannot be blank", HttpStatus.BAD_REQUEST),

    // Product-related Errors

    PRODUCT_NOT_FOUND(404, "Product not found", HttpStatus.NOT_FOUND),
    PRODUCT_EXISTED(500, "Product already exists", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_PRODUCT_ID(400, "Invalid product ID", HttpStatus.BAD_REQUEST),
    INVALID_PRODUCT_NAME(400, "Product name is invalid", HttpStatus.BAD_REQUEST),
    INVALID_PRODUCT_DESCRIPTION(400, "Product description is invalid", HttpStatus.BAD_REQUEST),
    INVALID_PRODUCT_PRICE(400, "Product price is invalid", HttpStatus.BAD_REQUEST),
    INVALID_PRODUCT_WEIGHT(400, "Product weight is invalid", HttpStatus.BAD_REQUEST),
    INVALID_PRODUCT_STOCK(400, "Product stock quantity is invalid", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_STOCK(400, "Insufficient stock", HttpStatus.BAD_REQUEST),
    PRODUCT_ALREADY_EXISTS(400, "Product already exists", HttpStatus.BAD_REQUEST),

    EMPLOYEE_NOT_FOUND(404, "Employee not found", HttpStatus.NOT_FOUND),
    CUSTOMER_NOT_FOUND(404, "Customer not found", HttpStatus.NOT_FOUND),
    // Promotion Errors
    PROMOTION_NOT_FOUND(404, "Promotion not found", HttpStatus.NOT_FOUND),

    // Statistics Errors
    STATISTICS_NOT_FOUND(404, "Statistics not found", HttpStatus.NOT_FOUND),

    // Categories Errors
    CATEGORY_NOT_FOUND(404, "Category not found", HttpStatus.NOT_FOUND),
    CATEGORY_ALREADY_EXISTS(404, "Category already exists", HttpStatus.BAD_REQUEST),

    // SalesOrder Errors
    SALES_ORDER_NOT_FOUND(404, "Sales order not found", HttpStatus.NOT_FOUND),
    INVALID_ORDER_DATE(400, "Order date is required", HttpStatus.BAD_REQUEST),
    INVALID_TOTAL_AMOUNT(400, "Total amount is required", HttpStatus.BAD_REQUEST),

    // SalesOrderDetail Errors
    INVALID_ORDER_ID(400, "Order ID is required", HttpStatus.BAD_REQUEST),

    INVALID_QUANTITY(400, "Quantity is required", HttpStatus.BAD_REQUEST),
    INVALID_UNIT_PRICE(400, "Unit price is required", HttpStatus.BAD_REQUEST),
    INVALID_TOTAL_PRICE(400, "Total price is required", HttpStatus.BAD_REQUEST),

    // Warranty Errors
    INVALID_WARRANTY_PERIOD(400, "Warranty period is required", HttpStatus.BAD_REQUEST),
    INVALID_WARRANTY_START_DATE(400, "Warranty start date is required", HttpStatus.BAD_REQUEST),
    INVALID_WARRANTY_END_DATE(400, "Warranty end date is required", HttpStatus.BAD_REQUEST),

    // BuyBackOrder Errors
    INVALID_BUYBACK_DATE(400, "Buyback date is required", HttpStatus.BAD_REQUEST),
    INVALID_BUYBACK_TOTAL_AMOUNT(400, "Buyback total amount is required", HttpStatus.BAD_REQUEST),

    // BuyBackOrderDetail Errors
    INVALID_BUYBACK_ID(400, "Buyback ID is required", HttpStatus.BAD_REQUEST),
    INVALID_BUYBACK_PRODUCT_ID(400, "Buyback product ID is required", HttpStatus.BAD_REQUEST),
    INVALID_BUYBACK_QUANTITY(400, "Buyback quantity is required", HttpStatus.BAD_REQUEST),
    INVALID_BUYBACK_UNIT_PRICE(400, "Buyback unit price is required", HttpStatus.BAD_REQUEST),
    INVALID_BUYBACK_TOTAL_PRICE(400, "Buyback total price is required", HttpStatus.BAD_REQUEST),

    // GoldPrice Errors
    INVALID_GOLD_PRICE(400, "Gold price is required", HttpStatus.BAD_REQUEST),

    // Customer Errors
    INVALID_CUSTOMER_NAME(400, "Customer name is required", HttpStatus.BAD_REQUEST),
    INVALID_CUSTOMER_PHONE(400, "Customer phone number is required", HttpStatus.BAD_REQUEST),
    INVALID_CUSTOMER_EMAIL(400, "Customer email is required", HttpStatus.BAD_REQUEST),
    INVALID_CUSTOMER_ADDRESS(400, "Customer address is required", HttpStatus.BAD_REQUEST),
    INVALID_REWARD_POINTS(400, "Reward points are required", HttpStatus.BAD_REQUEST),

    // Employee Errors
    INVALID_EMPLOYEE_NAME(400, "Employee name is required", HttpStatus.BAD_REQUEST),
    INVALID_EMPLOYEE_USERNAME(400, "Employee username is required", HttpStatus.BAD_REQUEST),
    INVALID_EMPLOYEE_PHONE(400, "Employee phone number is required", HttpStatus.BAD_REQUEST),
    INVALID_EMPLOYEE_PASSWORD(400, "Employee password is required", HttpStatus.BAD_REQUEST),
    INVALID_EMPLOYEE_ROLE(400, "Employee role is required", HttpStatus.BAD_REQUEST),

    // Category Errors
    INVALID_CATEGORY_NAME(400, "Category name is required", HttpStatus.BAD_REQUEST),

    // Return Policy Errors
    INVALID_POLICY_DESCRIPTION(400, "Policy description is required", HttpStatus.BAD_REQUEST),
    INVALID_POLICY_TERMS(400, "Policy terms are required", HttpStatus.BAD_REQUEST),

    // Promotion Errors
    INVALID_PROMOTION_DESCRIPTION(400, "Promotion description is required", HttpStatus.BAD_REQUEST),
    INVALID_PROMOTION_START_DATE(400, "Promotion start date is required", HttpStatus.BAD_REQUEST),
    INVALID_PROMOTION_END_DATE(400, "Promotion end date is required", HttpStatus.BAD_REQUEST),
    INVALID_DISCOUNT_PERCENTAGE(400, "Discount percentage is required", HttpStatus.BAD_REQUEST),

    //
    SALES_ORDER_DETAIL_NOT_FOUND(404, "Sales order detail not found", HttpStatus.NOT_FOUND),

    WARRANTY_NOT_FOUND(404,"Warranty not found", HttpStatus.NOT_FOUND),

    // Statistics Errors
    INVALID_REPORT_TYPE(400, "Report type is required", HttpStatus.BAD_REQUEST),
    INVALID_REPORT_DATE(400, "Report date is required", HttpStatus.BAD_REQUEST),
    INVALID_DATA(400, "Data is required", HttpStatus.BAD_REQUEST),

    RESOURCE_NOT_FOUND(404, "Resource not found", HttpStatus.NOT_FOUND),

    CART_IS_EMPTY(404, "Cart is empty", HttpStatus.BAD_REQUEST),
    ITEM_NOT_FOUND_IN_CART(404, "Item not found in cart", HttpStatus.NOT_FOUND),
    CART_NOT_FOUND(404, "Cart not found", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(400, "ROLE NOT FOUND",HttpStatus.BAD_REQUEST);




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
