package de.spring.tutorial.config;

import de.spring.tutorial.security.JwtAuthenticationEntryPoint;
import de.spring.tutorial.security.JwtAuthenticationFilter;
import de.spring.tutorial.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Sicherheitskonfiguration für die Anwendung.
 * Diese Klasse konfiguriert die HTTP-Sicherheitsregeln und fügt den JWT-Filter für die Authentifizierung hinzu.
 */
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Konstruktor zur Initialisierung der Sicherheitskonfiguration.
     *
     * @param jwtAuthenticationEntryPoint der EntryPoint für die JWT-Authentifizierung
     * @param jwtTokenProvider der Provider für die JWT-Token-Validierung
     */
    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtTokenProvider jwtTokenProvider) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Konfiguriert die Sicherheitsfilterkette.
     * Der JWT-Filter wird hinzugefügt, um Anfragen mit einem gültigen Token zu authentifizieren.
     *
     * @param http das HttpSecurity-Objekt für die Sicherheitskonfiguration
     * @return die konfigurierte SecurityFilterChain
     * @throws Exception bei Fehlern während der Konfiguration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenProvider);

        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/auth/**").permitAll()
                                .anyRequest().authenticated()
                );

        return http.build();
    }
}
