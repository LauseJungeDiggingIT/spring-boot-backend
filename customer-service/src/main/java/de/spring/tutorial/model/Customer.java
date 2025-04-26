package de.spring.tutorial.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Repräsentiert einen Kunden (Customer) innerhalb der Anwendung.
 * Diese Entität wird mit einer Datenbanktabelle verknüpft und enthält
 * personenbezogene Daten wie Namen, Telefonnummern und E-Mail-Adresse.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Customer {

    /**
     * Eindeutige ID des Kunden.
     * Wird automatisch von der Datenbank generiert.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Optionaler Spitzname des Kunden.
     */
    private String nickName;

    /**
     * Nachname des Kunden. Darf nicht Null sein.
     */
    @NotNull(message = "Der Nachname muss angegeben werden!")
    private String lastName;

    /**
     * Vorname des Kunden. Darf nicht Null sein.
     */
    @NotNull(message = "Der Vorname muss angegeben werden!")
    private String firstName;

    /**
     * Telefonnummer des Kunden (z.B. Festnetz). Darf nicht Null sein.
     */
    @NotNull(message = "Die Telefonnummer muss angegeben werden!")
    private String phoneNumber;

    /**
     * Mobile Telefonnummer des Kunden. Muss eindeutig und darf nicht null sein.
     */
    @NotNull(message = "Die Handynummer muss angegeben werden!")
    @Column(unique = true)
    private String mobileNumber;

    /**
     * E-Mail-Adresse des Kunden. Muss eindeutig, gültig und nicht null sein.
     */
    @NotNull(message = "E-Mail-Adresse muss angegeben werden!")
    @Email(message = "Bitte eine gültige E-Mail-Adresse angeben!")
    @Column(unique = true)
    private String email;
}
