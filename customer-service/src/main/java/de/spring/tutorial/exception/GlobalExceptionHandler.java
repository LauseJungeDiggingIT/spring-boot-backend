package de.spring.tutorial.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Globaler Fehlerbehandler, der alle Exceptions im Projekt verarbeitet.
 * Diese Klasse ist verantwortlich für das Abfangen von spezifischen Exceptions
 * und das Erstellen einer strukturierten JSON-Fehlermeldung.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Behandelt die {@link CustomerNotFoundException} und gibt eine detaillierte Fehlermeldung zurück.
     *
     * @param ex Die ausgelöste {@link CustomerNotFoundException}.
     * @return Eine ResponseEntity mit dem Fehlerstatus und der Fehlermeldung.
     */
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        ErrorResponse errorResponse = ErrorResponseUtil.createErrorResponse(
                HttpStatus.NOT_FOUND,
                "CUSTOMER_NOT_FOUND",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Behandelt die {@link DuplicateEmailException} und gibt eine detaillierte Fehlermeldung zurück.
     *
     * @param ex Die ausgelöste {@link DuplicateEmailException}.
     * @return Eine ResponseEntity mit dem Fehlerstatus und der Fehlermeldung.
     */
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmailException(DuplicateEmailException ex) {
        ErrorResponse errorResponse = ErrorResponseUtil.createErrorResponse(
                HttpStatus.BAD_REQUEST,
                "DUPLICATE_EMAIL",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Behandelt die {@link DuplicateMobileNumberException} und gibt eine detaillierte Fehlermeldung zurück.
     *
     * @param ex Die ausgelöste {@link DuplicateMobileNumberException}.
     * @return Eine ResponseEntity mit dem Fehlerstatus und der Fehlermeldung.
     */
    @ExceptionHandler(DuplicateMobileNumberException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateMobileNumberException(DuplicateMobileNumberException ex) {
        ErrorResponse errorResponse = ErrorResponseUtil.createErrorResponse(
                HttpStatus.BAD_REQUEST,
                "DUPLICATE_MOBILE_NUMBER",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Behandelt allgemeine {@link Exception}-Fehler und gibt eine detaillierte Fehlermeldung zurück.
     *
     * @param ex Die ausgelöste {@link Exception}.
     * @return Eine ResponseEntity mit dem Fehlerstatus und der Fehlermeldung.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse errorResponse = ErrorResponseUtil.createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "GENERAL_ERROR",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
