package de.spring.tutorial.response;

/**
 * Antwortobjekt für erfolgreiche Registrierungen.
 * Enthält nur die nötigen Informationen zur Identifikation des Benutzers.
 */
public record AuthResponse(Long id, String email) {
}
