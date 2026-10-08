package com.payflow360.payment_service.controller;


import com.payflow360.payment_service.dto.AccountResponse;
import com.payflow360.payment_service.dto.CreateAccountRequest;
import com.payflow360.payment_service.dto.TransferRequest;
import com.payflow360.payment_service.dto.TransferResponse;
import com.payflow360.payment_service.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse createAccount(
            @Valid @RequestBody CreateAccountRequest request) {

        return accountService.createAccount(request);
    }

    @GetMapping("/{accountNumber}")
    public AccountResponse getAccount(
            @PathVariable String accountNumber) {

        return accountService.getAccount(accountNumber);
    }
    @PostMapping("/transfer")
    public TransferResponse transfer(
            @Valid @RequestBody TransferRequest request) {

        return accountService.transfer(request);
    }
}