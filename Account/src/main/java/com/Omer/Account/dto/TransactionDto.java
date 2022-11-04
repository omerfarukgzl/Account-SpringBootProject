package com.Omer.Account.dto;

import com.Omer.Account.model.TransactionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class TransactionDto {
    private String id;
    private TransactionType transactionType=TransactionType.INITIAL;
    private BigDecimal amaount;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("date")
    private LocalDateTime localDateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionDto that = (TransactionDto) o;
        return Objects.equals(id, that.id) && transactionType == that.transactionType && Objects.equals(amaount, that.amaount) && Objects.equals(localDateTime, that.localDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, transactionType, amaount, localDateTime);
    }
}
