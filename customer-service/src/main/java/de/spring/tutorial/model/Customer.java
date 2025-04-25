package de.spring.tutorial.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    @NotNull(message = "Der Nachname muss angegeben werden!")
    private String lastName;

    @NotNull(message = "Der Vorname muss angegeben werden!")
    private String firstName;

    @NotNull(message = "Die Telefonnummer muss angegeben werden!")
    private String phoneNumber;

    @NotNull(message = "Die Telefonnummer muss angegeben werden!")
    @Column(unique = true)
    private String mobileNumber;

    @NotNull(message = "E-Mail-Adresse muss angegeben werden!")
    @Email(message = "Bitte eine gültige E-Mail-Adresse angeben!")
    @Column(unique = true)
    private String email;

}
