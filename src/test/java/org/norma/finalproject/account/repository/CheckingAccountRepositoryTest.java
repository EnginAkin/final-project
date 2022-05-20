package org.norma.finalproject.account.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.norma.finalproject.account.entity.CheckingAccount;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CheckingAccountRepositoryTest {

    @Autowired
    private CheckingAccountRepository underTest;

    @Autowired
    private CustomerRepository customerRepository;
/*
    @Test
    void givenValidBalance_whenExistsDepositAccountsBalanceGreatherThanZeroByCustomerId_thenReturnTrue() {
        // given -- precondition
        Customer customer = new Customer();
        customer.setName("engin");
        customer.setPassword("123");
        customerRepository.save(customer);
        CheckingAccount checkingAccount1 = new CheckingAccount();
        checkingAccount1.setCustomer(customer);
        checkingAccount1.setBalance(BigDecimal.ZERO);
        CheckingAccount checkingAccount2 = new CheckingAccount();
        checkingAccount2.setCustomer(customer);
        checkingAccount2.setBalance(BigDecimal.valueOf(10));
        CheckingAccount checkingAccount3 = new CheckingAccount();
        checkingAccount3.setCustomer(customer);
        checkingAccount3.setBalance(BigDecimal.valueOf(10));
        // when
        underTest.saveAll(List.of(checkingAccount1, checkingAccount2, checkingAccount3));
        // then -assert
        boolean existsDepositAccountsBalanceGreatherThanZero = underTest.existsCheckingAccountsBalanceGreatherThanZeroByCustomerId(customer.getId());
        Assertions.assertThat(existsDepositAccountsBalanceGreatherThanZero).isTrue();
    }



    @Test
    void givenInvalidBalance_whenExistsDepositAccountsBalanceGreatherThanZeroByCustomerId_thenReturnTrue() {
        // given -- precondition
        Customer customer = new Customer();
        customer.setName("engin");
        customer.setPassword("123");
        customerRepository.save(customer);
        CheckingAccount checkingAccount1 = new CheckingAccount();
        checkingAccount1.setCustomer(customer);
        checkingAccount1.setBalance(BigDecimal.ZERO);
        CheckingAccount checkingAccount2 = new CheckingAccount();
        checkingAccount2.setCustomer(customer);
        checkingAccount2.setBalance(BigDecimal.ZERO);
        CheckingAccount checkingAccount3 = new CheckingAccount();
        checkingAccount3.setCustomer(customer);
        checkingAccount3.setBalance(BigDecimal.ZERO);
        // when
        underTest.saveAll(List.of(checkingAccount1, checkingAccount2, checkingAccount3));
        // then -assert
        boolean existsDepositAccountsBalanceGreatherThanZero = underTest.existsCheckingAccountsBalanceGreatherThanZeroByCustomerId(customer.getId());
        Assertions.assertThat(existsDepositAccountsBalanceGreatherThanZero).isFalse();

    }
 */
}