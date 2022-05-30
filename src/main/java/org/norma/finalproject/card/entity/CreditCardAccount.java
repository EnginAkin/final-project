package org.norma.finalproject.card.entity;

import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.card.entity.base.CreditCardActivity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
    private BigDecimal totalDebt; // güncel borç

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "creditCardAccount")
    private Set<ExtractOfCard> extracts=new HashSet<>();



    public ExtractOfCard getCurrentTermExtract() {
        if(extracts.isEmpty()){
            ExtractOfCard extract=new ExtractOfCard();
            extracts.add(extract);
        }
        return extracts.stream().filter(ExtractOfCard::isCurrentTerm).findFirst().get();
    }
}
