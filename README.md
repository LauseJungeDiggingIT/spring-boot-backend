# spring-boot-backend – Ein Microservices-Tutorial-Projekt

Dieses Projekt wurde im Rahmen eines Tutorials für eine Lerngruppe erstellt und befindet sich derzeit noch in der  
aktiven Entwicklung. Ziel ist es, eine moderne und praxisnahe Microservice-Architektur mit Spring Boot Schritt für  
Schritt aufzubauen und verständlich zu dokumentieren.

---

## 🔧 Technische Anforderungen

- **JDK:** 23
- **Build-Tool:** Maven (Multi-Modul-Projekt)
- **Spring Boot:** 3.4.4
- **Spring Cloud:** 2024.0.1
- **Datenbank:** MySQL (lokal, z. B. über MAMP/XAMPP)
- **API-Testtool:** Postman
- **Entwicklungsumgebung:** IntelliJ IDEA (empfohlen), alternativ VS Code
- **Libraries & Tools für das Hauptprojekt:**  
  *(noch nicht final optimiert – Abhängigkeiten werden aktuell gesammelt und sukzessive zentral ausgelagert)*
    - Lombok (zur Reduktion von Boilerplate-Code)
    - Spring Web / WebFlux
    - Spring Security
    - Spring Cloud Gateway

---

## 🧠 Projektstruktur & Idee

Dieses Projekt basiert auf einer **Microservice-Architektur mit Spring Boot**. Ziel ist es, ein modular aufgebautes  
Backend-System zu entwickeln, das leicht wartbar, verständlich und gut erweiterbar ist.

### Warum ein Parent-Projekt?

Das `spring-boot-backend`-Projekt dient als zentrales Maven-Parent-Projekt für alle Microservices und verwaltet die  
Abhängigkeiten, die in mehreren Services benötigt werden.

**Vorteile:**

- ✅ Gemeinsame Abhängigkeits- und Pluginverwaltung
- ✅ Einheitliche Versionierung und Build-Konfiguration
- ✅ Saubere Trennung der Services durch Modulstruktur
- ✅ Leicht um neue Microservices erweiterbar

---

## 📦 Enthaltene Module

| Modul             | Beschreibung                                                   |
|------------------|----------------------------------------------------------------|
| `auth-service`    | Verwalten von Registrierung, Login und JWT-Erzeugung          |
| `customer-service`| Kundenverwaltung mit CRUD-Funktionen und JWT-Validierung      |
| `api-gateway`     | Zentrales Gateway für Routing, Header-Prüfung und Weiterleitung |


... In Bearbeitung