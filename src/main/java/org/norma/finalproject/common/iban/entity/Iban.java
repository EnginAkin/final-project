package org.norma.finalproject.common.iban.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.entity.Role;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Iban {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ibanNumber;

    @ManyToOne
    private Customer customer;


}
