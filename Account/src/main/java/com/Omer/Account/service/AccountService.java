package com.Omer.Account.service;

import com.Omer.Account.dto.AccountDto;
import com.Omer.Account.dto.CreateAccountRequest;
import com.Omer.Account.dto.converter.AccountDtoConverter;
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
    private final AccountDtoConverter converter;

    public AccountService(AccountRepository accountRepository, TransactionService transactionService, CustomerService customerService, ModelMapper modelMapper, AccountDtoConverter converter) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
        this.customerService = customerService;
        this.modelMapper = modelMapper;
        this.converter = converter;
    }


    public AccountDto createAccount(CreateAccountRequest createAccountRequest)
    {

        Customer customer = customerService.findCustomerById(createAccountRequest.getCustomerId());

        Account account = new Account(customer,createAccountRequest.getInitialCredit(),LocalDateTime.now());

        if(createAccountRequest.getInitialCredit().compareTo(BigDecimal.ZERO)>0)
        {
            //Transaction transaction = transactionService.createTransaction(account,createAccountRequest.getInitialCredit());

            Transaction transaction = new Transaction(account,
                    createAccountRequest.getInitialCredit(),
                    LocalDateTime.now()
                     );

            account.getTransactions().add(transaction);
        }

        AccountDto accountDto= converter.convert(accountRepository.save(account));


        return accountDto;

    }


}
