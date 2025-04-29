package de.spring.tutorial.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.spring.tutorial.exception.CustomerNotFoundException;
import de.spring.tutorial.model.Customer;
import de.spring.tutorial.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testklasse für den {@link CustomerService}.
 * Testet CustomerService mit normalen und parametrisierten Tests.
 */
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private List<Customer> testCustomers;

    /**
     * Setup vor jedem Test: Lade Testkunden.
     */
    @BeforeEach
    void setUp() throws Exception {
        testCustomers = loadTestCustomers();
    }

    /**
     * Parametrisierter Test für {@link CustomerService#getCustomerByNickName(String)},
     * dynamisch aus der JSON geladen.
     */
    @ParameterizedTest
    @MethodSource("provideNickNamesFromJson")
    void testGetCustomerByNickNameSuccess(String nickName) {
        Customer customer = testCustomers.stream()
                .filter(c -> c.getNickName().equals(nickName))
                .findFirst()
                .orElseThrow();

        when(customerRepository.findByNickName(nickName)).thenReturn(Optional.of(customer));

        Customer result = customerService.getCustomerByNickName(nickName).orElseThrow();

        assertEquals(customer.getNickName(), result.getNickName());
        assertEquals(customer.getEmail(), result.getEmail());

        verify(customerRepository, times(1)).findByNickName(nickName);
    }

    /**
     * Testet {@link CustomerService#getCustomerByNickName(String)} für einen ungültigen Nickname.
     */
    @Test
    void testGetCustomerByNickNameNotFound() {
        when(customerRepository.findByNickName("Unbekannt")).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerByNickName("Unbekannt"));

        verify(customerRepository, times(1)).findByNickName("Unbekannt");
    }

    /**
     * Parametrisierter Test für {@link CustomerService#getCustomersByLastName(String)}.
     * Testet verschiedene Lastnames aus der JSON.
     */
    @ParameterizedTest
    @MethodSource("provideLastNamesFromJson")
    void testGetCustomersByLastNameSuccess(String lastName) {
        List<Customer> expectedCustomers = testCustomers.stream()
                .filter(c -> c.getLastName().equals(lastName))
                .toList();

        when(customerRepository.findByLastName(lastName)).thenReturn(expectedCustomers);

        List<Customer> result = customerService.getCustomersByLastName(lastName);

        assertFalse(result.isEmpty(), "Liste sollte nicht leer sein für: " + lastName);
        assertEquals(expectedCustomers.size(), result.size(), "Falsche Anzahl Kunden für: " + lastName);

        verify(customerRepository, times(1)).findByLastName(lastName);
    }

    /**
     * Testet {@link CustomerService#getCustomersByLastName(String)} für unbekannten Nachnamen.
     */
    @Test
    void testGetCustomersByLastNameNotFound() {
        when(customerRepository.findByLastName("Unbekannt")).thenReturn(List.of());

        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomersByLastName("Unbekannt"));

        verify(customerRepository, times(1)).findByLastName("Unbekannt");
    }

    /**
     * Lädt Testkunden aus einer JSON-Datei im Ressourcenverzeichnis.
     *
     * @return Liste von Testkunden
     * @throws Exception falls Fehler beim Einlesen oder Parsen auftreten
     */
    static List<Customer> loadTestCustomers() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = CustomerServiceTest.class.getResourceAsStream("/test-customers.json");
        if (inputStream == null) {
            throw new IllegalStateException("Testkunden-Datei nicht gefunden!");
        }
        try {
            return objectMapper.readValue(inputStream, new TypeReference<>() {});
        } catch (Exception e) {
            // Fehler beim Parsen loggen
            System.err.println("Fehler beim Laden der Testkunden: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Stellt Testdaten für {@link CustomerService#getCustomerByNickName(String)} bereit.
     *
     * @return Stream von Arguments mit Nicknames
     */
    static Stream<String> provideNickNamesFromJson() throws Exception {
        List<Customer> customers = loadTestCustomers();
        return customers.stream()
                .map(Customer::getNickName); // Gebe nur die Nicknames zurück
    }

    /**
     * Stellt Testdaten für {@link CustomerService#getCustomersByLastName(String)} bereit.
     *
     * @return Stream von Arguments mit Lastnames
     */
    static Stream<String> provideLastNamesFromJson() throws Exception {
        List<Customer> customers = loadTestCustomers();
        return customers.stream()
                .map(Customer::getLastName); // Gebe nur die Lastnames zurück
    }
}
