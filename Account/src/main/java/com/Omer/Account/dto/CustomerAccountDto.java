package com.Omer.Account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAccountDto {
    private String id;
    private BigDecimal balance;
    private LocalDateTime creationDate;
    private Set<TransactionDto> transactions;

}
