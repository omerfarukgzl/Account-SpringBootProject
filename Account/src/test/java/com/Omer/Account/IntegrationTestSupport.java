package com.Omer.Account;

import com.Omer.Account.dto.converter.CustomerDtoConverter;
import com.Omer.Account.repository.CustomerRepository;
import com.Omer.Account.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public class IntegrationTestSupport extends TestSupport{
    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public CustomerRepository customerRepository;

    @Autowired
    public AccountService accountService;

    @Autowired
    public CustomerDtoConverter converter;

    public final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {

    }
}
