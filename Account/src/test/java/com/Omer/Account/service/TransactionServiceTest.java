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

        transactionService= new TransactionService(transactionRepository,transactionDtoConverter,accountService);
    }


    @Test
    public void whenCreateTransactionCalledWithCreateTransactionRequest_itShouldReturnValidTransactionDto()
    {
        CreateTransactionRequest createTransactionRequest= new CreateTransactionRequest("account-id",new BigDecimal(100.0));

        Customer customer = generateCustomer();
        Account account = new Account("account-id",new BigDecimal(100.0),getLocalDateTime(),customer,new HashSet<>());
         when(accountService.getAccount("account-id")).thenReturn(account);

        Transaction transaction = new Transaction("transaction-id", TransactionType.INITIAL,new BigDecimal(100.0),getLocalDateTime(),account);

        TransactionDto transactionDto = new TransactionDto("transaction-id",TransactionType.INITIAL,new BigDecimal(100.0),getLocalDateTime());

        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(transactionDtoConverter.convert(transaction)).thenReturn(transactionDto);

        TransactionDto result = transactionService.createTransaction(createTransactionRequest);

        assertEquals(result,transactionDto);


    }



}