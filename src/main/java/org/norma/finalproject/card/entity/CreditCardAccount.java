package org.norma.finalproject.card.entity;

import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.customer.core.exception.IdentityNotValidException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class CreditCardAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private BigDecimal totalCreditLimit;
    private BigDecimal availableBalance;
    private Date cutOffDate;// hesap kesim tarihi
    private Date paymentDate;// ödeme tarihi
    private BigDecimal lastExtractDebt=BigDecimal.ZERO;// son ekstreden kalan borç
    private BigDecimal currentDebt; // güncel borç


    @OneToMany(cascade = CascadeType.ALL,mappedBy = "creditCardAccount")
    private Set<ExtractOfCard> extracts=new HashSet<>();



}
