package org.norma.finalproject.card.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.norma.finalproject.account.entity.CheckingAccount;
import org.norma.finalproject.account.repository.CheckingAccountRepository;
import org.norma.finalproject.card.entity.DebitCard;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
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
    public void setup(){
        underTest.deleteAll();
    }

    @Test
    public void givenCustomerID_whenFindAllCustomerDebitCards_thenReturnsCardList(){
        // given
        Long customerID=1L;
        Customer customer=new Customer();
        customer.setId(customerID);
        customerRepository.save(customer);

        CheckingAccount checkingAccount=new CheckingAccount();
        checkingAccount.setBankCode("000000");
        checkingAccount.setBranchCode("000000");
        checkingAccount.setAccountName("account");
        checkingAccount.setIbanNo("000000");
        checkingAccount.setBranchName("branch");
        checkingAccount.setAccountNo("000000");
        checkingAccount.setBlocked(false);
        checkingAccount.setCustomer(customer);
        checkingAccountRepository.save(checkingAccount);

        DebitCard debitCard1 =new DebitCard();
        debitCard1.setCardNumber("11111");
        debitCard1.setBalance(BigDecimal.TEN);
        debitCard1.setPassword("123");
        debitCard1.setCvv("123");
        debitCard1.setCheckingAccount(checkingAccount);

        underTest.saveAll(List.of(debitCard1));

        // when
        List<DebitCard> debitCardListResponse = underTest.findAllByCheckingAccount_Customer_Id(customerID);
        // then

        Assertions.assertThat(debitCardListResponse.size()).isEqualTo(1);

    }


    @Test
    public void givenCardNumber_whenFindDebitCard_thenReturnsCard(){

        // given
        String cardNumber="1111";

        DebitCard debitCard =new DebitCard();
        debitCard.setCardNumber(cardNumber);
        debitCard.setBalance(BigDecimal.TEN);
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
