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
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceTest extends TestSupport{

    private AccountService accountService;

    private AccountRepository accountRepository;
    private  CustomerService customerService;
    private AccountDtoConverter converter;

    private final   Customer customer = generateCustomer();

    private final AccountCustomerDto accountCustomerDto = new AccountCustomerDto("customer-id",
            "customer-name",
            "customer-surname");
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

        Account account = generateAccount(customer,new BigDecimal(100.0));

        Transaction transaction = new Transaction(
                "transaction_id",
                TransactionType.INITIAL,
                createAccountRequest.getInitialCredit(),
                LocalDateTime.now(),
                account);

         account.getTransactions().add(transaction);
        // AccountCustomerDto accountCustomerDto = generateAccountCustomerDto(customer);

        TransactionDto transactionDto = new TransactionDto("transaction_id", TransactionType.INITIAL,  new BigDecimal(100.0), getLocalDateTime()); // account Dto transaction Dto ya sahip oludğu için oluşturduk
        AccountDto accountDtoExpected = new AccountDto("account_id2", new BigDecimal(100.0), getLocalDateTime(), accountCustomerDto, Set.of(transactionDto));

         when(accountRepository.save(ArgumentMatchers.any(Account.class))).thenReturn(account); // Gerçekten Db ye gitmediğimiz için account repository mocklamamız gerekir. Bunun anlamı account repository herhangi bir Account nesnesni classı ile çağırılırsa account dönsün

         when(converter.convert(ArgumentMatchers.any(Account.class))).thenReturn(accountDtoExpected);

         AccountDto result = accountService.createAccount(createAccountRequest); // null Dönüyor hata var!!

       /* System.out.println(account.getId()+account.toString());
        System.out.println(accountDtoExpected.getId() +accountDtoExpected.getBalance()+accountDtoExpected.toString());
        System.out.println(result.getId() + result.getBalance() + result.toString());*/
        assertEquals(result,accountDtoExpected);


    }


    // hem geçerli bir request param gelmesi hemde ınitial credit = 0 olması senaryosu (içerideki if e girmeme durumu)
    @Test()
    public void whenCreateAccountCalledWithValidRequestAndInitialCreditIsZero_itShouldReturnValidAccountDto() throws Exception//
    {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("customer-id", new BigDecimal(100.0));

        when(customerService.findCustomerById("customer-id")).thenReturn(customer);

        Account account = generateAccount(customer,new BigDecimal(0.0));

        AccountDto accountDtoExpected = new AccountDto("account_id2", new BigDecimal(0.0), getLocalDateTime(), accountCustomerDto,Set.of());

        when(accountRepository.save(any(Account.class))).thenReturn(account);

        when(converter.convert(account)).thenReturn(accountDtoExpected);

        AccountDto result = accountService.createAccount(createAccountRequest); // null Dönüyor hata var!!

        //System.out.println(result.toString());
        System.out.println(accountDtoExpected.toString());
        System.out.println(account.toString());
        System.out.println(result.toString());
        assertEquals(result,accountDtoExpected);


    }


    @Test
    public void whenCreateAccountCalledWithValidRequest_but_CustomerIdDoesNotExist_shouldThrowCustomerNotFoundException()
    {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("customer-id", new BigDecimal(100.0));

        when(customerService.findCustomerById("customer-id")).thenThrow(new CustomerNotFoundException("test-exception customer not found"));

        assertThrows(CustomerNotFoundException.class,
                () -> accountService.createAccount(createAccountRequest));

        verify(customerService).findCustomerById(createAccountRequest.getCustomerId());
        verifyNoInteractions(accountRepository);
        verifyNoInteractions(converter);


    }




}