package com.Omer.Account.model;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private String id;
    private String name;
    private String surname;

    @OneToMany(mappedBy = "customer",fetch = FetchType.EAGER)
    private Set<Account> accounts;

}
