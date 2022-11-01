package com.Omer.Account.model;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.GenericGenerators;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;
    private BigDecimal balance;
    private LocalDateTime creationDate;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL) // customer in içinde account bilgileri olucak fakat account nesnesi çağırıldığı zaman customer bilgilerini çekicek customer account çekicek ve loop select sorgu olucak bunun onune geçiyor fetchType.Lazy / cascade ise entitde yapılan herhangi bir işlemde eğer account a ait customer güncellenirse customer ds da güncelle => all crud hepsi.
    @JoinColumn(name = "customer_id", nullable = false)//foreign key
    private Customer customer;

    @OneToMany(mappedBy = "account",fetch = FetchType.EAGER,cascade = CascadeType.ALL)// Transaction entitysindeki account değişkeni ile bağlanır Eager => bu oluştuğunda transactionuda hemen oluştur
    private Set<Transaction> transactions  = new HashSet<>() ;

   public  Account(Customer customer, BigDecimal balance,LocalDateTime localDateTime)
    {
        this.id="";
        this.customer=customer;
        this.balance=balance;
        this.creationDate=localDateTime;

    }
}
