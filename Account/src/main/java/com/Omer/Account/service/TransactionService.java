package com.Omer.Account.service;

import com.Omer.Account.model.Account;
import com.Omer.Account.model.Transaction;
import com.Omer.Account.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    protected Transaction createTransaction(final Account account, BigDecimal amaount)
    {
        Transaction transaction= transactionRepository.save(new Transaction(account,amaount, LocalDateTime.now()));

        return  transaction;
    }


}
