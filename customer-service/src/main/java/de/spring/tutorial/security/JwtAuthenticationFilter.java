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

import java.io.IOException;

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
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Erstellt einen neuen JwtAuthenticationFilter.
     *
     * @param jwtTokenProvider der Provider zur Token-validierung und Extraktion von Informationen.
     */
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Führt die JWT-Validierung für eingehende Anfragen durch und setzt bei Erfolg
     * eine einfache {@link UsernamePasswordAuthenticationToken} in den Sicherheitskontext.
     *
     * @param request      die aktuelle HTTP-Anfrage
     * @param response     die aktuelle HTTP-Antwort
     * @param filterChain  die restliche Filterkette
     * @throws ServletException im Fehlerfall
     * @throws IOException      im Fehlerfall
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = getJwtFromRequest(request);

        if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
            String mail = jwtTokenProvider.getUserMailFromToken(jwt);

            // Authentifizierung ohne Authorities
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(mail, null);

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extrahiert das JWT aus dem Authorization-Header der HTTP-Anfrage.
     * Erwartet das Format: "Bearer &lt;token&gt;".
     *
     * @param request die aktuelle HTTP-Anfrage
     * @return das extrahierte JWT oder {@code null}, falls kein gültiges Token vorhanden ist
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
