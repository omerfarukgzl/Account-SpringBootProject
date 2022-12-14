package com.Omer.Account.service;

import com.Omer.Account.TestSupport;
import com.Omer.Account.dto.*;
import com.Omer.Account.dto.converter.AccountDtoConverter;
import com.Omer.Account.exception.CustomerNotFoundException;
import com.Omer.Account.model.Account;
import com.Omer.Account.model.Customer;
import com.Omer.Account.model.Transaction;
import com.Omer.Account.model.TransactionType;
import com.Omer.Account.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AccountServiceTest extends TestSupport{

    private AccountService accountService;

    private AccountRepository accountRepository;
    private  CustomerService customerService;
    private AccountDtoConverter converter;
    private TransactionService transactionService;

    private KafkaTemplate<String, String> kafkaTemplate;

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
        transactionService=Mockito.mock(TransactionService.class);
        kafkaTemplate = Mockito.mock(KafkaTemplate.class);

        accountService = new AccountService(accountRepository,customerService,converter, transactionService, kafkaTemplate);

    }

    // hem ge??erli bir request param gelmesi hemde ??nitial credit > 0 olmas?? senaryosu
    @Test
    public void whenCreateAccountCalledWithValidRequestAndInitialCreditMoreThanZero_itShouldReturnValidAccountDto()//createAccount Ge??erli istekle ??a????r??ld??????nda ge??erli accountDto d??nmeli senaryosu  / Fonksiyonun kendisinin testi ( parametre olarak ge??erli bir istek parametresi al??r ve geriye accountDto d??ner)
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

        TransactionDto transactionDto = new TransactionDto("transaction_id", TransactionType.INITIAL,  new BigDecimal(100.0), getLocalDateTime()); // account Dto transaction Dto ya sahip olud??u i??in olu??turduk
        AccountDto accountDtoExpected = new AccountDto("account_id", new BigDecimal(100.0), getLocalDateTime(), accountCustomerDto, Set.of(transactionDto));

         when(accountRepository.save(any(Account.class))).thenReturn(account); // Ger??ekten Db ye gitmedi??imiz i??in account repository mocklamam??z gerekir. Bunun anlam?? account repository herhangi bir Account nesnesni class?? ile ??a????r??l??rsa account d??ns??n

         when(converter.convert(any(Account.class))).thenReturn(accountDtoExpected);

         AccountDto result = accountService.createAccount(createAccountRequest); //

       /* System.out.println(account.getId()+account.toString());
        System.out.println(accountDtoExpected.getId() +accountDtoExpected.getBalance()+accountDtoExpected.toString());
        System.out.println(result.getId() + result.getBalance() + result.toString());*/
        assertEquals(result,accountDtoExpected);


    }


    // hem ge??erli bir request param gelmesi hemde ??nitial credit = 0 olmas?? senaryosu (i??erideki if e girmeme durumu)
    @Test()
    public void whenCreateAccountCalledWithValidRequestAndInitialCreditIsZero_itShouldReturnValidAccountDto() throws Exception//
    {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("customer-id", new BigDecimal(100.0));

        when(customerService.findCustomerById("customer-id")).thenReturn(customer);

        Account account = generateAccount(customer,new BigDecimal(0.0));

        AccountDto accountDtoExpected = new AccountDto("account_id", new BigDecimal(0.0), getLocalDateTime(), accountCustomerDto,Set.of());

        when(accountRepository.save(any(Account.class))).thenReturn(account);

        when(converter.convert(account)).thenReturn(accountDtoExpected);

        AccountDto result = accountService.createAccount(createAccountRequest); // null D??n??yor hata var!!

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

    @Test
    public void whenCalledCreateTransactionAndGetAccountWithCreateTransactionRequest_itShouldReturnValidAccountDto() throws Exception
    {

        Account account = generateAccount(customer,new BigDecimal(100.0));
        CreateTransactionRequest createTransactionRequest = new CreateTransactionRequest(account.getId(),new BigDecimal(200));

        Transaction transaction = new Transaction(
                "transaction_id",
                TransactionType.INITIAL,
                createTransactionRequest.getAmaount(),
                LocalDateTime.now(),
                account);
        TransactionDto transactionDto = new TransactionDto("transaction_id", TransactionType.INITIAL,  new BigDecimal(100.0), getLocalDateTime()); // account Dto transaction Dto ya sahip olud??u i??in olu??turduk
        AccountDto accountDtoExpected = new AccountDto("account_id", new BigDecimal(100.0), getLocalDateTime(), accountCustomerDto, Set.of(transactionDto));

        when(accountRepository.findById(createTransactionRequest.getAccountId())).thenReturn(Optional.of(account));
        when(transactionService.createTransaction(account,createTransactionRequest.getAmaount())).thenReturn(transaction);


        account.getTransactions().add(transaction);
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(converter.convert((account))).thenReturn(accountDtoExpected);

        AccountDto result = accountService.createTransactionAndGetAccount(createTransactionRequest);

        assertEquals(result,accountDtoExpected);
    }




}