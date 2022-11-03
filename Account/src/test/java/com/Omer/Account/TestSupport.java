package com.Omer.Account;

import com.Omer.Account.dto.AccountCustomerDto;
import com.Omer.Account.dto.AccountDto;
import com.Omer.Account.dto.CreateAccountRequest;
import com.Omer.Account.dto.CustomerDto;
import com.Omer.Account.model.Account;
import com.Omer.Account.model.Customer;
import com.Omer.Account.model.Transaction;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestSupport {
    public static final String CUSTOMER_API_ENDPOINT = "/v1/customer/";
    public static final String ACCOUNT_API_ENDPOINT = "/v1/account/";

    public Instant getCurrentInstant() {
        String instantExpected = "2021-06-15T10:15:30Z";
        Clock clock = Clock.fixed(Instant.parse(instantExpected), Clock.systemDefaultZone().getZone());

        return Instant.now(clock);
    }

    public LocalDateTime getLocalDateTime() {
        return LocalDateTime.ofInstant(getCurrentInstant(), Clock.systemDefaultZone().getZone());
    }

    public Customer generateCustomer() {
        return new Customer("customer-id", "customer-name", "customer-surname", Set.of());
    }
    public CustomerDto generateCustomerDto(Customer customer) {
        return new CustomerDto(customer.getId(), customer.getName(), customer.getSurname(), Set.of());
    }
    public List<Customer> generateCustomerList()
    {
        return IntStream.range(0,5).mapToObj(i->
                new Customer("customer-id"+i, "customer-name"+i, "customer-surname"+i, Set.of())
                ).collect(Collectors.toList());
    }

    public Account generateAccount(Customer customer,BigDecimal balance) {
        return new Account("account_id",balance, getLocalDateTime(), customer, new HashSet<>());
    }
    public AccountCustomerDto generateAccountCustomerDto(Customer customer) {

        return new AccountCustomerDto(customer.getId(), customer.getName(), customer.getSurname());
    }
    public CreateAccountRequest generateCreateAccountRequest(int initialCredit) {
        return generateCreateAccountRequest("customer-id", initialCredit);
    }

    public CreateAccountRequest generateCreateAccountRequest(String customerId, int initialCredit) {
        return new CreateAccountRequest(customerId, new BigDecimal(initialCredit));
    }




}
