package com.predio.mijangos.dto;

import lombok.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private String message;
    private T body;
    private int statusCode;
    @Builder.Default
    private long timestampMillis = Instant.now().toEpochMilli();
}