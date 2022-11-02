package com.Omer.Account.service;

import com.Omer.Account.dto.CustomerDto;
import com.Omer.Account.dto.converter.CustomerDtoConverter;
import com.Omer.Account.exception.CustomerNotFoundException;
import com.Omer.Account.model.Customer;
import com.Omer.Account.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerDtoConverter customerDtoConverter;


    public CustomerService(CustomerRepository customerRepository, CustomerDtoConverter customerDtoConverter) {
        this.customerRepository = customerRepository;
        this.customerDtoConverter = customerDtoConverter;
    }


    protected Customer findCustomerById(String id)
    {
        Customer customer= customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer could not find by id: " + id));
        return customer;
    }

    public CustomerDto getCustomerInformation(String customerId)
    {
        Customer customer= customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer could not find by id: " + customerId));
        CustomerDto customerDto = customerDtoConverter.convertToCustomerDto(customer);
        return customerDto;

    }

    public List<CustomerDto> getAllCustomer()
    {
        List<Customer> customers=customerRepository.findAll();



        //foreach ile List converter
    /*
        List<CustomerDto> customerDtoList=new ArrayList<>();
        for (Customer customer:customers) {

            customerDtoList.add(customerDtoConverter.convertToCustomerDto(customer));
        }
    */

        //stream ile List converter

        List<CustomerDto> customerDtoList =  customers
                .stream()
                .map(customerDtoConverter::convertToCustomerDto)
                .collect(Collectors.toList());

        return customerDtoList;
    }





}
