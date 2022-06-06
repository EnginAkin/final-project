package org.norma.finalproject.card.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.norma.finalproject.account.repository.CheckingAccountRepository;
import org.norma.finalproject.card.entity.DebitCard;
import org.norma.finalproject.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DebitCardRepositoryTest {


    @Autowired
    private DebitCardRepository underTest;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    @BeforeEach
    public void setup() {
        customerRepository.deleteAll();
        underTest.deleteAll();
    }


    @Test
    public void givenCardNumber_whenFindDebitCard_thenReturnsCard() {

        // given
        String cardNumber = "1111";

        DebitCard debitCard = new DebitCard();
        debitCard.setCardNumber(cardNumber);
        debitCard.setPassword("123");
        debitCard.setCvv("123");
        underTest.save(debitCard);
        // when
        Optional<DebitCard> optionalDebitCard = underTest.findDebitCardByCardNumber(cardNumber);
        // then
        Assertions.assertThat(optionalDebitCard).isNotEmpty();
        Assertions.assertThat(optionalDebitCard.get().getCardNumber()).isEqualTo(cardNumber);
    }


}
