package com.oop.motorph.utils;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private T data;
    private Map<String, String> errors;

    public ApiResponse(int status, String message, T data, Map<String, String> errors) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.data = data;
        this.errors = errors;
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data, null);
    }

    public static <T> ApiResponse<T> created(String message, T data) {
        return new ApiResponse<>(201, message, data, null);
    }

    public static ApiResponse<Void> error(int status, String message, Map<String, String> errors) {
        return new ApiResponse<>(status, message, null, errors);
    }

    public static ApiResponse<Void> entityNotFoundException(String message) {
        return new ApiResponse<>(404, message, null, null);
    }

    public static ApiResponse<Void> badRequestException(String message) {
        return new ApiResponse<>(400, message, null, null);
    }
}
