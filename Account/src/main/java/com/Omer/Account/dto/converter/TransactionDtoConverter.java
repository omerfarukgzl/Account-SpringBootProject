package com.Omer.Account.dto.converter;


import com.Omer.Account.dto.TransactionDto;
import com.Omer.Account.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionDtoConverter {

    public TransactionDto convert(Transaction from) {
        return new TransactionDto(from.getId(),
                from.getTransactionType(),
                from.getAmaount(),
                from.getLocalDateTime());
    }
}