package com.Omer.Account.model;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "Transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;
    private TransactionType transactionType=TransactionType.INITIAL;
    private BigDecimal amaount;
    private LocalDateTime localDateTime;


    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name="account_id", nullable=false)// foreign key
    private Account account;


    public Transaction(Account account,BigDecimal amaount,LocalDateTime localDateTime)
    {
        this.id = "";
        this.account=account;
        this.amaount=amaount;
        this.transactionType = TransactionType.INITIAL;
        this.localDateTime=localDateTime;

    }

}
