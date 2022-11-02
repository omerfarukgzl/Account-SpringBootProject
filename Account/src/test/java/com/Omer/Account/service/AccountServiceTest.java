package com.Omer.Account.service;

import com.Omer.Account.TestSupport;
import com.Omer.Account.dto.AccountCustomerDto;
import com.Omer.Account.dto.AccountDto;
import com.Omer.Account.dto.CreateAccountRequest;
import com.Omer.Account.dto.TransactionDto;
import com.Omer.Account.dto.converter.AccountDtoConverter;
import com.Omer.Account.exception.CustomerNotFoundException;
import com.Omer.Account.model.Account;
import com.Omer.Account.model.Customer;
import com.Omer.Account.model.Transaction;
import com.Omer.Account.model.TransactionType;
import com.Omer.Account.repository.AccountRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceTest extends TestSupport{

    private AccountService accountService;

    private AccountRepository accountRepository;
    private  CustomerService customerService;
    private AccountDtoConverter converter;

    private final   Customer customer = generateCustomer();
    //private final Customer customer = new Customer("12345", "customer-name", "customer-surname", Set.of());

    @BeforeEach
    public void setUp() throws Exception {

        accountRepository = Mockito.mock(AccountRepository.class);
        customerService = Mockito.mock(CustomerService.class);
        converter = Mockito.mock(AccountDtoConverter.class);


        accountService = new AccountService(accountRepository,customerService,converter);

    }

    // hem geçerli bir request param gelmesi hemde ınitial credit > 0 olması senaryosu
    @Test
    public void whenCreateAccountCalledWithValidRequestAndInitialCreditMoreThanZero_itShouldReturnValidAccountDto()//createAccount Geçerli istekle çağırıldığında geçerli accountDto dönmeli senaryosu  / Fonksiyonun kendisinin testi ( parametre olarak geçerli bir istek parametresi alır ve geriye accountDto döner)
    {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("customer-id", new BigDecimal(100.0));

        when(customerService.findCustomerById("customer-id")).thenReturn(customer);

        Account account = generateAccount(customer);

        Transaction transaction = new Transaction(
                "transaction_id",
                TransactionType.INITIAL,
                createAccountRequest.getInitialCredit(),
                LocalDateTime.now(),
                account);

         account.getTransactions().add(transaction);
        // AccountCustomerDto accountCustomerDto = generateAccountCustomerDto(customer);

        AccountCustomerDto accountCustomerDto = new AccountCustomerDto("customer-id",
                "customer-name",
                "customer-surname");

        TransactionDto transactionDto = new TransactionDto("transaction_id", TransactionType.INITIAL,  new BigDecimal(100.0), getLocalDateTime()); // account Dto transaction Dto ya sahip oludğu için oluşturduk
        AccountDto accountDtoExpected = new AccountDto("account_id", new BigDecimal(100.0), getLocalDateTime(), accountCustomerDto, Set.of(transactionDto));


         when(accountRepository.save(any(Account.class))).thenReturn(account);
         when(converter.convert(account)).thenReturn(accountDtoExpected);
        System.out.println(account.toString());
         AccountDto result = accountService.createAccount(createAccountRequest); // null Dönüyor hata var!!

        //System.out.println(result.toString());
        System.out.println(accountDtoExpected.toString());
        System.out.println(account.toString());
        System.out.println(result.toString());
        assertEquals(result,accountDtoExpected);


    }

}