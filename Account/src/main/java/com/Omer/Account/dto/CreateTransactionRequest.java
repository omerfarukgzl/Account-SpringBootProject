package com.Omer.Account.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CreateTransactionRequest {
    private String accountId;
    private BigDecimal amaount;

}
