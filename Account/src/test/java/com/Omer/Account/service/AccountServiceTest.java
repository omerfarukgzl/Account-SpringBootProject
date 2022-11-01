package com.Omer.Account.service;

import com.Omer.Account.dto.converter.AccountDtoConverter;
import com.Omer.Account.repository.AccountRepository;

import static org.junit.Assert.*;

public class AccountServiceTest {

    private AccountService accountService;

    private  AccountRepository accountRepository;
    private  CustomerService customerService;
    private  AccountDtoConverter converter;

    @org.junit.Before
    public void setUp() throws Exception {
    }
}