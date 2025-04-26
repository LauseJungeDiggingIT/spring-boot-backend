package de.spring.tutorial.repository;

import de.spring.tutorial.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Repository-Interface für die Customer-Entität.
 * Erweitert JpaRepository und bietet die Standard-CRUD-Methoden.
 * Zusätzlich sind benutzerdefinierte Abfrage-Methoden für das Finden von Kunden basierend auf E-Mail und Telefonnummer definiert.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Findet alle Kunden anhand des Nachnamens.
     *
     * @param lastName Der Nachname des Kunden.
     * @return Liste von Kunden, die den angegebenen Nachnamen haben.
     */
    List<Customer> findByLastName(String lastName);

    List<Customer> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);


    /**
     * Findet einen Kunden anhand der E-Mail-Adresse.
     *
     * @param email Die E-Mail-Adresse des Kunden.
     * @return Optional<Customer>, falls ein Kunde mit der angegebenen E-Mail gefunden wird.
     */
    Optional<Customer> findByEmail(String email);

    /**
     * Findet alle Kunden anhand der Telefonnummer.
     *
     * @param phoneNumber Die Telefonnummer des Kunden.
     * @return Liste von Kunden, die die angegebene Telefonnummer haben.
     */
    List<Customer> findByPhoneNumber(String phoneNumber);

    /**
     * Findet einen Kunden anhand der Handynummer.
     *
     * @param mobileNumber Die Telefonnummer des Kunden.
     * @return Optional<Customer>, falls ein Kunde mit der angegebenen Handynummer gefunden wird.
     */
    Optional<Customer> findByMobileNumber(String mobileNumber);
}