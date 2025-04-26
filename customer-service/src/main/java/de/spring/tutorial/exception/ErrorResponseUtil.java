package de.spring.tutorial.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public final class ErrorResponseUtil {

    private ErrorResponseUtil() {
    }

    public static ErrorResponse createErrorResponse(HttpStatus status, String message) {
        return new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message
        );
    }
}