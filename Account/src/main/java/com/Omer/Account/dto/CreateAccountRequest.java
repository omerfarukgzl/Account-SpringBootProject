package com.Omer.Account.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountRequest {

    private String customerId;
    private BigDecimal initialCredit;

}
