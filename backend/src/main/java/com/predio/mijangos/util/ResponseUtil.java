package com.predio.mijangos.util;

import com.predio.mijangos.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static <T> ResponseEntity<ApiResponse<T>> createResponse(String message, T body, HttpStatus status) {
        ApiResponse<T> response = ApiResponse.<T>builder()
                .message(message)
                .body(body)
                .statusCode(status.value())
                .timestampMillis(System.currentTimeMillis())
                .build();
        return new ResponseEntity<>(response, status);
    }
}
