package de.spring.tutorial.repository;

import de.spring.tutorial.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository für die Verwaltung von Benutzer-Daten.
 * Diese Schnittstelle ermöglicht den Zugriff auf die `User`-Entität in der Datenbank
 * und stellt Methoden für gängige Datenbankoperationen zur Verfügung.
 * Sie erbt von `JpaRepository`, um grundlegende CRUD-Operationen zu unterstützen,
 * und enthält benutzerdefinierte Methoden für spezielle Abfragen.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Findet einen Benutzer anhand seiner E-Mail-Adresse.
     * Diese Methode wird verwendet, um zu prüfen, ob ein Benutzer mit einer bestimmten E-Mail-Adresse existiert,
     * z.B. bei der Registrierung oder beim Login.
     *
     * @param email Die E-Mail-Adresse des Benutzers.
     * @return Ein `Optional<User>`, das den gefundenen Benutzer enthält, oder leer, wenn kein Benutzer mit der angegebenen E-Mail existiert.
     */
    Optional<User> findByEmail(String email);
}
