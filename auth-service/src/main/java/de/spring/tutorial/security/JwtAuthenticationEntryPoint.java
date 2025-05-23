package de.spring.tutorial.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

/**
 * Dieser Eintragspunkt wird verwendet, um Authentifizierungsfehler zu behandeln, wenn der Benutzer
 * nicht authentifiziert ist oder ungültige Anmeldeinformationen bereitstellt.
 * Die `commence`-Methode sendet eine HTTP-Fehlermeldung mit dem Statuscode `401 Unauthorized`,
 * um anzuzeigen, dass der Benutzer nicht autorisiert ist, auf die angeforderte Ressource zuzugreifen.
 */
@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Wird aufgerufen, wenn eine Authentifizierung erforderlich ist und der Benutzer nicht authentifiziert ist.
     * Diese Methode sendet eine `401 Unauthorized`-Antwort an den Client, wenn die Authentifizierung fehlschlägt.
     * Außerdem wird eine detailliertere Fehlermeldung im JSON-Format zurückgegeben.
     *
     * @param request Der HttpServletRequest, der die Anfrage des Clients enthält.
     * @param response Der HttpServletResponse, der für die Antwort an den Client verwendet wird.
     * @param authException Die Ausnahme, die während des Authentifizierungsprozesses aufgetreten ist.
     * @throws IOException Wenn ein Fehler beim Schreiben der Antwort auftritt.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        // Loggen der Authentifizierungsfehler für die Diagnose
        log.error("Authentication failed: {}", authException.getMessage());

        // Antwort mit einem "401 Unauthorized"-Fehler zurücksenden
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        // Fehlernachricht im JSON-Format senden
        String message = "{\"error\": \"Unauthorized\", \"message\": \"Authentication failed\"}";
        response.getWriter().write(message);
    }
}
