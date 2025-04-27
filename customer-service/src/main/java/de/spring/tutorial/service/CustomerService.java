package de.spring.tutorial.service;

import de.spring.tutorial.exception.CustomerNotFoundException;
import de.spring.tutorial.exception.DuplicateEmailException;
import de.spring.tutorial.exception.DuplicateMobileNumberException;
import de.spring.tutorial.model.Customer;
import de.spring.tutorial.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service-Klasse für die Geschäftslogik der {@link Customer}-Entität.
 * Beinhaltet Methoden zum Erstellen, Lesen, Aktualisieren und Löschen von Kunden.
 * Zusätzlich werden Methoden für das Abrufen von Kunden basierend auf verschiedenen Attributen wie
 * ID, Spitzname, Nachname, E-Mail-Adresse und Telefonnummer bereitgestellt.
 * Diese Klasse stellt auch Methoden für Soft- und Hard-Deletes von Kunden zur Verfügung.
 */
@Slf4j
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private static final String CUSTOMER_NOT_FOUND = " wurde nicht gefunden.";
    private static final String CUSTOMER_ID_PREFIX = "Kunde mit der ID ";
    private static final String CUSTOMER_NICKNAME = "Kunde mit dem Spitznamen ";
    private static final String CUSTOMER_LASTNAME = "Kunde mit dem Nachnamen ";
    private static final String CUSTOMER_NAME = "Kunde mit dem Namen ";
    private static final String CUSTOMER_EMAIL = "Kunde mit der E-Mail ";
    private static final String CUSTOMER_PHONE = "Kunde mit der Telefonnummer ";
    private static final String CUSTOMER_MOBILE = "Kunde mit der Handynummer ";

    /**
     * Konstruktor für den {@link CustomerService} mit Dependency Injection.
     *
     * @param customerRepository Repository für den Zugriff auf Kundendaten
     */
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // ------------------ Standard-Logik --------------------

    /**
     * Ruft alle Kunden aus der Datenbank ab.
     *
     * @return Liste aller Kunden
     */
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    /**
     * Ruft alle Kunden ab, die nicht als gelöscht markiert wurden (Soft-Delete).
     *
     * @return Liste von nicht gelöschten Kunden
     */
    public List<Customer> getAllActiveCustomers() {
        return customerRepository.findByDeletedFalse();
    }

    /**
     * Speichert einen neuen Kunden in der Datenbank.
     * Stellt sicher, dass E-Mail und Handynummer eindeutig sind.
     *
     * @param customer das zu speichernde {@link Customer}-Objekt
     * @return der gespeicherte Kunde
     * @throws DuplicateEmailException wenn ein Kunde mit derselben E-Mail bereits existiert
     * @throws DuplicateMobileNumberException wenn ein Kunde mit derselben Handynummer bereits existiert
     */
    @Transactional
    public Customer saveCustomer(Customer customer) {
        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            log.warn("Ein Kunde mit der E-Mail {} existiert bereits.", customer.getEmail());
            throw new DuplicateEmailException("Ein Kunde mit dieser E-Mail existiert bereits.");
        } else if (customerRepository.findByMobileNumber(customer.getMobileNumber()).isPresent()) {
            log.warn("Ein Kunde mit der Handynummer {} existiert bereits.", customer.getMobileNumber());
            throw new DuplicateMobileNumberException("Ein Kunde mit dieser Handynummer existiert bereits.");
        }
        return customerRepository.save(customer);
    }

    /**
     * Aktualisiert die Daten eines bestehenden Kunden.
     * Nur Felder, die nicht leer sind, werden aktualisiert.
     *
     * @param id die ID des zu aktualisierenden Kunden
     * @param customer das {@link Customer}-Objekt mit neuen Werten
     * @return Optional mit dem aktualisierten Kunden
     * @throws CustomerNotFoundException wenn kein Kunde mit der angegebenen ID existiert
     */
    @Transactional
    public Optional<Customer> updateCustomerById(Long id, Customer customer) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_ID_PREFIX + id + CUSTOMER_NOT_FOUND));

        existingCustomer.setNickName(StringUtils.defaultIfBlank(customer.getNickName(), existingCustomer.getNickName()));
        existingCustomer.setFirstName(StringUtils.defaultIfBlank(customer.getFirstName(), existingCustomer.getFirstName()));
        existingCustomer.setLastName(StringUtils.defaultIfBlank(customer.getLastName(), existingCustomer.getLastName()));
        existingCustomer.setPhoneNumber(StringUtils.defaultIfBlank(customer.getPhoneNumber(), existingCustomer.getPhoneNumber()));
        existingCustomer.setMobileNumber(StringUtils.defaultIfBlank(customer.getMobileNumber(), existingCustomer.getMobileNumber()));
        existingCustomer.setEmail(StringUtils.defaultIfBlank(customer.getEmail(), existingCustomer.getEmail()));

        log.info("Kunde mit der ID {} wurde aktualisiert.", id);
        return Optional.of(customerRepository.save(existingCustomer));
    }

    /**
     * Führt ein Soft-Delete durch, indem das "deleted"-Flag gesetzt wird.
     *
     * @param id Die ID des zu löschenden Kunden.
     * @throws CustomerNotFoundException wenn der Kunde mit der angegebenen ID nicht existiert.
     */
    @Transactional
    public void softDeleteCustomerById(Long id) {
        Optional<Customer> customerOpt = customerRepository.findById(id);
        if (customerOpt.isEmpty()) {
            throw new CustomerNotFoundException(CUSTOMER_ID_PREFIX + id + CUSTOMER_NOT_FOUND);
        }

        Customer customer = customerOpt.get();
        customer.setDeleted(true);  // Soft-Delete durchführen
        customerRepository.save(customer);
        log.info("Kunde mit der ID {} wurde soft gelöscht.", id);
    }

    /**
     * Führt ein Hard-Delete durch, indem der Kunde aus der Datenbank gelöscht wird.
     *
     * @param id Die ID des zu löschenden Kunden.
     * @throws CustomerNotFoundException wenn der Kunde mit der angegebenen ID nicht existiert.
     */
    @Transactional
    public void hardDeleteCustomerById(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException(CUSTOMER_ID_PREFIX + id + CUSTOMER_NOT_FOUND);
        }
        customerRepository.deleteById(id);  // Hard-Delete durchführen
        log.info("Kunde mit der ID {} wurde hard gelöscht.", id);
    }

    // ------------------ Spezifische Logik --------------------

    /**
     * Ruft einen Kunden anhand seiner ID ab.
     *
     * @param id die ID des Kunden
     * @return Optional mit dem gefundenen Kunden
     * @throws CustomerNotFoundException wenn kein Kunde mit der angegebenen ID existiert
     */
    public Optional<Customer> getCustomerById(Long id) {
        return Optional.ofNullable(customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_ID_PREFIX + id + CUSTOMER_NOT_FOUND)));
    }

    /**
     * Ruft einen Kunden mit dem angegebenen Spitznamen ab.
     *
     * @param nickName der Spitzname des Kunden
     * @return Kunde mit übereinstimmendem Spitznamen
     * @throws CustomerNotFoundException wenn kein Kunde mit dem angegebenen Spitznamen existiert
     */
    public Optional<Customer> getCustomerByNickName(String nickName) {
        return Optional.ofNullable(customerRepository.findByNickName(nickName)
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_NICKNAME + nickName + CUSTOMER_NOT_FOUND)));
    }

    /**
     * Ruft alle Kunden mit dem angegebenen Nachnamen ab.
     *
     * @param lastName der Nachname des Kunden
     * @return Liste von Kunden mit übereinstimmendem Nachnamen
     * @throws CustomerNotFoundException wenn keine Kunden mit dem angegebenen Nachnamen existieren
     */
    public List<Customer> getCustomersByLastName(String lastName) {
        List<Customer> customers = customerRepository.findByLastName(lastName);
        if (customers.isEmpty()) {
            throw new CustomerNotFoundException(CUSTOMER_LASTNAME + lastName + CUSTOMER_NOT_FOUND);
        }
        return customers;
    }

    /**
     * Sucht Kunden anhand eines Suchbegriffs im Vor- oder Nachnamen (case-insensitive).
     *
     * @param searchQuery der Suchbegriff
     * @return Liste von passenden Kunden
     * @throws CustomerNotFoundException wenn keine Kunden gefunden werden
     */
    public List<Customer> searchCustomersByName(String searchQuery) {
        List<Customer> customers = customerRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(searchQuery, searchQuery);
        if (customers.isEmpty()) {
            throw new CustomerNotFoundException(CUSTOMER_NAME + searchQuery + CUSTOMER_NOT_FOUND);
        }
        return customers;
    }

    /**
     * Ruft einen Kunden anhand seiner E-Mail-Adresse ab.
     *
     * @param email die E-Mail-Adresse des Kunden
     * @return Optional mit dem gefundenen Kunden
     * @throws CustomerNotFoundException wenn kein Kunde mit der angegebenen E-Mail existiert
     */
    public Optional<Customer> getCustomerByEmail(String email) {
        return Optional.ofNullable(customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_EMAIL + email + CUSTOMER_NOT_FOUND)));
    }

    /**
     * Ruft alle Kunden mit der angegebenen Telefonnummer ab.
     *
     * @param phoneNumber die Telefonnummer des Kunden
     * @return Liste von Kunden mit übereinstimmender Telefonnummer
     * @throws CustomerNotFoundException wenn keine Kunden mit der angegebenen Telefonnummer existieren
     */
    public List<Customer> getCustomersByPhoneNumber(String phoneNumber) {
        List<Customer> customers = customerRepository.findByPhoneNumber(phoneNumber);
        if (customers.isEmpty()) {
            throw new CustomerNotFoundException(CUSTOMER_PHONE + phoneNumber + CUSTOMER_NOT_FOUND);
        }
        return customers;
    }

    /**
     * Ruft einen Kunden anhand seiner Handynummer ab.
     *
     * @param mobileNumber die Handynummer des Kunden
     * @return Optional mit dem gefundenen Kunden
     * @throws CustomerNotFoundException wenn kein Kunde mit der angegebenen Handynummer existiert
     */
    public Optional<Customer> getCustomerByMobileNumber(String mobileNumber) {
        return Optional.ofNullable(customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new CustomerNotFoundException(CUSTOMER_MOBILE + mobileNumber + CUSTOMER_NOT_FOUND)));
    }

}