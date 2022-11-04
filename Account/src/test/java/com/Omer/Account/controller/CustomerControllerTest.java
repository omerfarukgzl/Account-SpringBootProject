package com.Omer.Account.controller;

import com.Omer.Account.IntegrationTestSupport;
import com.Omer.Account.dto.AccountDto;
import com.Omer.Account.dto.CustomerDto;
import com.Omer.Account.model.Account;
import com.Omer.Account.model.Customer;
import com.Omer.Account.repository.AccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CustomerControllerTest extends IntegrationTestSupport {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void testGetCustomerInformation_WhenIsExistCustomerId_ShoulRetrunCustomerDto() throws Exception
    {

        Customer customer = customerRepository.save(generateCustomer());
        accountService.createAccount(generateCreateAccountRequest(customer.getId(), 100));

        CustomerDto expectedCustomerDto = customerDtoConverter.convertToCustomerDto(
                customerRepository.findById(
                        Objects.requireNonNull(customer.getId())).get());

            this.mockMvc.perform(get(CUSTOMER_API_ENDPOINT + customer.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(expectedCustomerDto)));


    }

    @Test
    public void testGetCustomerInformation_WhenIsNotExistCustomerId_ShoulRetrunIsNotFound() throws Exception
    {
        String id="NotExistId";
        this.mockMvc.perform(get(CUSTOMER_API_ENDPOINT + id))
                .andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    public void testGetCustomerAll_ShoulRetrunCustomerDtoList() throws Exception
    {

        List<Customer> customers=customerRepository.findAll();

        List<CustomerDto> customerDtoList =
                customers.stream()
                .map(customerDtoConverter::convertToCustomerDto)
                .collect(Collectors.toList());

        this.mockMvc.perform(get(CUSTOMER_ALL_API_ENDPOINT))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(customerDtoList)))
                 .andDo(print());
    }
}









