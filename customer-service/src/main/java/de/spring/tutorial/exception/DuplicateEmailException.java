package de.spring.tutorial.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Wird ausgelöst, wenn ein Kunde mit einer bereits existierenden E-Mail-Adresse erstellt oder aktualisiert werden soll.
 * Führt zu einer HTTP-Antwort mit dem Statuscode 409 (Conflict).
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateEmailException extends RuntimeException {

    /**
     * Konstruktor für DuplicateEmailException.
     *
     * @param message Detailnachricht zur Beschreibung des Fehlers.
     */
    public DuplicateEmailException(String message) {
        super(message);
    }
}