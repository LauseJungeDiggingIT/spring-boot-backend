package de.spring.tutorial.config;

import de.spring.tutorial.security.JwtAuthenticationEntryPoint;
import de.spring.tutorial.security.JwtAuthenticationFilter;
import de.spring.tutorial.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Konfiguriert die Sicherheitseinstellungen für den Customer-Service.
 * Diese Klasse enthält die Konfiguration für Authentifizierung, Autorisierung und das Handling von Sicherheitsfehlern.
 * Der JWT-Filter wird hier zur Tokenvalidierung eingesetzt.
 */
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Konstruktor zur Injektion der benötigten Beans.
     *
     * @param jwtAuthenticationEntryPoint der JWT-Authentifizierungseintrag
     * @param jwtTokenProvider der JWT-Token-Provider
     */
    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtTokenProvider jwtTokenProvider) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Bean für den AuthenticationManager, der für die Authentifizierung zuständig ist.
     *
     * @param authConfig die Authentifizierungskonfiguration
     * @return der AuthenticationManager
     * @throws Exception wenn ein Fehler bei der Erstellung des AuthenticationManager auftritt
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Bean für die Sicherheitsfilterkette, die den HTTP-Sicherheitskonfigurationsfluss festlegt.
     *
     * @param http das HttpSecurity-Objekt, das für die Konfiguration von HTTP-Sicherheit verantwortlich ist
     * @return die konfigurierte Sicherheitsfilterkette
     * @throws Exception wenn ein Fehler bei der Konfiguration auftritt
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Den JWT-Filter zur Tokenvalidierung einfügen
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
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Token-Validierung
                // URL-Zugriffsrechte konfigurieren
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/auth/**").permitAll()
                                .anyRequest().authenticated()
                );

        return http.build();
    }
}
