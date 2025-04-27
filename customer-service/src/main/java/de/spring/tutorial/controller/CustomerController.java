package de.spring.tutorial.controller;

import de.spring.tutorial.model.Customer;
import de.spring.tutorial.service.CustomerService;
import de.spring.tutorial.exception.CustomerNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * REST-Controller für die {@link Customer}-Entität.
 * Stellt API-Endpunkte für CRUD-Operationen sowie spezifische Suchfunktionen bereit.
 */
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Konstruktor für {@link CustomerController} mit Dependency Injection.
     *
     * @param customerService Service zur Bereitstellung der Geschäftslogik für Kundenoperationen
     */
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // <---------------- Standard-Requests ---------------->

    /**
     * Ruft alle Kunden ab, unabhängig von ihrem Status.
     *
     * @return ResponseEntity mit der Liste aller Kunden.
     */
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    /**
     * Ruft alle aktiven Kunden ab (nicht gelöscht).
     *
     * @return ResponseEntity mit der Liste der aktiven Kunden.
     */
    @GetMapping("/active")
    public ResponseEntity<List<Customer>> getActiveCustomers() {
        List<Customer> customers = customerService.getAllActiveCustomers();
        return customers.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(customers);
    }

    /**
     * Aktualisiert die Informationen eines bestehenden Kunden.
     *
     * @param id       Die ID des Kunden, der aktualisiert werden soll.
     * @param customer Das Kundenobjekt mit den aktualisierten Informationen.
     * @return ResponseEntity mit dem aktualisierten Kunden.
     * @throws CustomerNotFoundException Wenn der Kunde nicht gefunden wurde oder die Aktualisierung fehlgeschlagen ist.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id,
                                                   @RequestBody Customer customer) {
        return customerService.updateCustomerById(id, customer)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new CustomerNotFoundException("Kunde mit ID " + id + " nicht gefunden oder Aktualisierung fehlgeschlagen"));
    }

    /**
     * Erstellt einen neuen Kunden.
     *
     * @param customer Das Kundenobjekt, das erstellt werden soll.
     * @return ResponseEntity mit dem erstellten Kunden und dem URI seiner Ressource.
     */
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.saveCustomer(customer);
        URI location = URI.create("/customers/" + savedCustomer.getId());
        return ResponseEntity.created(location).body(savedCustomer);
    }

    /**
     * Führt eine weiche Löschung eines Kunden durch (Markierung als gelöscht).
     *
     * @param id Die ID des Kunden, der gelöscht werden soll.
     * @return ResponseEntity mit dem Statuscode 204 (No Content).
     */
    @DeleteMapping("/{id}/soft")
    public ResponseEntity<Void> softDeleteCustomer(@PathVariable Long id) {
        customerService.softDeleteCustomerById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Führt eine harte Löschung eines Kunden durch (dauerhafte Entfernung).
     *
     * @param id Die ID des Kunden, der gelöscht werden soll.
     * @return ResponseEntity mit dem Statuscode 204 (No Content).
     */
    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDeleteCustomer(@PathVariable Long id) {
        customerService.hardDeleteCustomerById(id);
        return ResponseEntity.noContent().build();
    }

    // <---------------- Spezifische-Requests ---------------->

    /**
     * Ruft einen Kunden anhand seiner ID ab.
     *
     * @param id Die ID des Kunden.
     * @return ResponseEntity mit dem gefundenen Kunden.
     * @throws CustomerNotFoundException Wenn kein Kunde mit der angegebenen ID gefunden wurde.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new CustomerNotFoundException("Kunde mit ID " + id + " nicht gefunden"));
    }

    /**
     * Ruft Kunden anhand ihres Spitznamens ab.
     *
     * @param nickName Der Spitzname des Kunden.
     * @return ResponseEntity mit der Liste der gefundenen Kunden.
     * @throws CustomerNotFoundException Wenn kein Kunde mit dem angegebenen Spitznamen gefunden wurde.
     */
    @GetMapping("/nickName/{nickName}")
    public ResponseEntity<Customer> getCustomerByNickName(@PathVariable String nickName) {
        return customerService.getCustomerByNickName(nickName)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new CustomerNotFoundException("Kunde mit dem Spitznamen " + nickName + " nicht gefunden"));
    }


    /**
     * Ruft Kunden anhand ihres Nachnamens ab.
     *
     * @param lastName Der Nachname des Kunden.
     * @return ResponseEntity mit der Liste der gefundenen Kunden.
     * @throws CustomerNotFoundException Wenn kein Kunde mit dem angegebenen Nachnamen gefunden wurde.
     */
    @GetMapping("/lastName/{lastName}")
    public ResponseEntity<List<Customer>> getCustomersByLastName(@PathVariable String lastName) {
        List<Customer> customers = customerService.getCustomersByLastName(lastName);
        if (customers.isEmpty()) {
            throw new CustomerNotFoundException("Kein Kunde mit Nachname '" + lastName + "' gefunden");
        }
        return ResponseEntity.ok(customers);
    }

    /**
     * Sucht Kunden anhand eines Namensfragments.
     *
     * @param searchQuery Der Suchbegriff für den Namen.
     * @return ResponseEntity mit der Liste der gefundenen Kunden.
     * @throws CustomerNotFoundException Wenn keine Kunden mit dem angegebenen Namensfragment gefunden wurden.
     */
    @GetMapping("/search/{searchQuery}")
    public ResponseEntity<List<Customer>> searchCustomersByName(@PathVariable String searchQuery) {
        List<Customer> customers = customerService.searchCustomersByName(searchQuery);
        if (customers.isEmpty()) {
            throw new CustomerNotFoundException("Keine Kunden mit Name ähnlich '" + searchQuery + "' gefunden");
        }
        return ResponseEntity.ok(customers);
    }

    /**
     * Ruft einen Kunden anhand seiner E-Mail-Adresse ab.
     *
     * @param email Die E-Mail-Adresse des Kunden.
     * @return ResponseEntity mit dem gefundenen Kunden.
     * @throws CustomerNotFoundException Wenn kein Kunde mit der angegebenen E-Mail-Adresse gefunden wurde.
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Customer> getCustomerByEmail(@PathVariable String email) {
        return customerService.getCustomerByEmail(email)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new CustomerNotFoundException("Kein Kunde mit E-Mail '" + email + "' gefunden"));
    }

    /**
     * Ruft Kunden anhand ihrer Telefonnummer ab.
     *
     * @param phoneNumber Die Telefonnummer des Kunden.
     * @return ResponseEntity mit der Liste der gefundenen Kunden.
     * @throws CustomerNotFoundException Wenn kein Kunde mit der angegebenen Telefonnummer gefunden wurde.
     */
    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<List<Customer>> getCustomersByPhoneNumber(@PathVariable String phoneNumber) {
        List<Customer> customers = customerService.getCustomersByPhoneNumber(phoneNumber);
        if (customers.isEmpty()) {
            throw new CustomerNotFoundException("Kein Kunde mit Telefonnummer '" + phoneNumber + "' gefunden");
        }
        return ResponseEntity.ok(customers);
    }

    /**
     * Ruft einen Kunden anhand seiner Handynummer ab.
     *
     * @param mobileNumber Die Handynummer des Kunden.
     * @return ResponseEntity mit dem gefundenen Kunden.
     * @throws CustomerNotFoundException Wenn kein Kunde mit der angegebenen Handynummer gefunden wurde.
     */
    @GetMapping("/mobile/{mobileNumber}")
    public ResponseEntity<Customer> getCustomerByMobileNumber(@PathVariable String mobileNumber) {
        return customerService.getCustomerByMobileNumber(mobileNumber)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new CustomerNotFoundException("Kein Kunde mit Handynummer '" + mobileNumber + "' gefunden"));
    }
}

