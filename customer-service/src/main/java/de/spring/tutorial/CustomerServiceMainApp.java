package de.spring.tutorial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hauptklasse zum Starten der Customer-Service-Anwendung.
 */
@SpringBootApplication
public class CustomerServiceMainApp {

    /**
     * Einstiegspunkt der Spring Boot Anwendung.
     *
     * @param args Programmargumente.
     */
    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceMainApp.class, args);
    }
}
