package de.spring.tutorial.security;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

/**
 * Filter zur JWT-Authentifizierung.
 * <p>
 * Dieser Filter wird bei jeder eingehenden HTTP-Anfrage ausgeführt. Er extrahiert das JWT aus dem
 * Authorization-Header, validiert es über den {@link JwtTokenProvider} und setzt bei erfolgreicher
 * Validierung die Benutzeridentität (in diesem Fall die E-Mail) in den SecurityContext von Spring Security.
 * <p>
 * Es werden dabei keine Rollen (Authorities) gesetzt – die Authentifizierung dient nur zur
 * Identifizierung des Benutzers, nicht zur Autorisierung.
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Konstruktor für den JWT-Authentifizierungsfilter.
     *
     * @param jwtTokenProvider der Provider zur Token-Validierung und Extraktion von Benutzerinformationen.
     */
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Führt die JWT-Validierung durch und setzt bei erfolgreicher Validierung die Benutzeridentität
     * in den Spring Security Kontext.
     *
     * @param request die aktuelle HTTP-Anfrage
     * @param response die aktuelle HTTP-Antwort
     * @param filterChain die restliche Filterkette
     * @throws ServletException im Fehlerfall
     * @throws IOException im Fehlerfall
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = getJwtFromRequest(request);

        if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
            String mail = jwtTokenProvider.getUserMailFromToken(jwt);

            log.info("Token ist gültig. Extrahierte E-MAil: {}", mail);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(mail, null, List.of());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            log.warn("Token ist nicht vorhanden oder ungültig.");
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extrahiert das JWT aus dem Authorization-Header der HTTP-Anfrage.
     * Erwartet das Format: "Bearer <token>".
     *
     * @param request die aktuelle HTTP-Anfrage
     * @return das extrahierte JWT oder {@code null}, wenn kein gültiges Token vorhanden ist
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
