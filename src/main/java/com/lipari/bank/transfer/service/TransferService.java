package com.lipari.bank.transfer.service;

import com.lipari.bank.account.entity.Account;
import com.lipari.bank.account.repository.AccountRepository;
import com.lipari.bank.shared.exception.AccountNotFoundException;
import com.lipari.bank.shared.exception.InsufficientFundsException;
import com.lipari.bank.transfer.dto.TransferRequest;
import com.lipari.bank.transfer.dto.TransferResponse;
import com.lipari.bank.transfer.entity.Transfer;
import com.lipari.bank.transfer.entity.TransferStatus;
import com.lipari.bank.transfer.repository.TransferRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    @Transactional
    public TransferResponse executeTransfer(TransferRequest request) {
        Account fromAccount = accountRepository
                .findByIban(request.fromIban())
                .orElseThrow(() -> new AccountNotFoundException(request.fromIban()));

        Account toAccount = accountRepository
                .findByIban(request.toIban())
                .orElseThrow(() -> new AccountNotFoundException(request.toIban()));

        if (fromAccount.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException(fromAccount.getIban(), request.amount(), fromAccount.getBalance());
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(request.amount()));

        toAccount.setBalance(toAccount.getBalance().add(request.amount()));

        Transfer transfer = new Transfer();
        transfer.setFromAccount(fromAccount);
        transfer.setToAccount(toAccount);
        transfer.setAmount(request.amount());
        transfer.setDescription(request.description());
        transfer.setExecutedAt(LocalDateTime.now());
        transfer.setStatus(TransferStatus.COMPLETED);

        Transfer saved = transferRepository.save(transfer);

        return new TransferResponse(
                saved.getId(),
                saved.getFromAccount().getIban(),
                saved.getToAccount().getIban(),
                saved.getAmount(),
                saved.getDescription(),
                saved.getExecutedAt(),
                saved.getStatus());
    }
}
