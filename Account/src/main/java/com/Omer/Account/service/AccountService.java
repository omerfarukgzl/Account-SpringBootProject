package com.Omer.Account.service;

import com.Omer.Account.dto.AccountDto;
import com.Omer.Account.dto.CreateAccountRequest;
import com.Omer.Account.model.Account;
import com.Omer.Account.model.Customer;
import com.Omer.Account.model.Transaction;
import com.Omer.Account.repository.AccountRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;
    private final CustomerService customerService;
    private final ModelMapper modelMapper;

    public AccountService(AccountRepository accountRepository, TransactionService transactionService, CustomerService customerService, ModelMapper modelMapper) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }


    public AccountDto createAccount(CreateAccountRequest createAccountRequest)
    {

        Customer customer = customerService.findCustomerById(createAccountRequest.getCustomerId());

        Account account = new Account(customer,createAccountRequest.getInitalCredit(),LocalDateTime.now());

        if(createAccountRequest.getInitalCredit().compareTo(BigDecimal.ZERO)>0)
        {
            Transaction transaction = transactionService.createTransaction(account,createAccountRequest.getInitalCredit());
            account.getTransactions().add(transaction);
        }

        AccountDto accountDto= modelMapper.map(accountRepository.save(account),AccountDto.class);

        return accountDto;

    }


}
