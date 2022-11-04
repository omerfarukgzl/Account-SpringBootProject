package com.Omer.Account.service;

import com.Omer.Account.dto.CreateTransactionRequest;
import com.Omer.Account.dto.TransactionDto;
import com.Omer.Account.dto.converter.TransactionDtoConverter;
import com.Omer.Account.model.Account;
import com.Omer.Account.model.Transaction;
import com.Omer.Account.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private  final TransactionDtoConverter transactionDtoConverter;


    public TransactionService(TransactionRepository transactionRepository, TransactionDtoConverter transactionDtoConverter) {
        this.transactionRepository = transactionRepository;
        this.transactionDtoConverter = transactionDtoConverter;
    }

    protected Transaction createTransaction(Account account, BigDecimal amount)
    {
        Transaction transaction= new Transaction(account,amount, LocalDateTime.now());
        transactionRepository.save(transaction);
        return transaction;

    }


}
