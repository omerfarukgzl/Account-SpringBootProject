package com.Omer.Account.dto.converter;

import com.Omer.Account.dto.CustomerAccountDto;
import com.Omer.Account.model.Account;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CustomerAccountDtoConverter {

    private final TransactionDtoConverter transactionDtoConverter;

    public CustomerAccountDtoConverter(TransactionDtoConverter converter) {

        this.transactionDtoConverter = converter;
    }

    public CustomerAccountDto convert(Account from) {
        return new CustomerAccountDto(
                Objects.requireNonNull(from.getId()),
                from.getBalance(),
                LocalDateTime.now(),
                from.getTransactions().stream().map(transactionDtoConverter::convert).collect(Collectors.toSet()));
    }
}
