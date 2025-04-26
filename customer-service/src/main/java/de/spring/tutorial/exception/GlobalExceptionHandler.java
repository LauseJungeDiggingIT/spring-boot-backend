package de.spring.tutorial.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Globale Fehlerbehandlung für die gesamte Anwendung.
 * Fängt Exceptions ab und erstellt standardisierte Fehlerantworten.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Behandelt alle {@link RuntimeException}-Fälle.
     * Nutzt, falls vorhanden, den über {@link ResponseStatus} definierten HTTP-Status.
     *
     * @param ex Die aufgetretene RuntimeException.
     * @return ResponseEntity mit einer Fehlerbeschreibung.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        HttpStatus status = resolveAnnotatedStatus(ex);
        return ResponseEntity
                .status(status)
                .body(ErrorResponseUtil.createErrorResponse(status, ex.getMessage()));
    }

    /**
     * Behandelt alle allgemeinen {@link Exception}-Fälle,
     * die nicht explizit anders abgefangen wurden.
     *
     * @param ex Die aufgetretene Exception.
     * @return ResponseEntity mit Fehlerdetails und Statuscode 500.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponseUtil.createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()));
    }

    /**
     * Ermittelt den HTTP-Status aus der {@link ResponseStatus}-Annotation der Exception-Klasse.
     * Fällt zurück auf {@link HttpStatus#INTERNAL_SERVER_ERROR}, wenn keine Annotation vorhanden ist.
     *
     * @param ex Die Exception.
     * @return Der ermittelte HTTP-Status.
     */
    private HttpStatus resolveAnnotatedStatus(Throwable ex) {
        ResponseStatus annotation = ex.getClass().getAnnotation(ResponseStatus.class);
        if (annotation != null) {
            return annotation.value();
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
