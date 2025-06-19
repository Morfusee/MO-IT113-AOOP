package com.oop.motorph.utils;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * Generic API response wrapper for standardizing API output structure.
 *
 * @param <T> Type of the response payload.
 */
@Getter
@Setter
public class ApiResponse<T> {

    private LocalDateTime timestamp;
    private int status;
    private String message;
    private T data;
    private Map<String, String> errors;

    /**
     * Constructs an ApiResponse with all fields.
     *
     * @param status  HTTP status code.
     * @param message Response message.
     * @param data    Response body.
     * @param errors  Validation or business errors, if any.
     */
    public ApiResponse(int status, String message, T data, Map<String, String> errors) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.data = data;
        this.errors = errors;
    }

    /**
     * Factory method for successful 200 OK responses.
     *
     * @param message Success message.
     * @param data    Payload to return.
     * @param <T>     Type of the payload.
     * @return ApiResponse instance.
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data, null);
    }

    /**
     * Factory method for 201 Created responses.
     *
     * @param message Creation message.
     * @param data    Payload to return.
     * @param <T>     Type of the payload.
     * @return ApiResponse instance.
     */
    public static <T> ApiResponse<T> created(String message, T data) {
        return new ApiResponse<>(201, message, data, null);
    }

    /**
     * Factory method for error responses with specific HTTP status.
     *
     * @param status  HTTP status code.
     * @param message Error message.
     * @param errors  Error map (e.g., validation field errors).
     * @return ApiResponse instance.
     */
    public static ApiResponse<Void> error(int status, String message, Map<String, String> errors) {
        return new ApiResponse<>(status, message, null, errors);
    }

    /**
     * Factory method for 404 Not Found responses.
     *
     * @param message Not found message.
     * @return ApiResponse instance.
     */
    public static ApiResponse<Void> entityNotFoundException(String message) {
        return new ApiResponse<>(404, message, null, null);
    }

    /**
     * Factory method for 400 Bad Request responses.
     *
     * @param message Error message.
     * @return ApiResponse instance.
     */
    public static ApiResponse<Void> badRequestException(String message) {
        return new ApiResponse<>(400, message, null, null);
    }
}
