package de.spring.tutorial.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Repräsentiert einen Kunden innerhalb der Anwendung.
 * Diese Entität wird einer Datenbanktabelle zugeordnet und enthält
 * personenbezogene Daten wie Name, Telefonnummern und E-Mail-Adresse.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Customer {

    /**
     * Primärschlüssel: Eindeutige ID des Kunden.
     * Wird automatisch von der Datenbank generiert.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Spitzname des Kunden.
     * Darf nicht {@code null} sein. muss unique sein
     */
    @NotNull(message = "Der Spitzname muss angegeben werden!")
    @Column(unique = true)
    private String nickName;

    /**
     * Nachname des Kunden.
     * Darf nicht {@code null} sein.
     */
    @NotNull(message = "Der Nachname muss angegeben werden!")
    private String lastName;

    /**
     * Vorname des Kunden.
     * Darf nicht {@code null} sein.
     */
    @NotNull(message = "Der Vorname muss angegeben werden!")
    private String firstName;

    /**
     * Telefonnummer des Kunden (z.B. Festnetznummer).
     * Darf nicht {@code null} sein.
     */
    @NotNull(message = "Die Telefonnummer muss angegeben werden!")
    private String phoneNumber;

    /**
     * Handynummer des Kunden.
     * Muss eindeutig und darf nicht {@code null} sein.
     */
    @NotNull(message = "Die Handynummer muss angegeben werden!")
    @Column(unique = true)
    private String mobileNumber;

    /**
     * E-Mail-Adresse des Kunden.
     * Muss eindeutig, gültig und darf nicht {@code null} sein.
     */
    @NotNull(message = "E-Mail-Adresse muss angegeben werden!")
    @Email(message = "Bitte eine gültige E-Mail-Adresse angeben!")
    @Column(unique = true)
    private String email;

    /**
     * Gibt an, ob der Kunde als gelöscht markiert wurde (Soft-Delete).
     * Standardmäßig {@code false}.
     */
    private boolean deleted = false;
}
