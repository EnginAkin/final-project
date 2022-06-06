package org.norma.finalproject.card.entity.base;

import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.card.entity.ExtractOfCard;
import org.norma.finalproject.card.entity.enums.SpendCategory;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
public class CreditCardActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String crossAccount;
    private BigDecimal amount;
    private String description;
    private SpendCategory spendCategory;

    @Temporal(TemporalType.DATE)
    private Date processDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "extract_of_card_id")
    private ExtractOfCard extractOfCard;

}
