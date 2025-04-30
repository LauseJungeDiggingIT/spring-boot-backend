package de.spring.tutorial.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Entität, die einen Benutzer im System repräsentiert.
 * Diese Entität wird für die Speicherung und Verwaltung von Benutzerdaten verwendet.
 * Sie enthält Felder für die E-Mail-Adresse und das Passwort des Benutzers.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {

    /**
     * Primärschlüssel für die User-Entität.
     * Automatisch generierte ID mit der Strategie IDENTITY.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * E-Mail-Adresse des Benutzers.
     * Die E-Mail-Adresse muss gültig und eindeutig sein.

     * "@NotNull" stellt sicher, dass eine E-Mail-Adresse angegeben wird.
     * "@Email" stellt sicher, dass die E-Mail-Adresse im gültigen Format ist.
     */
    @NotNull(message = "E-Mail-Adresse ist erforderlich")
    @Email(message = "Ungültige E-Mail-Adresse")
    @Column(unique = true)
    private String email;

    /**
     * Passwort des Benutzers.
     * Das Passwort muss mindestens 8 Zeichen lang sein und bestimmte Sicherheitsanforderungen erfüllen:
     * - mindestens einen Großbuchstaben
     * - mindestens einen Kleinbuchstaben
     * - mindestens eine Zahl
     * - mindestens ein Sonderzeichen

     * "@NotNull" stellt sicher, dass ein Passwort angegeben wird.
     * "@Size" stellt sicher, dass das Passwort mindestens 8 Zeichen lang ist.
     * "@Pattern" stellt sicher, dass das Passwort den oben genannten Sicherheitsanforderungen entspricht.
     */
    @NotNull(message = "Passwort ist erforderlich")
    @Size(min = 8, message = "Passwort muss mindestens 8 Zeichen lang sein")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[#?!@$%^&*\\-]).{8,}$",
            message = "Passwort muss Groß-/Kleinbuchstaben, Zahl und Sonderzeichen enthalten"
    )
    private String password;

}
