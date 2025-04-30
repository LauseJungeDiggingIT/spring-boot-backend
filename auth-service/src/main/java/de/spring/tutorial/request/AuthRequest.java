package de.spring.tutorial.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Request-Objekt für die Authentifizierungs- und Registrierungsanforderungen.
 * Dieses Objekt wird verwendet, um die Eingaben des Benutzers zu empfangen,
 * wie die E-Mail-Adresse und das Passwort, die für den Login oder die Registrierung benötigt werden.
 *
 * @author Dein Name
 */
@Data
public class AuthRequest {

    /**
     * Die E-Mail-Adresse des Benutzers, die für die Authentifizierung verwendet wird.
     * Muss im richtigen Format vorliegen und wird auf Duplikate überprüft.

     * "@NotNull" Stellt sicher, dass eine E-Mail-Adresse angegeben wird.
     * "@Email" Stellt sicher, dass die E-Mail-Adresse im gültigen Format ist.
     */
    @NotNull(message = "E-Mail-Adresse ist erforderlich")
    @Email(message = "Ungültige E-Mail-Adresse")
    @Column(unique = true)
    private String email;

    /**
     * Das Passwort des Benutzers.
     * Muss den festgelegten Sicherheitsanforderungen entsprechen, einschließlich
     * einer Mindestlänge und der Kombination von Großbuchstaben, Kleinbuchstaben,
     * Zahlen und Sonderzeichen.

     * "@NotNull" Stellt sicher, dass ein Passwort angegeben wird.
     * "@Size" Stellt sicher, dass das Passwort mindestens 8 Zeichen lang ist.
     * "@Pattern" Stellt sicher, dass das Passwort bestimmte Sicherheitsanforderungen erfüllt.
     */
    @NotNull(message = "Passwort ist erforderlich")
    @Size(min = 8, message = "Passwort muss mindestens 8 Zeichen lang sein")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[#?!@$%^&*\\-]).{8,}$",
            message = "Passwort muss Groß-/Kleinbuchstaben, Zahl und Sonderzeichen enthalten"
    )
    private String password;
}
