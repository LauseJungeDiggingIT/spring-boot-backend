package de.spring.tutorial.controller;
import de.spring.tutorial.model.Customer;
import de.spring.tutorial.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-Controller für die Customer-Entität.
 * Stellt API-Endpunkte für CRUD-Operationen bereit.
 */
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Konstruktor für CustomerController mit Dependency-Injection.
     * @param customerService Stellt die Geschäftslogik für Kundenoperationen bereit.
     */
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // <----------------Standard-Requests ---------------->

    /**
     * API-Endpunkt zum Abrufen aller Kunden.
     * @return ResponseEntity mit der Liste aller Kunden.
     */
    @GetMapping
    public ResponseEntity<List<Customer>> getCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    /**
     * API-Endpunkt zum Erstellen eines neuen Kunden.
     * @param customer JSON-Body mit Kundendaten.
     * @return ResponseEntity mit dem erstellten Kunden.
     */
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer createdCustomer = customerService.saveCustomer(customer);
        return ResponseEntity.ok(createdCustomer);
    }

    /**
     * API-Endpunkt zum Löschen eines Kunden anhand der ID.
     * @param id Die ID des zu löschenden Kunden.
     * @return ResponseEntity ohne Inhalt.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable Long id) {
        customerService.deleteCustomerById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * API-Endpunkt zum Aktualisieren der Kundendaten anhand der ID.
     * @param id Die ID des Kunden.
     * @param customer JSON-Body mit den neuen Kundendaten.
     * @return ResponseEntity oder 404 Not Found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomerDataById(@PathVariable Long id,
                                                           @RequestBody Customer customer) {
        Customer updatedCustomer = customerService.updateCustomerById(id, customer)
                .orElseThrow(() -> new RuntimeException("Kundenaktualisierung fehlgeschlagen"));
        return ResponseEntity.ok(updatedCustomer);
    }

    // <----------------Spezifische-Requests ---------------->

    /**
     * API-Endpunkt zum Abrufen eines Kunden anhand der ID.
     * @param id Die ID des Kunden.
     * @return ResponseEntity mit dem Kunden.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id)
                .orElseThrow(() -> new RuntimeException("Kunde nicht gefunden"));
        return ResponseEntity.ok(customer);
    }

    /**
     * API-Endpunkt zum Abrufen von Kunden anhand des Nachnamens.
     * @param nickName Der Spitznamen des Kunden.
     * @return ResponseEntity mit der Liste der Kunden oder 404 Not Found.
     */
    @GetMapping("/nickName/{nickName}")
    public ResponseEntity<List<Customer>> getCustomersByNickName(@PathVariable String nickName) {
        List<Customer> customers = customerService.getCustomersByNickName(nickName);
        return customers.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(customers);
    }

    /**
     * API-Endpunkt zum Abrufen von Kunden anhand des Nachnamens.
     * @param lastName Der Nachnamen des Kunden.
     * @return ResponseEntity mit der Liste der Kunden oder 404 Not Found.
     */
    @GetMapping("/lastName/{lastName}")
    public ResponseEntity<List<Customer>> getCustomersByLastName(@PathVariable String lastName) {
        List<Customer> customers = customerService.getCustomersByLastName(lastName);
        return customers.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(customers);
    }

    @GetMapping("/search/{searchQuery}")
    public ResponseEntity<List<Customer>> searchCustomersByName(@PathVariable String searchQuery) {
        List<Customer> customers = customerService.searchCustomersByName(searchQuery);
        return customers.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(customers);
    }

    /**
     * API-Endpunkt zum Abrufen eines Kunden anhand der E-Mail.
     * @param email Die E-Mail des Kunden.
     * @return ResponseEntity mit dem Kunden.
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Customer> getCustomerByEmail(@PathVariable String email) {
        Customer customer = customerService.getCustomerByEmail(email)
                .orElseThrow(() -> new RuntimeException("Keinen Kunden mit dieser E-Mail gefunden"));
        return ResponseEntity.ok(customer);
    }

    /**
     * API-Endpunkt zum Abrufen von Kunden anhand der Telefonnummer.
     * @param phoneNumber Die Telefonnummer des Kunden.
     * @return ResponseEntity mit der Liste der Kunden oder 404 Not Found.
     */
    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<List<Customer>> getCustomersByPhoneNumber(@PathVariable String phoneNumber) {
        List<Customer> customers = customerService.getCustomersByPhoneNumber(phoneNumber);
        return customers.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(customers);
    }

    /**
     * API-Endpunkt zum Abrufen von Kunden anhand der Telefonnummer.
     * @param mobileNumber Die Handynummer des Kunden.
     * @return ResponseEntity mit dem Kunden oder 404 Not Found.
     */
    @GetMapping("/mobile/{mobileNumber}")
    public ResponseEntity<Customer> getCustomersByMobileNumber(@PathVariable String mobileNumber) {
        Customer customer = customerService.getCustomerByMobileNumber(mobileNumber)
                .orElseThrow(() -> new RuntimeException("Keinen Kunden mit dieser Handynummer gefunden"));
        return ResponseEntity.ok(customer);
    }

}

