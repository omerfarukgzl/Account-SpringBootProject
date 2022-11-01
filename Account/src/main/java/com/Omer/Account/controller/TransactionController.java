package com.Omer.Account.controller;

import com.Omer.Account.dto.CreateAccountRequest;
import com.Omer.Account.dto.CreateTransactionRequest;
import com.Omer.Account.dto.CustomerDto;
import com.Omer.Account.dto.TransactionDto;
import com.Omer.Account.service.CustomerService;
import com.Omer.Account.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/v1/trs/")
public class TransactionController {

    private final TransactionService transactionService;


    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    @PostMapping
    public ResponseEntity<TransactionDto> CreateTransaction(@RequestBody CreateTransactionRequest createTransactionRequest)
    {
       TransactionDto transactionDto=transactionService.createTransaction(createTransactionRequest);


        return  ResponseEntity.ok(transactionDto);
    }
}
