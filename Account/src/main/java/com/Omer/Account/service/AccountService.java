package com.Omer.Account.service;

import com.Omer.Account.dto.AccountDto;
import com.Omer.Account.dto.CreateAccountRequest;
import com.Omer.Account.dto.CreateTransactionRequest;
import com.Omer.Account.dto.converter.AccountDtoConverter;
import com.Omer.Account.model.Account;
import com.Omer.Account.model.Customer;
import com.Omer.Account.model.Transaction;
import com.Omer.Account.repository.AccountRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AccountService {


    private final AccountRepository accountRepository;
    private final CustomerService customerService;
    private final AccountDtoConverter converter;
    private final TransactionService transactionService;
    private final KafkaTemplate<String, String> kafkaTemplate;


    public AccountService(AccountRepository accountRepository, CustomerService customerService, AccountDtoConverter converter, TransactionService transactionService, KafkaTemplate<String, String> kafkaTemplate) {
        this.accountRepository = accountRepository;
        this.customerService = customerService;
        this.converter = converter;
        this.transactionService = transactionService;
        this.kafkaTemplate = kafkaTemplate;

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

            account.getTransactions().add(transaction);// transaction repository.save burda denemiyeceği için add yaparak account u kaydettik böylece transaction da db ye eklendi.
        }

        AccountDto accountDto= converter.convert(accountRepository.save(account));
                    String notificationMessage = "Dear customer %s \n Your account create transaction has been succeed. Your new balance is %s";
                    System.out.println("Account (" + account.getId() +") new account balance: " + account.getBalance());
                    String senderMessage = String.format(notificationMessage, account.getId(), account.getBalance());
                    kafkaTemplate.send("transfer-notification",  senderMessage);


        return accountDto;

    }

    public AccountDto createTransactionAndGetAccount(CreateTransactionRequest createTransactionRequest)
    {
        Account account =accountRepository.findById(createTransactionRequest.getAccountId()).get();

        Transaction transaction = transactionService.createTransaction(account,createTransactionRequest.getAmaount());
        account.getTransactions().add(transaction);
        AccountDto accountDto= converter.convert(accountRepository.save(account));
        return accountDto;
    }
}
