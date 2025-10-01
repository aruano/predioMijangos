package com.predio.mijangos.core.response;

import lombok.*;
import java.time.Instant;

/**
 * Contenedor estándar para todas las respuestas HTTP del API.
 *
 * @param <T> tipo del cuerpo (DTO de respuesta)
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ApiResponse<T> {

  /** Código de estado de la operación (ej. 200, 201, 400, 401, 403, 404, 500). */
  private int status_code;

  /** Mensaje corto para el cliente. */
  private String message;

  /** Cuerpo de la respuesta (DTO). */
  private T body;

  /** Timestamp en milisegundos (UTC). */
  private long timestampMillis;

  public static <T> ApiResponse<T> ok(String msg, T body) {
        return ApiResponse.<T>builder()
            .status_code(200)
            .message(msg)
            .body(body)
            .timestampMillis(Instant.now().toEpochMilli())
            .build();
    }

    public static <T> ApiResponse<T> of(int code, String msg, T body) {
        return ApiResponse.<T>builder()
            .status_code(code)
            .message(msg)
            .body(body)
            .timestampMillis(Instant.now().toEpochMilli())
            .build();
    }
}
