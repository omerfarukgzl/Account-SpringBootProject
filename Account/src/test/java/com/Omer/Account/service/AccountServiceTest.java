package com.Omer.Account.service;

import com.Omer.Account.dto.CreateAccountRequest;
import com.Omer.Account.dto.converter.AccountDtoConverter;
import com.Omer.Account.model.Customer;
import com.Omer.Account.repository.AccountRepository;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class AccountServiceTest {

    private AccountService accountService;

    private  AccountRepository accountRepository;
    private  CustomerService customerService;
    private  AccountDtoConverter converter;

    @org.junit.Before
    public void setUp() throws Exception {

        accountRepository = Mockito.mock(AccountRepository.class);
        customerService = Mockito.mock(CustomerService.class);
        converter = Mockito.mock(AccountDtoConverter.class);

        accountService = new AccountService(accountRepository,customerService,converter);
    }

    @Test
    public void whenCreateAccountCalledWithValidRequest_itShouldReturnValidAccountDto()//createAccount Geçerli istekle çağırıldığında geçerli accountDto dönmeli senaryosu  / Fonksiyonun kendisinin testi ( parametre olarak geçerli bir istek parametresi alır ve geriye accountDto döner)
    {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("12345",new BigDecimal(100.0));

        Customer customer = Customer.builder()
                .id("12345")
                .name("Omer")
                .surname("Faruk")
                .build();

        Mockito.when(customerService.findCustomerById("12345")).thenReturn(customer);



    }

}