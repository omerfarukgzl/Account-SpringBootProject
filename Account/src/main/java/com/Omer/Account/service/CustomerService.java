package com.Omer.Account.service;

import com.Omer.Account.exception.CustomerNotFoundException;
import com.Omer.Account.model.Customer;
import com.Omer.Account.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    protected Customer findCustomerById(String id)
    {
        Customer customer= customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer could not find by id: " + id));
        return customer;
    }



}
