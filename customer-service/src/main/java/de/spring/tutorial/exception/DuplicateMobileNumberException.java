package de.spring.tutorial.exception;

/**
 * Exception, die geworfen wird, wenn ein Kunde mit der angegebenen E-Mail bereits existiert.
 */
public class DuplicateMobileNumberException extends RuntimeException {

    /**
     * Konstruktor für DuplicateEmailException.
     *
     * @param message Fehlermeldung, die den Fehler beschreibt.
     */
    public DuplicateMobileNumberException(String message) {
        super(message);
    }
}