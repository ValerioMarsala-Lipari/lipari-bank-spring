
package com.lipari.bank.account.service;

import com.lipari.bank.account.dto.AccountResponse;
import com.lipari.bank.account.entity.Account;
import com.lipari.bank.account.mapper.AccountMapper;
import com.lipari.bank.account.repository.AccountRepository;
import com.lipari.bank.customer.repository.CustomerRepository;
import com.lipari.bank.shared.exception.AccountNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private AccountMapper accountMapper;

  @Mock
  private CustomerRepository customerRepository;

  @InjectMocks
  private AccountService accountService;

  @Test
  void findById_whenAccountExists_returnsAccountResponse() {
    // Given
    Long accountId = 1L;
    Account account = new Account();
    AccountResponse expectedResponse = org.mockito.Mockito.mock(AccountResponse.class);

    when(accountRepository.findById(accountId))
        .thenReturn(Optional.of(account));
    when(accountMapper.toResponse(account))
        .thenReturn(expectedResponse);

    // When
    AccountResponse actualResponse = accountService.findById(accountId);

    // Then
    assertSame(expectedResponse, actualResponse);
    verify(accountRepository).findById(accountId);
    verify(accountMapper).toResponse(account);
  }

  @Test
  void findById_whenAccountDoesNotExist_throwsAccountNotFoundException() {
    // Given
    Long accountId = 99L;

    when(accountRepository.findById(accountId))
        .thenReturn(Optional.empty());

    // When / Then
    assertThrows(
        AccountNotFoundException.class,
        () -> accountService.findById(accountId)
    );

    verify(accountRepository).findById(accountId);
    verify(accountMapper, never()).toResponse(
        org.mockito.ArgumentMatchers.any(Account.class)
    );
  }
}