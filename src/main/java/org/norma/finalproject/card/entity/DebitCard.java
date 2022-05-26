package org.norma.finalproject.card.entity;

import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.account.entity.CheckingAccount;
import org.norma.finalproject.card.entity.enums.CardStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
public class DebitCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardNumber;
    private String password;
    private BigDecimal balance=BigDecimal.ZERO;
    private String cvv;

    @Enumerated(EnumType.STRING)
    private CardStatus status=CardStatus.ACTIVE;

    @Temporal(TemporalType.DATE)
    private Date expiryDate;

    @OneToOne(fetch = FetchType.LAZY)
    private CheckingAccount checkingAccount;


    public void refreshBalance() {
        this.balance=checkingAccount.getBalance();
    }
}
