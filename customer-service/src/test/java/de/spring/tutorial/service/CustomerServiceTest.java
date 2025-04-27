package de.spring.tutorial.service;

import de.spring.tutorial.model.Customer;
import de.spring.tutorial.repository.CustomerRepository;
import de.spring.tutorial.exception.CustomerNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        // Testkunde erstellen
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setFirstName("Max");
        testCustomer.setLastName("Mustermann");
        testCustomer.setEmail("max.mustermann@example.com");
        testCustomer.setPhoneNumber("0123456789");
    }

    @Test
    void testGetCustomerByIdSuccess() {
        // Setup: Mock das Repository, damit der Kunde gefunden wird
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));

        // Test: Aufruf der Service-Methoden
        Customer result = customerService.getCustomerById(1L).orElseThrow();

        // Assertion: Überprüfe, ob der erwartete Kunde zurückgegeben wird
        assertEquals(testCustomer.getId(), result.getId());
        assertEquals(testCustomer.getFirstName(), result.getFirstName());
        assertEquals(testCustomer.getLastName(), result.getLastName());

        // Verifizieren, dass der Repository-Mock mit der richtigen ID aufgerufen wurde
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCustomerByIdNotFound() {
        // Setup: Mock das Repository, damit der Kunde nicht gefunden wird
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        // Test: Aufruf der Service-Methode, die eine Ausnahme werfen sollte
        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(1L));

        // Verifizieren, dass das Repository tatsächlich nach dem Kunden gesucht hat
        verify(customerRepository, times(1)).findById(1L);
    }

    // Weitere Tests können hier hinzugefügt werden (z.B. für das Erstellen von Kunden, Aktualisieren etc.)
}

