package com.Omer.Account.dto;

import com.Omer.Account.model.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private String id;
    private TransactionType transactionType=TransactionType.INITIAL;
    private BigDecimal amaount;
    private LocalDateTime localDateTime;
}
