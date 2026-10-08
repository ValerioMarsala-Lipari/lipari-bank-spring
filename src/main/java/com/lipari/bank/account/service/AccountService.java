package com.lipari.bank.account.service;

import com.lipari.bank.account.dto.AccountCreateRequest;
import com.lipari.bank.account.dto.AccountResponse;
import com.lipari.bank.account.dto.AccountUpdateRequest;
import com.lipari.bank.account.entity.Account;
import com.lipari.bank.account.entity.AccountStatus;
import com.lipari.bank.account.mapper.AccountMapper;
import com.lipari.bank.account.repository.AccountRepository;
import com.lipari.bank.customer.entity.Customer;
import com.lipari.bank.customer.repository.CustomerRepository;
import com.lipari.bank.shared.exception.AccountNotFoundException;
import com.lipari.bank.shared.exception.ResourceNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final CustomerRepository customerRepository;

    public List<AccountResponse> findAll() {
        log.debug("Retrieving all accounts");

        return accountRepository.findAll().stream()
                .map(accountMapper::toResponse)
                .toList();
    }

    public AccountResponse findById(Long id) {
        log.debug("Retrieving account with id: {}", id);

        Account account =
                accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(String.valueOf(id)));

        return accountMapper.toResponse(account);
    }

    public AccountResponse create(AccountCreateRequest request) {
        log.info("Creating new account for fiscal code: {}", request.fiscalCode());

        Customer customer = customerRepository
                .findByFiscalCode(request.fiscalCode())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found with fiscal code: " + request.fiscalCode()));

        Account account = accountMapper.toEntity(request);

        account.setCustomer(customer);
        account.setStatus(AccountStatus.ACTIVE);
        account.setCreatedAt(LocalDateTime.now());

        Account saved = accountRepository.save(account);

        log.info("Account created with id: {} and IBAN: {}", saved.getId(), saved.getIban());

        return accountMapper.toResponse(saved);
    }

    public AccountResponse update(Long id, AccountUpdateRequest request) {
        log.info("Updating account with id: {}", id);

        Account account =
                accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(String.valueOf(id)));

        account.setBalance(request.balance());

        Account updated = accountRepository.save(account);

        return accountMapper.toResponse(updated);
    }

    public void delete(Long id) {
        log.info("Deleting account with id: {}", id);

        if (!accountRepository.existsById(id)) {
            throw new AccountNotFoundException(String.valueOf(id));
        }

        accountRepository.deleteById(id);
    }

    public AccountResponse findByIban(String iban) {
        log.debug("Retrieving account with iban: {}", iban);

        Account account = accountRepository.findByIban(iban).orElseThrow(() -> new AccountNotFoundException(iban));

        return accountMapper.toResponse(account);
    }
}
