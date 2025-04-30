package de.spring.tutorial.security;

import de.spring.tutorial.model.User;
import de.spring.tutorial.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Service zur Bereitstellung von Benutzerinformationen für die Authentifizierung.
 * Implementiert das `UserDetailsService`-Interface von Spring Security, um Benutzer basierend
 * auf der E-Mail-Adresse zu laden und diese für die Authentifizierung bereitzustellen.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Konstruktor zur Initialisierung des `UserRepository`.
     *
     * @param userRepository Das Repository, das für den Zugriff auf Benutzer-Daten zuständig ist.
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Lädt die Benutzerinformationen anhand des Benutzernamens (E-Mail).
     * Diese Methode wird von Spring Security verwendet, um die Benutzerinformationen zu laden.
     *
     * @param username Die E-Mail-Adresse des Benutzers.
     * @return Ein `UserDetails`-Objekt, das die Benutzerdaten enthält.
     * @throws UsernameNotFoundException Wenn der Benutzer mit der angegebenen E-Mail nicht gefunden wird.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Benutzer anhand der E-Mail-Adresse suchen
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Dieser Benutzer existiert nicht im System."));

        // Rückgabe eines Spring Security UserDetails-Objekts
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList()  // Keine weiteren Rollen oder Berechtigungen für diesen Benutzer
        );
    }
}


