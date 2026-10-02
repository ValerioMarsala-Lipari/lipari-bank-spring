# LipariBank

Backend bancario didattico sviluppato con **Java** e **Spring Boot**, con l'obiettivo di applicare progressivamente concetti di backend development enterprise.

Il progetto viene sviluppato in maniera incrementale attraverso esercizi e funzionalità che introducono concetti di architettura, REST API, validazione, gestione degli errori, DTO, mapping e successivamente persistenza, sicurezza e altri aspetti tipici di un backend enterprise.

## Stack

* Java 21
* Spring Boot 4.1.1
* Maven
* MapStruct
* Lombok
* Jakarta Bean Validation
* Springdoc OpenAPI / Swagger UI

## Architettura

Il progetto segue un approccio **feature-first**, con una separazione interna a layer:

```text
com.lipari.bank
├── common/
│   └── exception/
│
├── config/
│
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
└── transfer/
    ├── controller/
    ├── service/
    ├── repository/
    ├── dto/
    └── entity/
```

La classe principale dell'applicazione è:

```text
com.lipari.bank.BankApplication
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

Entity
     ↓
MapStruct Mapper
     ↓
Response DTO
     ↓
HTTP Response
```

I Controller sono responsabili dell'esposizione delle API REST, i Service della logica applicativa, i Repository dell'accesso ai dati e i Mapper della conversione tra DTO ed Entity.

## REST API

Sono attualmente implementate le API CRUD per **Account** e **Customer**.

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

Gli endpoint utilizzano DTO distinti per le operazioni di creazione, aggiornamento e risposta.

## DTO

Per ogni feature vengono utilizzati DTO separati per evitare di esporre direttamente le Entity attraverso le API.

Attualmente sono presenti:

```text
Account
├── AccountCreateRequest
├── AccountUpdateRequest
└── AccountResponse

Customer
├── CustomerCreateRequest
├── CustomerUpdateRequest
└── CustomerResponse
```

La validazione degli input viene effettuata tramite **Jakarta Bean Validation**.

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

## In-memory Repository

La persistenza attuale è volutamente semplificata e utilizza strutture dati in memoria.

```text
Repository
    ↓
HashMap<Long, Entity>
```

Sono attualmente presenti:

* `AccountRepository`
* `CustomerRepository`

L'implementazione potrà essere successivamente sostituita da una vera persistenza tramite database e Spring Data JPA.

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

## Gestione degli errori

La gestione delle eccezioni è centralizzata tramite `@RestControllerAdvice`.

Attualmente vengono gestiti:

* `ResourceNotFoundException` → `404 Not Found`
* `MethodArgumentNotValidException` → `400 Bad Request`

Le risposte di validazione includono gli errori relativi ai singoli campi della richiesta.

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
  bank-code: LPBK
  max-transfer-amount: 10000.00
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

## Stato del progetto

Il progetto ha completato la prima implementazione REST prevista per il modulo G2.

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
* repository in-memory tramite `HashMap`;
* validazione tramite Jakarta Bean Validation;
* gestione centralizzata delle eccezioni;
* documentazione OpenAPI / Swagger;
* validazione dell'esistenza del Customer prima della creazione di un Account.

### Prossimi sviluppi

Il progetto continuerà progressivamente con l'introduzione di ulteriori concetti e funzionalità backend, tra cui il dominio dei trasferimenti e successivamente aspetti di persistenza, sicurezza e altre componenti tipiche di un'applicazione enterprise.
