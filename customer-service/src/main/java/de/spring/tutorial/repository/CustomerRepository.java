package de.spring.tutorial.repository;

import de.spring.tutorial.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository-Interface für die {@link Customer}-Entität.
 * Erweitert {@link JpaRepository} und bietet Standard-CRUD-Operationen sowie benutzerdefinierte Abfragen.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Findet alle Kunden mit dem angegebenen Nachnamen.
     *
     * @param lastName der Nachname des Kunden
     * @return Liste der Kunden mit übereinstimmendem Nachnamen
     */
    List<Customer> findByLastName(String lastName);

    /**
     * Findet alle Kunden mit dem angegebenen Spitznamen.
     *
     * @param nickName der Spitzname des Kunden
     * @return Liste der Kunden mit übereinstimmendem Spitznamen
     */
    List<Customer> findByNickName(String nickName);

    /**
     * Findet alle Kunden, deren Vor- oder Nachname (case-insensitive) den angegebenen Suchbegriff enthält.
     *
     * @param firstName der Suchbegriff für den Vornamen
     * @param lastName  der Suchbegriff für den Nachnamen
     * @return Liste der passenden Kunden
     */
    List<Customer> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);

    /**
     * Findet einen Kunden anhand seiner E-Mail-Adresse.
     *
     * @param email die E-Mail-Adresse des Kunden
     * @return {@link Optional} mit dem gefundenen Kunden oder leer, falls nicht vorhanden
     */
    Optional<Customer> findByEmail(String email);

    /**
     * Findet alle Kunden mit der angegebenen Telefonnummer.
     *
     * @param phoneNumber die Telefonnummer des Kunden
     * @return Liste der Kunden mit übereinstimmender Telefonnummer
     */
    List<Customer> findByPhoneNumber(String phoneNumber);

    /**
     * Findet einen Kunden anhand seiner Handynummer.
     *
     * @param mobileNumber die Handynummer des Kunden
     * @return {@link Optional} mit dem gefundenen Kunden oder leer, falls nicht vorhanden
     */
    Optional<Customer> findByMobileNumber(String mobileNumber);

    /**
     * Findet alle Kunden, die nicht als gelöscht markiert wurden (Soft-Delete-Logik).
     *
     * @return Liste der aktiven Kunden (deleted = false)
     */
    List<Customer> findByDeletedFalse();
}
