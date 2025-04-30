package de.spring.tutorial.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Komponente zur Erstellung und Validierung von JWTs.

 * Diese Klasse verwendet das Secret aus den Application Properties zur Validierung der Signatur
 * und stellt Methoden bereit, um Informationen wie die E-Mail-Adresse (Subject) aus dem Token zu extrahieren.
 */
@Component
@Slf4j
public class JwtTokenProvider {

    private final SecretKey jwtSecret;

    /**
     * Initialisiert den Token-Provider mit dem konfigurierten geheimen Schlüssel (Base64-kodiert).
     *
     * @param secret das Base64-kodierte JWT-Secret aus der Konfiguration (z. B. application.yml)
     */
    public JwtTokenProvider(@Value("${app.jwtSecret}") String secret) {
        this.jwtSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    /**
     * Validiert ein JWT.
     *
     * @param token das zu überprüfende Token
     * @return {@code true}, wenn das Token gültig ist und nicht abgelaufen; andernfalls {@code false}
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(jwtSecret)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            if (claims.getExpiration().before(new Date())) {
                log.warn("Token abgelaufen: {}", token);
                return false;
            }

            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            log.error("Ungültiges JWT: {}", ex.getMessage());
            return false;
        }
    }

    /**
     * Extrahiert die Benutzer-Mail (Subject) aus dem JWT.
     *
     * @param token das JWT
     * @return die E-Mail-Adresse des Benutzers (Subject)
     */
    public String getUserMailFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(jwtSecret)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }
}
