package de.spring.tutorial.exception;

/**
 * Exception, die geworfen wird, wenn ein Kunde mit der angegebenen E-Mail bereits existiert.
 */
public class DuplicateEmailException extends RuntimeException {

    /**
     * Konstruktor für DuplicateEmailException.
     *
     * @param message Fehlermeldung, die den Fehler beschreibt.
     */
    public DuplicateEmailException(String message) {
        super(message);
    }
}
