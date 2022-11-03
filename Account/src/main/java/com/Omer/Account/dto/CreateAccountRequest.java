package com.Omer.Account.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountRequest {

    @NotBlank()
    private String customerId;
    @Min(0)
    private BigDecimal initialCredit;


}
