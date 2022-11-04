package com.Omer.Account.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class AccountDto {
    private String id;
    private BigDecimal balance;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("date")
    private LocalDateTime creationDate;
    private AccountCustomerDto customer;
    private Set<TransactionDto> transactionDtos;

    @Override
    public String toString() {
        return "AccountDto{" +
                "id='" + id + '\'' +
                ", balance=" + balance +
                ", creationDate=" + creationDate +
                ", customer=" + customer +
                ", transactionDtos=" + transactionDtos +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountDto that = (AccountDto) o;
        return Objects.equals(id, that.id) && Objects.equals(balance, that.balance) && Objects.equals(creationDate, that.creationDate) && Objects.equals(customer, that.customer) && Objects.equals(transactionDtos, that.transactionDtos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, balance, creationDate, customer, transactionDtos);
    }
}
