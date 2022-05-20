package org.norma.finalproject.account.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.norma.finalproject.account.entity.DepositAccount;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DepositAccountRepositoryTest {

    @Autowired
    private DepositAccountRepository underTest;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void givenValidBalance_whenExistsDepositAccountsBalanceGreatherThanZeroByCustomerId_thenReturnTrue() {
        // given -- precondition
        Customer customer = new Customer();
        customer.setName("engin");
        customer.setPassword("123");
        customerRepository.save(customer);
        DepositAccount depositAccount1 = new DepositAccount();
        depositAccount1.setCustomer(customer);
        depositAccount1.setBalance(BigDecimal.ZERO);
        DepositAccount depositAccount2 = new DepositAccount();
        depositAccount2.setCustomer(customer);
        depositAccount2.setBalance(BigDecimal.valueOf(10));
        DepositAccount depositAccount3 = new DepositAccount();
        depositAccount3.setCustomer(customer);
        depositAccount3.setBalance(BigDecimal.valueOf(10));
        // when
        underTest.saveAll(List.of(depositAccount1, depositAccount2, depositAccount3));
        // then -assert
        boolean existsDepositAccountsBalanceGreatherThanZero = underTest.existsDepositAccountsBalanceGreatherThanZeroByCustomerId(customer.getId());
        Assertions.assertThat(existsDepositAccountsBalanceGreatherThanZero).isTrue();
    }

    @Test
    void givenInvalidBalance_whenExistsDepositAccountsBalanceGreatherThanZeroByCustomerId_thenReturnTrue() {
        // given -- precondition
        Customer customer = new Customer();
        customer.setName("engin");
        customer.setPassword("123");
        customerRepository.save(customer);
        DepositAccount depositAccount1 = new DepositAccount();
        depositAccount1.setCustomer(customer);
        depositAccount1.setBalance(BigDecimal.ZERO);
        DepositAccount depositAccount2 = new DepositAccount();
        depositAccount2.setCustomer(customer);
        depositAccount2.setBalance(BigDecimal.ZERO);
        DepositAccount depositAccount3 = new DepositAccount();
        depositAccount3.setCustomer(customer);
        depositAccount3.setBalance(BigDecimal.ZERO);
        // when
        underTest.saveAll(List.of(depositAccount1, depositAccount2, depositAccount3));
        // then -assert
        boolean existsDepositAccountsBalanceGreatherThanZero = underTest.existsDepositAccountsBalanceGreatherThanZeroByCustomerId(customer.getId());
        Assertions.assertThat(existsDepositAccountsBalanceGreatherThanZero).isFalse();

    }
}