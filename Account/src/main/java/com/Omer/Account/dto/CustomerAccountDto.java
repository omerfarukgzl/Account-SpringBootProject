package com.Omer.Account.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CustomerAccountDto {
    private String id;
    private BigDecimal balance;
    private LocalDateTime creationDate;
    private Set<TransactionDto> transactions;

}
