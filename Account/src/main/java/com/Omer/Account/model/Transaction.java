package com.Omer.Account.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Transaction")
@Data
@NoArgsConstructor
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


    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="account_id", nullable=false)// foreign key
    private Account account;


    public Transaction(Account account,BigDecimal amaount,LocalDateTime localDateTime)
    {
        this.account=account;
        this.amaount=amaount;
        this.account=account;
        this.localDateTime=localDateTime;

    }


}
