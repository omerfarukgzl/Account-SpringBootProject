package com.Omer.Account.service;

import com.Omer.Account.dto.CreateTransactionRequest;
import com.Omer.Account.dto.TransactionDto;
import com.Omer.Account.dto.converter.TransactionDtoConverter;
import com.Omer.Account.model.Account;
import com.Omer.Account.model.Transaction;
import com.Omer.Account.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private  final TransactionDtoConverter transactionDtoConverter;
    private final AccountService accountService;

    public TransactionService(TransactionRepository transactionRepository, TransactionDtoConverter transactionDtoConverter, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.transactionDtoConverter = transactionDtoConverter;
        //this.accountService = accountService;
        this.accountService = accountService;
    }

    public TransactionDto createTransaction(CreateTransactionRequest createTransactionRequest)
    {


        Account account = accountService.getAccount(createTransactionRequest.getAccountId());
        Transaction transaction= new Transaction(account,createTransactionRequest.getAmaount(), LocalDateTime.now());
        //Account.getTransaction().add() demeye gerek yok çünkü transaction save edildiğinde account a da değişiklik yansıyacaktır.
        TransactionDto transactionDto = transactionDtoConverter.convert(transactionRepository.save(transaction));

        return transactionDto;

    }


}
