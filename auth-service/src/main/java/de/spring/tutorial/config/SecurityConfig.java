package de.spring.tutorial.config;

import de.spring.tutorial.security.CustomUserDetailsService;
import de.spring.tutorial.security.JwtAuthenticationEntryPoint;
import de.spring.tutorial.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Konfiguriert die Sicherheitseinstellungen für die Anwendung.
 * Diese Klasse enthält die Konfiguration für Authentifizierung, Autorisierung und das Handling von Sicherheitsfehlern.
 * Sie richtet den JWT-Filter ein und stellt sicher, dass keine Sitzungsinformationen gespeichert werden (stateless).
 */
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    /**
     * Konstruktor zur Injektion der benötigten Beans.
     *
     * @param jwtAuthenticationEntryPoint der JWT-Authentifizierungseintrag
     * @param jwtTokenProvider der JWT-Token-Provider
     * @param customUserDetailsService der benutzerdefinierte User-Details-Service
     */
    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                          JwtTokenProvider jwtTokenProvider,
                          CustomUserDetailsService customUserDetailsService) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    /**
     * Bean für den Passwort-Encoder, der zur Hashing von Passwörtern verwendet wird.
     *
     * @return ein BCryptPasswordEncoder, der zur sicheren Passwortverschlüsselung verwendet wird
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/auth/**").permitAll()
                                .anyRequest().denyAll() // Keine anderen Routen erlaubt
                );

        return http.build();
    }

}
