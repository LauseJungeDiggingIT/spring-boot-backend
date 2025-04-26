package de.spring.tutorial.service;

import de.spring.tutorial.model.Customer;
import de.spring.tutorial.exception.CustomerNotFoundException;
import de.spring.tutorial.exception.DuplicateEmailException;
import de.spring.tutorial.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service-Klasse für die Geschäftslogik der Customer-Entität.
 * Enthält Methoden zum Abrufen, Speichern, Aktualisieren und Löschen von Kunden.
 */
@Slf4j
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private static final String CUSTOMER_NOT_FOUND = " nicht gefunden.";
    private static final String CUSTOMER_ID_PREFIX = "Kunde mit der ID ";

    /**
     * Konstruktor für CustomerService mit Dependency-Injection.
     *
     * @param customerRepository Repository zum Zugriff auf die Kundendaten.
     */
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // ------------------ Standard Logik --------------------

    /**
     * Holt alle Kunden aus der Datenbank.
     *
     * @return Liste aller Kunden.
     */
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    /**
     * Speichert einen neuen Kunden in der Datenbank.
     * Überprüft, ob bereits ein Kunde mit der angegebenen E-Mail existiert.
     *
     * @param customer Das zu speichernde Customer-Objekt.
     * @return Der gespeicherte Kunde.
     * @throws DuplicateEmailException wenn ein Kunde mit derselben E-Mail bereits existiert.
     */
    @Transactional
    public Customer saveCustomer(Customer customer) {
        if (customerRepository.findByEmail(customer.getEmail()).isPresent())  {
            log.warn("Ein Kunde mit der E-Mail {} existiert bereits.", customer.getEmail());
            throw new DuplicateEmailException("Ein Kunde mit dieser E-Mail existiert bereits.");
        }
        return customerRepository.save(customer);
    }

    /**
     * Löscht einen Kunden anhand der ID.
     *
     * @param id Die ID des zu löschenden Kunden.
     * @throws CustomerNotFoundException wenn der Kunde mit der angegebenen ID nicht existiert.
     */
    @Transactional
    public void deleteCustomerById(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException(CUSTOMER_ID_PREFIX + id + CUSTOMER_NOT_FOUND);
        }
        customerRepository.deleteById(id);
        log.info("Kunde mit der ID {} wurde gelöscht.", id);
    }

    /**
     * Aktualisiert die Daten eines bestehenden Kunden anhand der ID.
     *
     * @param id       Die ID des zu aktualisierenden Kunden.
     * @param customer Customer-Objekt mit den neuen Daten.
     * @return Optional<Customer> mit dem aktualisierten Kunden, falls vorhanden.
     * @throws CustomerNotFoundException wenn kein Kunde mit der angegebenen ID existiert.
     */
    @Transactional
    public Optional<Customer> updateCustomerById(Long id, Customer customer) {
        Optional<Customer> existing = customerRepository.findById(id);
        if (existing.isEmpty()) {
            throw new CustomerNotFoundException(CUSTOMER_ID_PREFIX + id + CUSTOMER_NOT_FOUND);
        }
        Customer existingCustomer = existing.get();
        existingCustomer.setFirstName(StringUtils.isNotBlank(customer.getFirstName()) ? customer.getFirstName() : existingCustomer.getFirstName());
        existingCustomer.setLastName(StringUtils.isNotBlank(customer.getLastName()) ? customer.getLastName() : existingCustomer.getLastName());
        existingCustomer.setPhoneNumber(StringUtils.isNotBlank(customer.getPhoneNumber()) ? customer.getPhoneNumber() : existingCustomer.getPhoneNumber());
        existingCustomer.setEmail(StringUtils.isNotBlank(customer.getEmail()) ? customer.getEmail() : existingCustomer.getEmail());

        log.info("Kunde mit der ID {} wurde aktualisiert.", id);
        return Optional.of(customerRepository.save(existingCustomer));
    }

    // ------------------ Spezifische Logik --------------------

    /**
     * Holt einen Kunden anhand der ID.
     *
     * @param id Die ID des Kunden.
     * @return Optional<Customer> des gefundenen Kunden.
     * @throws CustomerNotFoundException wenn kein Kunde mit der angegebenen ID existiert.
     */
    public Optional<Customer> getCustomerById(Long id) {
        return Optional.ofNullable(customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_ID_PREFIX + id + CUSTOMER_NOT_FOUND)));
    }

    /**
     * Holt alle Kunden anhand des Nachnamens.
     *
     * @param lastName Der Nachname des Kunden.
     * @return Liste von Kunden mit dem angegebenen Nachnamen.
     */
    public List<Customer> getCustomersByLastName(String lastName) {
        return customerRepository.findByLastName(lastName);
    }

    /**
     * Holt alle Kunden die den Suchkriterien entsprechen
     * @param searchQuery Querries in die Suche einbezogen werden (hier Nach- und Vorname)
     * @return Liste von Suchtreffern
     */
    public List<Customer> searchCustomersByName(String searchQuery) {
        return customerRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(searchQuery, searchQuery);
    }

    /**
     * Holt einen Kunden anhand der E-Mail.
     *
     * @param email Die E-Mail des Kunden.
     * @return Optional<Customer> des gefundenen Kunden.
     */
    public Optional<Customer> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    /**
     * Holt alle Kunden anhand der Telefonnummer.
     *
     * @param phoneNumber Die Telefonnummer des Kunden.
     * @return Liste von Kunden mit der angegebenen Telefonnummer.
     */
    public List<Customer> getCustomersByPhoneNumber(String phoneNumber) {
        return customerRepository.findByPhoneNumber(phoneNumber);
    }
    /**
     * Holt einen Kunden anhand der Handynummer.
     *
     * @param mobileNumber Die Handynummer des Kunden.
     * @return Liste von Kunden mit der angegebenen Handynummer.
     */
    public Optional<Customer> getCustomerByMobileNumber(String mobileNumber) {
        return customerRepository.findByMobileNumber(mobileNumber);
    }
}
