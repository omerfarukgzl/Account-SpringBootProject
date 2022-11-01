package com.Omer.Account.model;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Transaction")
@Getter
@Setter
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


    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="account_id", nullable=false)// foreign key
    private Account account;


    public Transaction(Account account,BigDecimal amaount,LocalDateTime localDateTime)
    {
        this.id = null;
        this.account=account;
        this.amaount=amaount;
        this.transactionType = TransactionType.INITIAL;
        this.localDateTime=localDateTime;

    }


}
