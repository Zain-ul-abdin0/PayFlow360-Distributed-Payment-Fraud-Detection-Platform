package com.payflow360.payment_service.service;

import com.payflow360.payment_service.dto.AccountResponse;
import com.payflow360.payment_service.dto.CreateAccountRequest;
import com.payflow360.payment_service.dto.TransferRequest;
import com.payflow360.payment_service.dto.TransferResponse;
import com.payflow360.payment_service.entity.Account;
import com.payflow360.payment_service.exception.AccountNotFoundException;
import com.payflow360.payment_service.exception.CurrencyMismatchException;
import com.payflow360.payment_service.exception.InsufficientBalanceException;
import com.payflow360.payment_service.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request) {

        if (accountRepository.findByAccountNumber(request.accountNumber()).isPresent()) {
            throw new IllegalArgumentException(
                    "Account already exists: " + request.accountNumber()
            );
        }

        Account account = Account.builder()
                .accountNumber(request.accountNumber())
                .ownerName(request.ownerName())
                .balance(request.initialBalance())
                .currency(request.currency().toUpperCase())
                .version(0L)
                .build();

        Account savedAccount = accountRepository.save(account);

        return toResponse(savedAccount);
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccount(String accountNumber) {

        Account account = accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Account not found: " + accountNumber
                        )
                );

        return toResponse(account);
    }

    private AccountResponse toResponse(Account account) {

        return new AccountResponse(
                account.getAccountNumber(),
                account.getOwnerName(),
                account.getBalance(),
                account.getCurrency()
        );
    }
    @Transactional
    public TransferResponse transfer(TransferRequest request) {

        if (request.sourceAccountNumber()
                .equals(request.destinationAccountNumber())) {

            throw new IllegalArgumentException(
                    "Source and destination accounts must be different"
            );
        }

        Account sourceAccount = accountRepository
                .findByAccountNumber(request.sourceAccountNumber())
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Source account not found: "
                                        + request.sourceAccountNumber()
                        )
                );

        Account destinationAccount = accountRepository
                .findByAccountNumber(request.destinationAccountNumber())
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Destination account not found: "
                                        + request.destinationAccountNumber()
                        )
                );

        if (!sourceAccount.getCurrency()
                .equals(destinationAccount.getCurrency())) {

            throw new CurrencyMismatchException(
                    "Currency mismatch between accounts"
            );
        }

        if (sourceAccount.getBalance()
                .compareTo(request.amount()) < 0) {

            throw new InsufficientBalanceException(
                    "Insufficient balance"
            );
        }

        sourceAccount.setBalance(
                sourceAccount.getBalance()
                        .subtract(request.amount())
        );

        destinationAccount.setBalance(
                destinationAccount.getBalance()
                        .add(request.amount())
        );

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        return new TransferResponse(
                sourceAccount.getAccountNumber(),
                destinationAccount.getAccountNumber(),
                request.amount(),
                sourceAccount.getBalance(),
                destinationAccount.getBalance(),
                "COMPLETED"
        );
    }
}