package org.norma.finalproject.card.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.norma.finalproject.card.entity.CreditCard;
import org.norma.finalproject.card.entity.CreditCardAccount;
import org.norma.finalproject.card.repository.CreditCardRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class CreditCardServiceImplTest {

    @Mock
    private CreditCardRepository creditCardRepository;

    @InjectMocks
    private CreditCardServiceImpl underTest;

    @Test
    public void givenCreditCard_whenSave_thenReturnedCreditCard() {
        //give
        CreditCard creditCard = createCreditCard();

        BDDMockito.given(creditCardRepository.save(creditCard)).willReturn(creditCard);
        // when
        CreditCard savedCreditCard = underTest.save(creditCard);
        // then
        Assertions.assertThat(savedCreditCard).isNotNull();
        Assertions.assertThat(savedCreditCard).isEqualTo(creditCard);

    }

    @Test
    public void givenCreditCardId_whenFindCreditCardById_thenReturnedCreditCard() {
        //give
        CreditCard creditCard = createCreditCard();
        BDDMockito.given(creditCardRepository.findById(creditCard.getId())).willReturn(Optional.of(creditCard));

        // when
        Optional<CreditCard> optionalCreditCard = underTest.findCreditCardById(creditCard.getId());
        // then
        Assertions.assertThat(optionalCreditCard).isNotEmpty();
        Assertions.assertThat(optionalCreditCard.get()).isEqualTo(creditCard);
    }

    @Test
    public void givenCardNumber_whenFindCreditCardByCreditCardNumber_thenReturnedCreditCard() {
        //give
        CreditCard creditCard = createCreditCard();
        BDDMockito.given(creditCardRepository.findByCardNumber(creditCard.getCardNumber())).willReturn(Optional.of(creditCard));
        // when
        Optional<CreditCard> optionalCreditCard = underTest.findCreditCardByCreditCardNumber(creditCard.getCardNumber());
        // then
        Assertions.assertThat(optionalCreditCard).isNotEmpty();
        Assertions.assertThat(optionalCreditCard.get()).isEqualTo(creditCard);


    }

    @Test
    public void givenCreditCard_whenDelete_thenReturnedCreditCard() {
        //give
        CreditCard creditCard = createCreditCard();
        doNothing().when(creditCardRepository).delete(creditCard);
        // when
        underTest.delete(creditCard);

    }

    @Test
    public void whenFindAllCreditCardsCutoffDateInToday_thenReturnCreditCardList() {
        //give
        CreditCard creditCard1 = createCreditCard();
        CreditCard creditCard2 = createCreditCard();
        CreditCard creditCard3 = createCreditCard();
        List<CreditCard> creditCardList = List.of(creditCard1, creditCard2, creditCard3);
        BDDMockito.given(creditCardRepository.findAllCutoffDateInToday()).willReturn(creditCardList);
        // when
        List<CreditCard> allCreditCardsCutoffDateInToday = underTest.findAllCreditCardsCutoffDateInToday();

        // then
        Assertions.assertThat(allCreditCardsCutoffDateInToday.size()).isEqualTo(3);
    }

    private CreditCard createCreditCard() {
        CreditCard creditCard = new CreditCard();
        Long creditCardId = 1L;
        creditCard.setId(creditCardId);
        creditCard.setCardNumber("111");
        CreditCardAccount account = new CreditCardAccount();
        account.setLastExtractDebt(BigDecimal.ZERO);
        account.setTotalDebt(BigDecimal.ZERO);
        account.setAvailableBalance(BigDecimal.ZERO);
        account.setCutOffDate(new Date());
        creditCard.setCreditCardAccount(account);
        return creditCard;
    }
}