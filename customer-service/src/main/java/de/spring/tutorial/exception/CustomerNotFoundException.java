package de.spring.tutorial.exception;

/**
 * Exception, die geworfen wird, wenn ein Kunde nicht gefunden wird.
 */
public class CustomerNotFoundException extends RuntimeException {

    /**
     * Konstruktor für CustomerNotFoundException.
     *
     * @param message Fehlermeldung, die den Fehler beschreibt.
     */
    public CustomerNotFoundException(String message) {
        super(message);
    }
}