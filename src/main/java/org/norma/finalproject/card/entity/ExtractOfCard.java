package org.norma.finalproject.card.entity;

import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.card.entity.base.CreditCardActivity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class ExtractOfCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal totalExtractDebt = BigDecimal.ZERO;

    private boolean isCurrentTerm = true;

    @Temporal(TemporalType.DATE)
    private Date extractTerm;// ekstre dönemi

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "extractOfCard")
    private List<CreditCardActivity> creditCardActivities = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_card_account_id")
    private CreditCardAccount creditCardAccount;

    public void addCreditCardActivity(CreditCardActivity creditCardActivity) {
        creditCardActivities.add(creditCardActivity);
    }

}
