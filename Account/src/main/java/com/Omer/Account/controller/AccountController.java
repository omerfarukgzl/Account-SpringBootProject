package com.Omer.Account.controller;

import com.Omer.Account.dto.AccountDto;
import com.Omer.Account.dto.CreateAccountRequest;
import com.Omer.Account.dto.CreateTransactionRequest;
import com.Omer.Account.model.Customer;
import com.Omer.Account.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/account")
public class AccountController {


private final AccountService accountService;


    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody CreateAccountRequest createAccountRequest)
    {
        AccountDto accountDto =accountService.createAccount(createAccountRequest);
        return ResponseEntity.ok(accountDto);
    }

    @PostMapping("/trs")
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody CreateTransactionRequest createTransactionRequest)
    {
        AccountDto accountDto =accountService.createTransactionAndGetAccount(createTransactionRequest);
        return ResponseEntity.ok(accountDto);
    }





}
