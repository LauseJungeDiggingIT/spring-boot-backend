package de.spring.tutorial.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * Hilfsklasse zum Erstellen von {@link ErrorResponse}-Objekten.
 */
public final class ErrorResponseUtil {

    private ErrorResponseUtil() {
        // Utility-Klasse, sollte nicht instanziiert werden.
    }

    /**
     * Erstellt ein {@link ErrorResponse}-Objekt mit aktuellem Zeitstempel, HTTP-Status und einer Nachricht.
     *
     * @param status Der HTTP-Status.
     * @param message Die Fehlermeldung.
     * @return Ein neues {@link ErrorResponse}-Objekt.
     */
    public static ErrorResponse createErrorResponse(HttpStatus status, String message) {
        return new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message
        );
    }
}
