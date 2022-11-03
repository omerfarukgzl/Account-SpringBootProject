package com.Omer.Account.controller;

import com.Omer.Account.TestSupport;
import com.Omer.Account.dto.AccountDto;
import com.Omer.Account.dto.CreateAccountRequest;
import com.Omer.Account.dto.converter.AccountDtoConverter;
import com.Omer.Account.model.Account;
import com.Omer.Account.model.Customer;
import com.Omer.Account.repository.AccountRepository;
import com.Omer.Account.repository.CustomerRepository;
import com.Omer.Account.service.AccountService;
import com.Omer.Account.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.Matchers;
import org.json.JSONString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "server-port=0",
        "command.line.runner.enabled=false"
})
@RunWith(SpringRunner.class)
@DirtiesContext
public class AccountControllerTest extends TestSupport {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private Supplier<UUID> uuidSupplier;

    @Autowired
    private AccountRepository accountRepository;//account controller da account service. create kullaıldığı için accoun service 'in ihtiyacı olan nesneleeri oluşturuyoruz.


    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountDtoConverter accountDtoConverter;

    private AccountService accountService=new AccountService(accountRepository,customerService,accountDtoConverter);

    private static final UUID uuId=UUID.randomUUID();//db lerde uuıd olan id lerimizi test etmek için yazdık

    @BeforeEach
    public void Setup()
    {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS,false);



    }



    @Test
    public void testCreateAccount_whenCustomerIdExist_ItShouldCreateAccountAndReturnAccountDto () throws Exception {
        Customer customer = customerRepository.save(new Customer("", "Omer", "Faruk", new HashSet<>()));
        CreateAccountRequest createAccountRequest = new CreateAccountRequest(customer.getId(), new BigDecimal(100.0));

        this.mockMvc.perform(post(ACCOUNT_API_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(createAccountRequest)))
                        .andExpect(status().is2xxSuccessful())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.id", notNullValue()))
                        .andExpect(jsonPath("$.balance", is(100)))
                        .andExpect(jsonPath("$.customer.id", is(customer.getId())))
                        .andExpect(jsonPath("$.customer.name", is(customer.getName())))
                        .andExpect(jsonPath("$.customer.surname", is(customer.getSurname())))
                        ;


    }
}

