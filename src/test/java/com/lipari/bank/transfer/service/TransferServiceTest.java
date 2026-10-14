package com.lipari.bank.transfer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.lipari.bank.account.entity.Account;
import com.lipari.bank.account.repository.AccountRepository;
import com.lipari.bank.shared.exception.AccountNotFoundException;
import com.lipari.bank.shared.exception.InsufficientFundsException;
import com.lipari.bank.transfer.dto.TransferRequest;
import com.lipari.bank.transfer.entity.Transfer;
import com.lipari.bank.transfer.entity.TransferStatus;
import com.lipari.bank.transfer.repository.TransferRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransferRepository transferRepository;

    @InjectMocks
    private TransferService transferService;

    private static final String FROM_IBAN = "IT000000000000000000000001";
    private static final String TO_IBAN = "IT000000000000000000000002";

    private Account createAccount(String iban, String balance) {
        Account account = new Account();
        account.setIban(iban);
        account.setBalance(new BigDecimal(balance));
        return account;
    }

    @Test
    void executeTransfer_withSufficientBalance_updatesBalancesAndSavesTransfer() {
        // Arrange
        Account fromAccount = createAccount(FROM_IBAN, "1000.00");
        Account toAccount = createAccount(TO_IBAN, "500.00");

        TransferRequest request = new TransferRequest(FROM_IBAN, TO_IBAN, new BigDecimal("200.00"), "Rent payment");

        when(accountRepository.findByIban(FROM_IBAN)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByIban(TO_IBAN)).thenReturn(Optional.of(toAccount));
        when(transferRepository.save(any(Transfer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        transferService.executeTransfer(request);

        // Assert
        assertEquals(new BigDecimal("800.00"), fromAccount.getBalance());
        assertEquals(new BigDecimal("700.00"), toAccount.getBalance());

        ArgumentCaptor<Transfer> transferCaptor = ArgumentCaptor.forClass(Transfer.class);

        verify(transferRepository).save(transferCaptor.capture());

        Transfer savedTransfer = transferCaptor.getValue();

        assertEquals(fromAccount, savedTransfer.getFromAccount());
        assertEquals(toAccount, savedTransfer.getToAccount());
        assertEquals(new BigDecimal("200.00"), savedTransfer.getAmount());
        assertEquals("Rent payment", savedTransfer.getDescription());
        assertEquals(TransferStatus.COMPLETED, savedTransfer.getStatus());
        assertNotNull(savedTransfer.getExecutedAt());

        verify(accountRepository).findByIban(FROM_IBAN);
        verify(accountRepository).findByIban(TO_IBAN);
    }

    @Test
    void executeTransfer_withInsufficientBalance_throwsExceptionAndDoesNotSave() {
        // Arrange
        Account fromAccount = createAccount(FROM_IBAN, "100.00");
        Account toAccount = createAccount(TO_IBAN, "500.00");

        TransferRequest request = new TransferRequest(FROM_IBAN, TO_IBAN, new BigDecimal("200.00"), "Rent payment");

        when(accountRepository.findByIban(FROM_IBAN)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByIban(TO_IBAN)).thenReturn(Optional.of(toAccount));

        // Act
        assertThrows(InsufficientFundsException.class, () -> transferService.executeTransfer(request));

        // Assert
        assertEquals(new BigDecimal("100.00"), fromAccount.getBalance());
        assertEquals(new BigDecimal("500.00"), toAccount.getBalance());

        verify(transferRepository, never()).save(any(Transfer.class));
    }

    @Test
    void executeTransfer_withMissingSourceAccount_throwsExceptionAndDoesNotSave() {
        // Arrange
        TransferRequest request = new TransferRequest(FROM_IBAN, TO_IBAN, new BigDecimal("200.00"), "Rent payment");

        when(accountRepository.findByIban(FROM_IBAN)).thenReturn(Optional.empty());

        // Act
        assertThrows(AccountNotFoundException.class, () -> transferService.executeTransfer(request));

        // Assert
        verify(accountRepository).findByIban(FROM_IBAN);
        verify(accountRepository, never()).findByIban(TO_IBAN);
        verify(transferRepository, never()).save(any(Transfer.class));
    }
}
