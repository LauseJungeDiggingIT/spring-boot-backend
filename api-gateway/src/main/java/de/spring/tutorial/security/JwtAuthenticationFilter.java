package de.spring.tutorial.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import reactor.core.publisher.Mono;

/**
 * Filter zur Überprüfung des JWT-Authentifizierungstokens im Gateway.
 * Dieser Filter prüft, ob im Header der Anfrage ein gültiges JWT vorhanden ist.
 * Das Token wird extrahiert und, falls vorhanden, überprüft, ob es im richtigen Format (Bearer <Token>) vorliegt.
 * Wenn das Token fehlt oder ungültig ist, wird eine Fehlermeldung mit Statuscode 401 zurückgegeben.
 * Ansonsten wird die Anfrage an den nächsten Filter in der Kette weitergeleitet.
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<Object> {

    public JwtAuthenticationFilter() {
        super(Object.class);
    }

    /**
     * Wendet den Filter an, um das Vorhandensein und das Format des JWT im Authorization-Header zu überprüfen.
     *
     * @param config Die Filterkonfiguration, die nicht benötigt wird, aber erforderlich ist, da es sich um eine erweiterbare Klasse handelt.
     * @return Ein {@link GatewayFilter}, der die Anfrage validiert und ggf. weiterleitet oder mit einem Fehler beantwortet.
     */
    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            String token = extractToken(exchange);

            if (token == null) {
                log.warn("Fehlendes oder ungültiges Token in der Anfrage");
                return onError(exchange);
            }

            log.info("Token im richtigen Format vorhanden. Weiterleitung an Service.");

            return chain.filter(exchange);
        };
    }

    /**
     * Extrahiert das JWT-Token aus dem Authorization-Header der Anfrage.
     *
     * @param exchange Die Web-Exchange-Instanz, die Informationen über die Anfrage enthält.
     * @return Das extrahierte JWT-Token oder {@code null}, wenn der Header nicht im richtigen Format vorliegt.
     */
    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    /**
     * Verarbeitet Fehler, indem der HTTP-Status gesetzt und die Antwort beendet wird.
     *
     * @param exchange Die Web-Exchange-Instanz.
     * @return Ein {@link Mono}, das den Abschluss der Antwort signalisiert.
     */
    private Mono<Void> onError(ServerWebExchange exchange) {
        log.error("Security-Fehler: {}", "Fehlendes oder ungültiges Token");
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
