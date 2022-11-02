package com.Omer.Account.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

public class CreateAccountRequest {

    private String customerId;
    private BigDecimal initialCredit;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateAccountRequest that = (CreateAccountRequest) o;
        return Objects.equals(customerId, that.customerId) && Objects.equals(initialCredit, that.initialCredit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, initialCredit);
    }
}
