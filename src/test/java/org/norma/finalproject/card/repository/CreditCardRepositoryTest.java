package org.norma.finalproject.card.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.norma.finalproject.card.entity.CreditCard;
import org.norma.finalproject.card.entity.CreditCardAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CreditCardRepositoryTest {

    @Autowired
    private CreditCardRepository underTest;


    @Test
    public void whenFindAllCutoffDateInToday_thenReturnCreditCardList() {
        // given
        CreditCardAccount account = new CreditCardAccount();
        account.setCutOffDate(new Date());

        CreditCard creditCard = new CreditCard();
        creditCard.setCreditCardAccount(account);
        underTest.save(creditCard);

        CreditCardAccount account1 = new CreditCardAccount();
        account1.setCutOffDate(new Date());

        CreditCard creditCard1 = new CreditCard();
        creditCard1.setCreditCardAccount(account1);
        underTest.save(creditCard1);
        // when
        List<CreditCard> creditCards = underTest.findAllCutoffDateInToday();

        // then
        Assertions.assertThat(creditCards.size()).isEqualTo(2);
    }

    @Test
    public void givenCardNumberwhenFindByCardNumber_thenReturnCreditCard() {
        // given
        CreditCardAccount account = new CreditCardAccount();
        account.setCutOffDate(new Date());

        CreditCard creditCard = new CreditCard();
        String cardNumber = "1111111111";
        creditCard.setCardNumber(cardNumber);
        creditCard.setCreditCardAccount(account);
        underTest.save(creditCard);
        // when
        Optional<CreditCard> optionalCreditCard = underTest.findByCardNumber(cardNumber);
        // then
        Assertions.assertThat(optionalCreditCard).isNotEmpty();
        Assertions.assertThat(optionalCreditCard.get()).isEqualTo(creditCard);
    }

}