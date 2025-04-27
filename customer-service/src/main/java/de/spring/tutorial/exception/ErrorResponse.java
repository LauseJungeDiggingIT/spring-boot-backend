package de.spring.tutorial.exception;

import java.time.LocalDateTime;

/**
 * Datentransferobjekt für Fehlerantworten.
 * Enthält Details wie Zeitstempel, HTTP-Statuscode, Fehlerbeschreibung und eine benutzerfreundliche Nachricht.
 *
 * @param timestamp Zeitpunkt des Fehlers.
 * @param status HTTP-Statuscode.
 * @param error Kurzbeschreibung des Fehlertyps.
 * @param errorCode FehlerCode.
 * @param message Detaillierte Fehlermeldung.
 */
public record ErrorResponse(LocalDateTime timestamp, int status, String error, String errorCode, String message) {

}

