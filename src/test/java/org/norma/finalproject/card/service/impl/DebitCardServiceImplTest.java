package org.norma.finalproject.card.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.norma.finalproject.card.entity.DebitCard;
import org.norma.finalproject.card.entity.enums.CardStatus;
import org.norma.finalproject.card.repository.DebitCardRepository;
import org.norma.finalproject.customer.entity.Customer;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class DebitCardServiceImplTest {
    @Mock
    private DebitCardRepository debitCardRepository;
    @InjectMocks
    private DebitCardServiceImpl underTest;

    @Test
    public void givenDebitCard_whenSave_thenReturnSavedDebitCard() {
        // given
        DebitCard debitCard = createDebitCard();
        BDDMockito.given(debitCardRepository.save(debitCard)).willReturn(debitCard);
        // when
        DebitCard savedDebitCard = underTest.save(debitCard);

        // then
        Assertions.assertThat(savedDebitCard).isNotNull();
        Assertions.assertThat(savedDebitCard).isEqualTo(debitCard);

    }

    @Test
    public void givenCustomerId_whenGetAllCustomersDebitCards_thenReturnListDebitCards() {
        // given
        DebitCard debitCard1 = createDebitCard();
        DebitCard debitCard2 = createDebitCard();
        DebitCard debitCard3 = createDebitCard();
        Long customerID = 1L;
        List<DebitCard> debitCards = List.of(debitCard1, debitCard2, debitCard3);
        BDDMockito.given(debitCardRepository.findAllByCheckingAccount_Customer_Id(customerID)).willReturn(debitCards);
        // when
        List<DebitCard> allCustomersDebitCards = underTest.getAllCustomersDebitCards(customerID);

        // then
        Assertions.assertThat(allCustomersDebitCards.size()).isEqualTo(debitCards.size());
    }

    @Test
    public void givenCustomerIDAndDebitCardID_whenFindDebitCardWithCustomerIDAndCardID_thenReturnDebitCard() {
        // given
        Long customerId = 1L;
        Long debitCardId = 1L;

        DebitCard debitCard = createDebitCard();
        debitCard.setId(debitCardId);
        BDDMockito.given(debitCardRepository.findDebitCardByCheckingAccount_Customer_IdAndId(customerId, debitCardId)).willReturn(Optional.of(debitCard));
        // when
        Optional<DebitCard> optionalDebitCard = underTest.findDebitCardWithCustomerIDAndCardID(customerId, debitCardId);
        // then
        Assertions.assertThat(optionalDebitCard).isNotEmpty();
    }

    @Test
    public void givenDebitCardNumber_whenFindDebitCardWithCardNumber_thenReturnDebitCard() {
        // given
        DebitCard debitCard = createDebitCard();
        BDDMockito.given(debitCardRepository.findDebitCardByCardNumber(debitCard.getCardNumber())).willReturn(Optional.of(debitCard));
        // when
        Optional<DebitCard> optionalDebitCard = underTest.findDebitCardWithCardNumber(debitCard.getCardNumber());
        // then
        Assertions.assertThat(optionalDebitCard).isNotEmpty();
    }

    @Test
    public void givenCheckingAccountId_whenExistsDebitCardByCheckingAccountId_thenReturnTrue() {
        // given
        Long parentCheckingAccountId = 1L;
        BDDMockito.given(debitCardRepository.existsByCheckingAccount_Id(parentCheckingAccountId)).willReturn(true);
        // when
        boolean existsDebitCardByCheckingAccountId = underTest.existsDebitCardByCheckingAccountId(parentCheckingAccountId);
        // then
        Assertions.assertThat(existsDebitCardByCheckingAccountId).isTrue();
    }

    @Test
    public void givenCheckingAccountId_whenFindByParentCheckingAccount_thenReturnDebitCard() {
        // given
        Long parentCheckingAccountId = 1L;
        DebitCard debitCard = createDebitCard();
        BDDMockito.given(debitCardRepository.findDebitCardByCheckingAccount_Id(parentCheckingAccountId)).willReturn(Optional.of(debitCard));
        // when
        Optional<DebitCard> optionalDebitCard = underTest.findByParentCheckingAccount(parentCheckingAccountId);
        // then
        Assertions.assertThat(optionalDebitCard).isNotEmpty();
    }

    @Test
    public void givenDebitCard_whenDelete_thenDelete() {
        // given
        DebitCard debitCard = createDebitCard();
        doNothing().when(debitCardRepository).delete(debitCard);
        // when
        underTest.delete(debitCard);
    }

    private DebitCard createDebitCard() {
        DebitCard debitCard = new DebitCard();
        debitCard.setCardNumber("1111");
        debitCard.setPassword("1111");
        debitCard.setCvv("1111");
        debitCard.setStatus(CardStatus.ACTIVE);
        debitCard.setExpiryDate(new Date());
        return debitCard;
    }

    private Customer createCustomer() {
        Customer customer = new Customer();
        String invalidIdentityNumber = "00000000000";
        customer.setIdentityNumber("111111111111");
        customer.setName("ENGIN");
        customer.setSurname("AKIN");
        return customer;
    }
}