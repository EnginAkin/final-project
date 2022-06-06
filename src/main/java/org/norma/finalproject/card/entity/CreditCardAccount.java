package org.norma.finalproject.card.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
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
    private BigDecimal lastExtractDebt = BigDecimal.ZERO;// son ekstreden kalan borç
    private BigDecimal totalDebt; // güncel borç

    @Temporal(TemporalType.DATE)
    private Date cutOffDate;// hesap kesim tarihi
    @Temporal(TemporalType.DATE)
    private Date paymentDate;// ödeme tarihi
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creditCardAccount")
    private Set<ExtractOfCard> extracts = new HashSet<>();


    public ExtractOfCard getCurrentTermExtract() {
        if (extracts.isEmpty()) {
            ExtractOfCard extract = new ExtractOfCard();
            extract.setExtractTerm(new Date());
            extracts.add(extract);
        }
        return extracts.stream().filter(ExtractOfCard::isCurrentTerm).findFirst().get();
    }
}
