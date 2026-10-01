# LipariBank

Backend bancario didattico sviluppato con **Java** e **Spring Boot**, con l'obiettivo di applicare progressivamente concetti di backend development enterprise.

## Stack

* Java 21
* Spring Boot 3.x
* Maven

## Architettura

Il progetto segue un approccio **feature-first**, con una separazione interna a layer:

```text
com.lipari.bank
├── config/
│
├── account/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── dto/
│   └── entity/
│
├── customer/
│   ├── controller/
│   ├── service/
│   ├── repository/
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

Il progetto è attualmente nella fase iniziale di configurazione e bootstrap.

Le prime attività completate sono:

* creazione della struttura a package;
* configurazione `application.yml`;
* configurazione dei profili `dev` e `prod`;
* introduzione di `LipariBankProperties`;
* abilitazione di `@ConfigurationPropertiesScan`;
* logging delle configurazioni all'avvio tramite un componente Spring.
