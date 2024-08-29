package com.example.JewelrySalesSystem.exception;

import com.example.JewelrySalesSystem.dto.request.ApiResponse;
import jakarta.validation.ConstraintViolation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.access.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String MIN_ATTRIBUTE = "min";

    //Phương thức này xử lý tất cả các ngoại lệ chung chung (Exception)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception exception) {
        logger.error("Uncategorized exception", exception);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        return ResponseEntity.status(500).body(apiResponse);
    }


    //Phương thức này xử lý các ngoại lệ thuộc loại AppException,
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse> handleAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(apiResponse);
    }

//Phương thức này xử lý ngoại lệ AccessDeniedException,
// thường xảy ra khi người dùng không có quyền truy cập vào tài nguyên nào đó.
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        ApiResponse apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        logger.warn("Access denied: {}", exception.getMessage());
        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    //Phương thức này xử lý ngoại lệ MethodArgumentNotValidException,
// xảy ra khi các tham số đầu vào không hợp lệ. Nó cố gắng ánh xạ thông báo lỗi vào một mã lỗi cụ thể.
// Nếu không tìm thấy mã lỗi phù hợp, nó sẽ sử dụng một mã lỗi chung.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();

        // Collect all errors into a map
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        // Create ApiResponse to return
        ApiResponse<Map<String, String>> apiResponse = new ApiResponse<>();
        apiResponse.setCode(HttpStatus.BAD_REQUEST.value());
        apiResponse.setMessage("Validation error");
        apiResponse.setResult(null);
        apiResponse.setErrors(errors);  // Set the errors map

        return ResponseEntity.badRequest().body(apiResponse);
    }




    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }
}
