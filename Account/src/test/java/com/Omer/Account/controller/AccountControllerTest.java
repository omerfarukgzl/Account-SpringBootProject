package com.Omer.Account.controller;

import com.Omer.Account.IntegrationTestSupport;
import com.Omer.Account.dto.CreateAccountRequest;
import com.Omer.Account.model.Customer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.test.context.junit4.SpringRunner;


import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AccountControllerTest extends IntegrationTestSupport {


    @Test
    public void testCreateAccount_whenCustomerIdExist_ItShouldCreateAccountAndReturnAccountDto () throws Exception {
        Customer customer = customerRepository.save(generateCustomer());
        CreateAccountRequest createAccountRequest = new CreateAccountRequest(customer.getId(), new BigDecimal(100.0));

        this.mockMvc.perform(post(ACCOUNT_API_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(createAccountRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))// veritabnına kayıtlı olan id yi getirecektir .
                .andExpect(jsonPath("$.balance", is(100)))//AccountDto içerisindeki  balance verilerini getir
                .andExpect(jsonPath("$.customer.id", is(customer.getId())))//AccountDto içerisindeki customer içerisindeki customer id verisini getir
                .andExpect(jsonPath("$.customer.name", is(customer.getName())))//AccountDto içerisindeki customer içerisindeki customer name getir
                .andExpect(jsonPath("$.customer.surname", is(customer.getSurname()))) //AccountDto içerisindeki customer içerisindeki customer surname getir
                .andExpect(jsonPath("$.transactionDtos", hasSize(1))) // AccountDto içerisindeki transactionDtos verileri getir
                .andDo(print()) // dönen sonucu ekranda göster
                ;
    }
    @Test
    public void testCreateAccount_whenCustomerIdDoesNotExit_shouldReturn404NotFound() throws Exception {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("id", new BigDecimal(100.0));

        this.mockMvc.perform(post(ACCOUNT_API_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(createAccountRequest)))
                .andExpect(status().isNotFound())
                .andDo(print()); // dönen sonucu ekranda göster;

    }

    @Test
    public void testCreateAccount_whenCustomerIsNotValid_shouldReturn404NotFound() throws Exception {
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("isNotValidId", new BigDecimal(100.0));

        this.mockMvc.perform(post(ACCOUNT_API_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(createAccountRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print()); // dönen sonucu ekranda göster;

    }

    @Test
    public void testCreateAccount_whenCustomerIdExist_ButInitialCreditLessThanZero_ItShouldReturn400BadRequest () throws Exception {

        CreateAccountRequest createAccountRequest = new CreateAccountRequest("id", new BigDecimal(-100));

        this.mockMvc.perform(post(ACCOUNT_API_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(createAccountRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print()); // dönen sonucu ekranda göster;




    }



}

