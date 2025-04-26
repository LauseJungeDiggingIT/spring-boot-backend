package de.spring.tutorial.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Wird ausgelöst, wenn ein Kunde anhand einer ID, E-Mail oder anderen Kriterien nicht gefunden wird.
 * Führt zu einer HTTP-Antwort mit dem Statuscode 404 (Not Found).
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomerNotFoundException extends RuntimeException {

    /**
     * Konstruktor für CustomerNotFoundException.
     *
     * @param message Detailnachricht zur Beschreibung des Fehlers.
     */
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
