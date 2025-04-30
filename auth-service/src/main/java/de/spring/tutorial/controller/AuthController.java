package de.spring.tutorial.controller;

import de.spring.tutorial.exception.UserAlreadyExistsException;
import de.spring.tutorial.model.User;
import de.spring.tutorial.repository.UserRepository;
import de.spring.tutorial.request.AuthRequest;
import de.spring.tutorial.response.AuthResponse;
import de.spring.tutorial.security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * REST-Controller für Authentifizierungsprozesse wie Registrierung und Login.
 * <p>
 * Dieser Controller bietet Endpunkte zur Benutzerregistrierung und -anmeldung.
 * Nach erfolgreicher Anmeldung wird ein JWT-Token generiert und an den Client zurückgegeben.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Registriert einen neuen Benutzer anhand der angegebenen AuthRequest-Daten.
     * <p>
     * Bei erfolgreicher Registrierung wird eine AuthResponse mit ID und E-Mail des Benutzers zurückgegeben.
     *
     * @param authRequest Die Anmeldedaten des neuen Benutzers (E-Mail und Passwort), validiert per Bean Validation.
     * @return ResponseEntity mit AuthResponse, enthält Benutzer-ID und E-Mail.
     * @throws UserAlreadyExistsException wenn die E-Mail bereits vergeben ist.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRequest authRequest) {
        if (userRepository.findByEmail(authRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Ein Benutzer mit dieser E-Mail existiert bereits.");
        }

        User user = User.builder()
                .email(authRequest.getEmail())
                .password(passwordEncoder.encode(authRequest.getPassword()))
                .build();

        User created = userRepository.save(user);

        return ResponseEntity.ok(new AuthResponse(created.getId(), created.getEmail()));
    }

    /**
     * Authentifiziert einen Benutzer anhand der übermittelten Anmeldedaten.
     * <p>
     * Bei erfolgreicher Authentifizierung wird ein JWT-Token zurückgegeben.
     *
     * @param authRequest Die Anmeldedaten des Benutzers (E-Mail und Passwort), validiert per Bean Validation.
     * @return ResponseEntity mit dem JWT-Token als String.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        String token = jwtTokenProvider.generateToken(authRequest.getEmail());
        return ResponseEntity.ok(token);
    }
}
