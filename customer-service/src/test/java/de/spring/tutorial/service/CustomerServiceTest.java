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

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testklasse für den {@link CustomerService}.
 * Diese Tests stellen sicher, dass die Methoden des CustomerService wie erwartet funktionieren.
 */
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer testCustomer1;

    /**
     * Setup-Methode, die vor jedem Testfall ausgeführt wird.
     * Hier werden 2 Testkunden erstellt, der in den Tests verwendet wird.
     */
    @BeforeEach
    void setUp() {
        testCustomer1 = new Customer();
        testCustomer1.setId(1L);
        testCustomer1.setNickName("Maxi");
        testCustomer1.setFirstName("Max");
        testCustomer1.setLastName("Mustermann");
        testCustomer1.setEmail("max.mustermann@example.com");
        testCustomer1.setPhoneNumber("0123456789");
        testCustomer1.setMobileNumber("016023234578");
    }

    /**
     * Testet die Methode {@link CustomerService#getCustomerById(Long)} im Erfolgsfall,
     * wenn der Kunde mit der angegebenen ID gefunden wird.
     */
    @Test
    void testGetCustomerByIdSuccess() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer1));

        Customer result = customerService.getCustomerById(1L).orElseThrow();

        assertEquals(testCustomer1.getId(), result.getId());
        assertEquals(testCustomer1.getNickName(), result.getNickName());
        assertEquals(testCustomer1.getFirstName(), result.getFirstName());
        assertEquals(testCustomer1.getLastName(), result.getLastName());
        assertEquals(testCustomer1.getEmail(), result.getEmail());
        assertEquals(testCustomer1.getPhoneNumber(), result.getPhoneNumber());

        verify(customerRepository, times(1)).findById(1L);
    }

    /**
     * Testet die Methode {@link CustomerService#getCustomerById(Long)} im Fall,
     * dass der Kunde mit der angegebenen ID nicht gefunden wird.
     * Es wird erwartet, dass eine {@link CustomerNotFoundException} geworfen wird.
     */
    @Test
    void testGetCustomerByIdNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(1L));

        verify(customerRepository, times(1)).findById(1L);
    }

    /**
     * Testet die Methode {@link CustomerService#getCustomerByNickName(String)} im Erfolgsfall,
     * wenn der Kunde mit dem angegebenen Nicknamen gefunden wird.
     */
    @Test
    void testGetCustomerByNicknameSuccess() {
        when(customerRepository.findByNickName("Maxi")).thenReturn(Optional.of(testCustomer1));

        Customer result = customerService.getCustomerByNickName("Maxi").orElseThrow();

        assertEquals(testCustomer1.getId(), result.getId());
        assertEquals(testCustomer1.getNickName(), result.getNickName());
        assertEquals(testCustomer1.getFirstName(), result.getFirstName());
        assertEquals(testCustomer1.getLastName(), result.getLastName());
        assertEquals(testCustomer1.getEmail(), result.getEmail());
        assertEquals(testCustomer1.getPhoneNumber(), result.getPhoneNumber());

        verify(customerRepository, times(1)).findByNickName("Maxi");
    }

    /**
     * Testet die Methode {@link CustomerService#getCustomerByNickName(String)} im Fall,
     * dass kein Kunde mit dem angegebenen Nicknamen gefunden wird.
     * Es wird erwartet, dass eine {@link CustomerNotFoundException} geworfen wird.
     */
    @Test
    void testGetCustomerByNickNameNotFound() {
        when(customerRepository.findByNickName("Maxi")).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerByNickName("Maxi"));

        verify(customerRepository, times(1)).findByNickName("Maxi");
    }

    /**
     * Testet die Methode {@link CustomerService#getCustomersByLastName(String)} im Erfolgsfall,
     * wenn Kunden mit dem angegebenen Nachnamen gefunden werden.
     */
    @Test
    void testGetCustomersByLastNameSuccess() {
        when(customerRepository.findByLastName("Mustermann")).thenReturn(List.of(testCustomer1));

        List<Customer> result = customerService.getCustomersByLastName("Mustermann");

        assertFalse(result.isEmpty(), "Die Liste sollte nicht leer sein.");
        assertEquals(1, result.size(), "Die Liste sollte genau einen Kunden enthalten.");
        assertTrue(result.contains(testCustomer1), "Die Liste sollte den ersten Kunden enthalten.");

        verify(customerRepository, times(1)).findByLastName("Mustermann");
    }

    /**
     * Testet die Methode {@link CustomerService#getCustomersByLastName(String)} im Fall,
     * dass keine Kunden mit dem angegebenen Nachnamen gefunden werden.
     * Es wird erwartet, dass eine {@link CustomerNotFoundException} geworfen wird.
     */
    @Test
    void testGetCustomersByLastNameNotFound() {
        when(customerRepository.findByLastName("Mustermann")).thenReturn(List.of());

        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomersByLastName("Mustermann"));

        verify(customerRepository, times(1)).findByLastName("Mustermann");
    }
}
