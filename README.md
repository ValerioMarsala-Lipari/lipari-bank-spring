# LipariBank

Backend bancario didattico sviluppato con **Java 21** e **Spring Boot**, con l'obiettivo di applicare progressivamente concetti di backend development enterprise.

Il progetto viene sviluppato in maniera incrementale attraverso esercizi e funzionalità che introducono concetti di architettura, REST API, dependency injection, persistenza, JPA, validazione, gestione degli errori, DTO, mapping, transazioni, sicurezza e altri aspetti tipici di un backend enterprise.

## Stack

* Java 21
* Spring Boot 4.1.1
* Maven
* PostgreSQL 16
* Spring Data JPA
* Hibernate
* Liquibase
* MapStruct
* Lombok
* Jakarta Bean Validation
* Spring Security
* Springdoc OpenAPI / Swagger UI
* Docker

## Architettura

Il progetto segue un approccio **feature-first**, con una separazione interna a layer:

```text
com.lipari.bank
├── account/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── mapper/
│   ├── dto/
│   └── entity/
│
├── customer/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── mapper/
│   ├── dto/
│   └── entity/
│
├── transfer/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── dto/
│   └── entity/
│
├── auth/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── dto/
│   └── entity/
│
└── shared/
    ├── config/
    └── exception/
```

La classe principale dell'applicazione è:

```text
com.lipari.bank.LipariBankApplication
```

### Layer architecture

Le richieste REST seguono questo flusso:

```text
HTTP Request
     ↓
Controller
     ↓
Request DTO
     ↓
Service
     ↓
Repository
     ↓
Entity
     ↓
Database

Entity
     ↓
MapStruct Mapper
     ↓
Response DTO
     ↓
HTTP Response
```

I Controller sono responsabili dell'esposizione delle API REST, i Service della logica applicativa e delle transazioni, i Repository dell'accesso ai dati e i Mapper della conversione tra DTO ed Entity.

## REST API

Sono attualmente implementate le API CRUD per **Account** e **Customer**, oltre alle API relative ai trasferimenti e all'autenticazione.

### Account

```text
GET    /api/v1/accounts
GET    /api/v1/accounts/{id}
POST   /api/v1/accounts
PUT    /api/v1/accounts/{id}
DELETE /api/v1/accounts/{id}
```

### Customer

```text
GET    /api/v1/customers
GET    /api/v1/customers/{id}
POST   /api/v1/customers
PUT    /api/v1/customers/{id}
DELETE /api/v1/customers/{id}
```

### Authentication

```text
POST   /api/v1/auth/register
```

La registrazione crea un nuovo utente applicativo con ruolo `USER`.

Gli endpoint protetti richiedono autenticazione.

## DTO

Per le API vengono utilizzati DTO separati dalle Entity, evitando di esporre direttamente il modello di persistenza.

Attualmente sono presenti DTO dedicati alle principali operazioni:

```text
Account
├── AccountCreateRequest
├── AccountUpdateRequest
└── AccountResponse

Customer
├── CustomerCreateRequest
├── CustomerUpdateRequest
└── CustomerResponse

Auth
├── RegisterRequest
└── RegisterResponse
```

La validazione degli input viene effettuata tramite **Jakarta Bean Validation**.

Esempi di vincoli utilizzati:

* `@NotBlank`
* `@NotNull`
* `@Email`
* `@PositiveOrZero`
* `@Valid`

Gli errori di validazione vengono restituiti tramite una risposta HTTP `400 Bad Request`.

## MapStruct

La conversione tra DTO ed Entity viene gestita tramite **MapStruct**.

```text
AccountCreateRequest ──→ Account
Account ───────────────→ AccountResponse

CustomerCreateRequest ──→ Customer
Customer ───────────────→ CustomerResponse

CustomerUpdateRequest ──→ Customer
```

Per gli aggiornamenti viene utilizzato `@MappingTarget` per modificare l'Entity esistente.

## Persistenza

La persistenza è implementata tramite **Spring Data JPA** e **PostgreSQL**.

```text
Service
   ↓
Spring Data Repository
   ↓
JPA / Hibernate
   ↓
PostgreSQL
```

Il database PostgreSQL viene eseguito tramite Docker.

Configurazione di sviluppo:

```text
Database: liparibank
User:     liparibank_user
Port:     5432
```

Hibernate utilizza:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
```

Lo schema del database non viene quindi generato automaticamente da Hibernate.

## Liquibase

La gestione dello schema del database è affidata a **Liquibase**.

I cambiamenti allo schema vengono organizzati tramite changelog versionati:

```text
src/main/resources/db/changelog/
├── db.changelog-master.yaml
├── 001-...
├── 002-...
├── 003-...
└── 004-create-app-users.yaml
```

Liquibase permette di mantenere sincronizzato lo schema del database con le versioni del progetto.

Tra le tabelle gestite dal progetto sono presenti:

* `customers`
* `accounts`
* `transfers`
* `app_users`

## Account e Customer

Un Account può essere creato solamente se esiste un Customer valido associato al `fiscalCode` fornito nella richiesta.

```text
POST /api/v1/accounts
        │
        ▼
   fiscalCode
        │
        ▼
CustomerRepository
        │
   ┌────┴────┐
   │         │
 trovato   non trovato
   │         │
   ▼         ▼
 Account    404
```

Questa verifica viene effettuata nel `AccountService` prima della creazione dell'Account.

## Trasferimenti

Il progetto implementa il trasferimento di denaro tra Account tramite un `TransferService`.

L'operazione comprende:

```text
Source Account
      │
      │ debit
      ▼
TransferService
      │
      │ credit
      ▼
Destination Account
```

Il trasferimento viene eseguito all'interno di una singola transazione Spring:

```java
@Transactional
```

In questo modo il debit e il credit fanno parte della stessa unità transazionale.

Se il trasferimento non può essere completato, l'operazione viene sottoposta a rollback e i saldi rimangono invariati.

Sono presenti eccezioni di dominio dedicate, tra cui:

* `AccountNotFoundException`
* `InsufficientFundsException`

## Gestione delle transazioni

La gestione transazionale viene effettuata a livello di Service.

Il `TransferService` garantisce l'atomicità dell'operazione:

```text
BEGIN TRANSACTION
      │
      ├── debit account
      │
      ├── credit account
      │
      └── create transfer
             │
       ┌─────┴─────┐
       │           │
     COMMIT      ROLLBACK
       │           │
     success     failure
```

Sono stati verificati sia il caso di trasferimento valido sia il caso di saldo insufficiente.

## Gestione degli errori

La gestione delle eccezioni è centralizzata tramite `@RestControllerAdvice`.

Attualmente vengono gestiti, tra gli altri:

* `AccountNotFoundException` → `404 Not Found`
* `InsufficientFundsException` → `400 Bad Request`
* `UsernameAlreadyExistsException` → `409 Conflict`
* `MethodArgumentNotValidException` → `400 Bad Request`

Le risposte di validazione includono gli errori relativi ai singoli campi della richiesta.

## Spring Security

La sicurezza degli endpoint è implementata tramite **Spring Security**.

Gli utenti applicativi sono rappresentati dall'Entity:

```text
AppUser
├── id
├── username
├── password
├── email
└── role
```

I ruoli disponibili sono:

```text
USER
ADMIN
```

Le password non vengono salvate in chiaro: durante la registrazione vengono cifrate tramite **BCrypt**.

### Registrazione

```text
POST /api/v1/auth/register
```

Il flusso di registrazione è:

```text
RegisterRequest
      ↓
AuthService
      ↓
username duplicate check
      ↓
BCrypt password hashing
      ↓
AppUserRepository
      ↓
PostgreSQL
```

La response di registrazione non espone il password hash.

### Autenticazione

L'autenticazione attuale utilizza **HTTP Basic**.

La configurazione della security chain prevede:

```text
/api/v1/auth/**   → permitAll()
/api/v1/admin/**  → hasRole("ADMIN")
tutto il resto    → authenticated()
```

La sessione è configurata come stateless e il CSRF è disabilitato.

Gli endpoint protetti senza autenticazione restituiscono:

```text
401 Unauthorized
```

Con credenziali valide, ad esempio:

```text
GET /api/v1/accounts
Authorization: Basic ...
```

la richiesta viene autenticata e raggiunge il Controller.

## Configurazione

La configurazione applicativa è gestita tramite `@ConfigurationProperties`.

```text
application.yml
      ↓
@ConfigurationProperties
      ↓
LipariBankProperties
      ↓
constructor injection
      ↓
componenti Spring
```

Le proprietà applicative utilizzano il prefisso:

```yaml
liparibank:
```

Configurazione attuale:

```yaml
liparibank:
  bank-code: LPRI-IT
  max-transfer-amount: 50000.00
  audit:
    enabled: true
```

La configurazione è rappresentata da:

```text
LipariBankProperties
├── bankCode
├── maxTransferAmount
└── audit
    └── enabled
```

## Profili

Sono configurati due ambienti:

* `dev` — logging `DEBUG`
* `prod` — logging `INFO`

Il profilo di sviluppo viene attualmente attivato tramite:

```yaml
spring:
  profiles:
    active: dev
```

## OpenAPI / Swagger

La documentazione delle API REST viene generata tramite **Springdoc OpenAPI**.

Swagger UI è disponibile all'indirizzo:

```text
http://localhost:8080/swagger-ui.html
```

La specifica OpenAPI è disponibile tramite:

```text
http://localhost:8080/v3/api-docs
```

Controller, endpoint, DTO e relativi campi sono documentati tramite annotazioni OpenAPI.

## Spring Boot concepts

La prima parte del progetto è dedicata alla pratica dei principali meccanismi di Spring:

* Inversion of Control (IoC)
* Dependency Injection
* Constructor Injection
* `ApplicationContext`
* Bean lifecycle
* `@PostConstruct`
* `@Configuration`
* `@ConfigurationProperties`
* Spring profiles
* Bean scopes
* Spring Boot auto-configuration
* Proxy e AOP
* Spring Data JPA
* JPA Entity Mapping
* Fetch strategies
* N+1 query problem
* Cascade operations
* Transazioni
* Isolation levels
* Rollback
* Spring Security

## Stato del progetto

Il progetto ha completato le principali fasi didattiche iniziali, passando da una prima implementazione REST in-memory a una vera persistenza relazionale con JPA e PostgreSQL e introducendo successivamente validazione, transazioni e sicurezza.

### Completato

* struttura a package feature-first;
* configurazione `application.yml`;
* configurazione dei profili `dev` e `prod`;
* introduzione di `LipariBankProperties`;
* abilitazione di `@ConfigurationPropertiesScan`;
* logging delle configurazioni all'avvio tramite un componente Spring;
* CRUD completo per Account;
* CRUD completo per Customer;
* DTO Create / Update / Response;
* MapStruct per il mapping DTO/Entity;
* validazione tramite Jakarta Bean Validation;
* gestione centralizzata delle eccezioni;
* documentazione OpenAPI / Swagger;
* validazione dell'esistenza del Customer prima della creazione di un Account;
* introduzione di PostgreSQL tramite Docker;
* persistenza tramite Spring Data JPA e Hibernate;
* gestione dello schema tramite Liquibase;
* Entity e Repository per i trasferimenti;
* `TransferService` transazionale;
* gestione del rollback in caso di saldo insufficiente;
* eccezioni custom per gli errori di dominio;
* validazione dei DTO con `@Valid`;
* Entity `AppUser` e ruoli `USER` / `ADMIN`;
* registrazione degli utenti;
* password hashing tramite BCrypt;
* autenticazione tramite Spring Security e HTTP Basic;
* protezione degli endpoint tramite ruoli e autenticazione;
* configurazione stateless della Security;
* gestione degli username duplicati tramite `409 Conflict`.

### Prossimi sviluppi

Il progetto continuerà progressivamente con l'introduzione di ulteriori concetti e funzionalità backend enterprise, approfondendo autenticazione e autorizzazione, gestione avanzata delle transazioni, audit, testing, aspetti architetturali e ulteriori funzionalità del dominio bancario.
