package com.Omer.Account.controller;

import com.Omer.Account.dto.CustomerDto;
import com.Omer.Account.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/customer/")
public class CustomerController {


    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("{customerId}")
    public ResponseEntity<CustomerDto> getCustomerInformation(@PathVariable String customerId)
    {
        CustomerDto customerDto = customerService.getCustomerInformation(customerId);

        return ResponseEntity.ok(customerDto);
    }


    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomer());
    }



}
