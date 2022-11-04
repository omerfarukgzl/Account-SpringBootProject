package com.Omer.Account.service;

import com.Omer.Account.TestSupport;
import com.Omer.Account.dto.CreateTransactionRequest;
import com.Omer.Account.dto.TransactionDto;
import com.Omer.Account.dto.converter.TransactionDtoConverter;
import com.Omer.Account.model.Account;
import com.Omer.Account.model.Customer;
import com.Omer.Account.model.Transaction;
import com.Omer.Account.model.TransactionType;
import com.Omer.Account.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TransactionServiceTest extends TestSupport {

    private TransactionService transactionService;
    private TransactionRepository transactionRepository;
    private TransactionDtoConverter transactionDtoConverter;
    private AccountService accountService;
    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        transactionDtoConverter= mock(TransactionDtoConverter.class);
        accountService =mock(AccountService.class);

        transactionService= new TransactionService(transactionRepository,transactionDtoConverter);
    }


    @Test
    public void whenCreateTransactionCalledWithCreateTransactionRequest_itShouldReturnValidTransactionDto()
    {

        /*protected metodların testi olurmu ? gerek varmı ?*/

    }



}