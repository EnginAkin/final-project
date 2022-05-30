package org.norma.finalproject.card.entity;

import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.customer.entity.Customer;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cardNumber;
    private String cvv;
    private String password;
    @Temporal(TemporalType.DATE)
    private Date expiryDate;

    @OneToOne(cascade = CascadeType.ALL)
    private CreditCardAccount creditCardAccount;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;


}
