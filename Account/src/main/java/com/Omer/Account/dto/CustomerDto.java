package com.Omer.Account.dto;

import lombok.*;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDto {
    private String id;
    private String name;
    private String surname;
    private Set<CustomerAccountDto> accounts;

}
