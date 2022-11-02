package com.Omer.Account.service;

import com.Omer.Account.TestSupport;
import com.Omer.Account.dto.CustomerDto;
import com.Omer.Account.dto.converter.CustomerDtoConverter;
import com.Omer.Account.exception.CustomerNotFoundException;
import com.Omer.Account.model.Customer;
import com.Omer.Account.repository.CustomerRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class CustomerServiceTest extends TestSupport {


    private CustomerService customerService;
    private CustomerRepository customerRepository;
    private CustomerDtoConverter customerDtoConverter;


    private final   Customer customer = generateCustomer();
    private final  CustomerDto customerDto =generateCustomerDto(customer);


    @BeforeEach
    public void setUp() {

        customerRepository=Mockito.mock(CustomerRepository.class);
        customerDtoConverter=Mockito.mock(CustomerDtoConverter.class);
        customerService = new CustomerService(customerRepository,customerDtoConverter);


    }

    @Test
    public void whenGetCustomerInformationWithCustomerIdExist_itShouldReturnValidCustomerDto(){

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(customerDtoConverter.convertToCustomerDto(customer)).thenReturn(customerDto);

        CustomerDto result = customerService.getCustomerInformation(customer.getId());

        assertEquals(result, customerDto);
    }
    @Test
    public void whenGetCustomerInformationWithCustomerIdDoesNotExist_itShouldReturnEmptyCustomerDto(){

        String customerId= customer.getId();
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> customerService.findCustomerById(customerId));
        verifyNoInteractions(customerDtoConverter);

    }
    @Test
    public void whenGetAllCustomer_itShouldReturnCustomerDtoList(){

        List<Customer> customerList = generateCustomerList();

        when(customerRepository.findAll()).thenReturn(customerList);

        List<CustomerDto> customerDtoList = customerList
                .stream()
                .map(customerDtoConverter::convertToCustomerDto)
                .collect(Collectors.toList());

        List<CustomerDto> result = customerService.getAllCustomer();

        assertEquals(result,customerDtoList);

    }

}