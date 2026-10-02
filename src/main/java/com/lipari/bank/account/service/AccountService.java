package com.lipari.bank.account.service;

import com.lipari.bank.account.dto.AccountCreateRequest;
import com.lipari.bank.account.dto.AccountResponse;
import com.lipari.bank.account.dto.AccountUpdateRequest;
import com.lipari.bank.account.entity.Account;
import com.lipari.bank.account.entity.AccountStatus;
import com.lipari.bank.account.mapper.AccountMapper;
import com.lipari.bank.account.repository.AccountRepository;
import com.lipari.bank.common.exception.ResourceNotFoundException;
import com.lipari.bank.customer.entity.Customer;
import com.lipari.bank.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

  private final AccountRepository accountRepository;
  private final AccountMapper accountMapper;
  private final CustomerRepository customerRepository;

  public List<AccountResponse> findAll() {
    log.debug("Retrieving all accounts");

    return accountRepository.findAll()
        .stream()
        .map(accountMapper::toResponse)
        .toList();
  }

  public AccountResponse findById(Long id) {
    log.debug("Retrieving account with id: {}", id);

    Account account = accountRepository.findById(id);

    if (account == null) {
      throw new ResourceNotFoundException("Account not found with id: " + id);
    }

    return accountMapper.toResponse(account);
  }

  public AccountResponse create(AccountCreateRequest request) {
    log.info(
        "Creating new account for fiscal code: {}",
        request.fiscalCode()
    );

    Customer customer = customerRepository.findByFiscalCode(
        request.fiscalCode()
    );

    if (customer == null) {
      throw new ResourceNotFoundException(
          "Customer not found with fiscal code: "
              + request.fiscalCode()
      );
    }

    Account account = accountMapper.toEntity(request);

    account.setIban(
        "IT" + UUID.randomUUID()
            .toString()
            .replace("-", "")
            .substring(0, 24)
            .toUpperCase()
    );

    account.setStatus(AccountStatus.ACTIVE);
    account.setCreatedAt(LocalDateTime.now());

    Account saved = accountRepository.save(account);

    log.info(
        "Account created with id: {} and IBAN: {}",
        saved.getId(),
        saved.getIban()
    );

    return accountMapper.toResponse(saved);
  }

  public AccountResponse update(Long id, AccountUpdateRequest request) {
    log.info("Updating account with id: {}", id);

    Account account = accountRepository.findById(id);

    if (account == null) {
      throw new ResourceNotFoundException("Account not found with id: " + id);
    }

    account.setBalance(request.balance());

    Account updated = accountRepository.save(account);

    return accountMapper.toResponse(updated);
  }

  public void delete(Long id) {
    log.info("Deleting account with id: {}", id);

    if (!accountRepository.existsById(id)) {
      throw new ResourceNotFoundException("Account not found with id: " + id);
    }

    accountRepository.deleteById(id);
  }
}