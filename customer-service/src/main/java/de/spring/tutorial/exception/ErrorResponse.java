package de.spring.tutorial.exception;

import java.time.LocalDateTime;

/**
 * @param timestamp Getter und Setter
 */

public record ErrorResponse(LocalDateTime timestamp, int status, String error, String message) {

}