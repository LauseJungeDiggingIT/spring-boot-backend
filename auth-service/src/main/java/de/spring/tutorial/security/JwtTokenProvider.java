package de.spring.tutorial.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Diese Klasse stellt Funktionen zum Erzeugen, Validieren und Verarbeiten von JWT (JSON Web Tokens) zur Verfügung.
 * Sie wird verwendet, um Benutzer zu authentifizieren, indem ein Token erstellt und validiert wird.
 */
@Component
@Slf4j
public class JwtTokenProvider {

    private final SecretKey jwtSecret;

    /**
     * Konstruktor, der den geheimen Schlüssel für die JWT-Erstellung aus den Anwendungseigenschaften lädt.
     *
     * @param secret Der Base64-codierte geheime Schlüssel, der zum Signieren der Tokens verwendet wird.
     */
    public JwtTokenProvider(@Value("${app.jwtSecret}") String secret) {
        this.jwtSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    /**
     * Erzeugt ein JWT für den angegebenen Benutzer-E-Mail.
     * Das Token ist 7 Tage gültig.
     *
     * @param userEmail Die E-Mail des Benutzers, die als Subjekt des Tokens verwendet wird.
     * @return Ein JWT als String.
     */
    public String generateToken(String userEmail) {
        Instant now = Instant.now();
        Instant expiration = now.plus(7, ChronoUnit.DAYS);

        return Jwts.builder()
                .subject(userEmail)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(jwtSecret, Jwts.SIG.HS512)
                .compact();
    }

    /**
     * Erzeugt ein JWT basierend auf den Authentifizierungsinformationen des Benutzers.
     *
     * @param authentication Die Authentifizierung des Benutzers, die die E-Mail des Benutzers enthält.
     * @return Ein JWT als String.
     */
    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return generateToken(user.getUsername());
    }
}
