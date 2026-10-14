package com.lipari.bank.account.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.lipari.bank.account.entity.Account;
import com.lipari.bank.account.entity.AccountStatus;
import com.lipari.bank.customer.entity.Customer;
import com.lipari.bank.customer.entity.CustomerStatus;
import com.lipari.bank.customer.repository.CustomerRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest(
        properties = {
            "spring.datasource.url=jdbc:h2:mem:account-test;DB_CLOSE_DELAY=-1",
            "spring.datasource.driver-class-name=org.h2.Driver",
            "spring.datasource.username=sa",
            "spring.datasource.password=",
            "spring.liquibase.enabled=false",
            "spring.jpa.hibernate.ddl-auto=create-drop"
        })
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer createCustomer() {
        Customer customer = new Customer();
        customer.setFiscalCode("RSSMRA80A01H501Z");
        customer.setFirstName("Mario");
        customer.setLastName("Rossi");
        customer.setEmail("mario.rossi@example.com");
        customer.setPhone("+393331234567");
        customer.setStatus(CustomerStatus.ACTIVE);

        return customerRepository.save(customer);
    }

    private Account createAccount(String iban) {
        Account account = new Account();
        account.setIban(iban);
        account.setCustomer(createCustomer());
        account.setBalance(new BigDecimal("1000.00"));
        account.setStatus(AccountStatus.ACTIVE);
        account.setCreatedAt(LocalDateTime.of(2026, 10, 2, 10, 30));

        return accountRepository.save(account);
    }

    @Test
    void findByIban_whenAccountExists_returnsAccount() {
        // Given
        String iban = "IT60X0542811101000000123456";
        Account savedAccount = createAccount(iban);

        // When
        Optional<Account> result = accountRepository.findByIban(iban);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(savedAccount.getId());
        assertThat(result.get().getIban()).isEqualTo(iban);
    }

    @Test
    void findByIban_whenAccountDoesNotExist_returnsEmpty() {
        // Given
        String iban = "IT60X0542811101000000999999";

        // When
        Optional<Account> result = accountRepository.findByIban(iban);

        // Then
        assertThat(result).isEmpty();
    }
}
