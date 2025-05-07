# spring-boot-backend – Ein Microservices-Tutorial-Projekt

Dieses Projekt wurde im Rahmen eines Tutorials für eine Lerngruppe erstellt und befindet sich derzeit noch in der  
aktiven Entwicklung. Ziel ist es, eine moderne und praxisnahe Microservice-Architektur mit Spring Boot Schritt für  
Schritt aufzubauen und verständlich zu dokumentieren.

## DAS PROJEKT IST NOCH IN BEARBEITUNG!
Sollten also noch Dinge wie ZB Testintegration etc Fehlen liegt es daran, das dieses Projekt aktuell  
noch bearbeitet wird!

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

| Modul              | Beschreibung                                                    |
|--------------------|-----------------------------------------------------------------|
| `auth-service`     | Verwalten von Registrierung, Login und JWT-Erzeugung            |
| `customer-service` | Kundenverwaltung mit CRUD-Funktionen und JWT-Validierung        |
| `api-gateway`      | Zentrales Gateway für Routing, Header-Prüfung und Weiterleitung |

---

## 🚀 Funktionsweise der Anwendung

### **Architekturübersicht**

Die Anwendung basiert auf einer **Microservices-Architektur**, die in drei Hauptmodule unterteilt ist:

1. **auth-service**: Dieser Microservice kümmert sich um die Benutzer-Authentifizierung und -Autorisierung.  
Er stellt Endpunkte zur Registrierung und Anmeldung zur Verfügung und generiert **JWTs** (JSON Web Tokens) zur  
Authentifizierung der Benutzer.

2. **customer-service**: In diesem Microservice werden Kunden verwaltet. Er bietet Funktionen zum Erstellen, Abrufen,  
Aktualisieren und Löschen von Kunden. Kunden werden mit einer eindeutigen ID identifiziert. Der Service stellt sicher,  
dass nur authentifizierte Benutzer mit einem gültigen JWT Zugriff auf die Daten haben.

3. **api-gateway**: Das API-Gateway fungiert als zentraler Einstiegspunkt für alle Anfragen. Es leitet die Anfragen an  
den entsprechenden Microservice weiter und sorgt für eine einfache Authentifizierung über den **Authorization-Header**  
mit dem JWT.

### **Authentifizierung & Autorisierung**

Die Authentifizierung erfolgt über **JWT (JSON Web Token)**. Ein Benutzer muss sich über den `auth-service` anmelden,  
um ein Token zu erhalten. Dieses Token wird anschließend für die Authentifizierung bei anderen Microservices  
(wie `customer-service`) verwendet.

**Beispielablauf:**

1. Ein Benutzer registriert sich oder meldet sich an, indem er seine Zugangsdaten (E-Mail und Passwort) an den  
`auth-service` sendet.
2. Nach erfolgreicher Anmeldung erhält der Benutzer ein JWT.
3. Das JWT wird dann in den Header zukünftiger Anfragen eingefügt, wenn auf den `customer-service` zugegriffen wird.
4. Das API-Gateway überprüft das JWT bei jeder Anfrage, um sicherzustellen, dass der Benutzer authentifiziert ist,  
bevor die Anfrage an den entsprechenden Microservice weitergeleitet wird.

---

### **API Gateway** (`api-gateway`):

Das API-Gateway ist der zentrale Punkt für die Verwaltung von Anfragen. Es überprüft den **Authorization-Header** auf  
ein JWT und leitet die Anfrage nur dann an den entsprechenden Microservice weiter, wenn das Token gültig ist. Es stellt  
sicher, dass nur authentifizierte Benutzer Zugriff auf die Microservices haben.

---

## 🛠️ Weitere Funktionen (in Entwicklung)

- **Verwendung von Spring Cloud** für die Verwaltung und Skalierung der Microservices.
- **Fehlerlogging** und **Monitoring** für eine bessere Fehlerbehandlung und Systemüberwachung.
- **Automatisierte Tests** für die Endpunkte und die gesamte Architektur.

---

### Weitere Hinweise

- Die JWT-Authentifizierung ist in diesem Beispiel einfach gehalten, es wird empfohlen, weitere Sicherheitsmaßnahmen zu  
integrieren, wie z. B. die Verwendung von Refresh Tokens und eine sichere Speicherung von Passwörtern.
- Du kannst das Projekt leicht erweitern, indem du neue Microservices hinzufügst, die ebenfalls über das API-Gateway  
zugänglich gemacht werden.

---

## 📝 ToDo-Liste

Diese Punkte sind noch offen oder sollten im Laufe der Entwicklung verbessert werden:

### 🔐 Sicherheit & Authentifizierung
- [ ] Implementierung eines Refresh-Token-Mechanismus (z.B. Endpoint zum Erneuern von JWTs)
- [ ] Optional: Blacklisting bzw. Invalidierung von Tokens bei Logout

### ✅ Validierung & Datenkonsistenz
- [ ] Nutzung von Bean Validation (z.B. `@Email`, `@Pattern`, `@NotBlank`) in DTO-Klassen
- [ ] Konsistente Verwendung passender HTTP-Statuscodes in allen Fehlerantworten (z.B. `409 CONFLICT` bei Duplikaten)

### 🧪 Tests
- [ ] Unit-Tests für Service-Klassen (z.B. `CustomerService`, `JwtTokenProvider`)
- [ ] Integrationstests für API-Endpunkte (z.B. Registrierung, Login, geschützte Ressourcen)
- [ ] Vorbereitung von Testdaten (z.B. via `data.sql` oder Testcontainer für MySQL)

### 📄 Dokumentation & Qualität
- [ ] Erweiterung der README mit Beispielen für API-Requests und Abläufe
- [ ] Optional: Beispielintegration eines weiteren Microservices (z.B. `order-service`) zur Demonstration der Erweiterbarkeit
