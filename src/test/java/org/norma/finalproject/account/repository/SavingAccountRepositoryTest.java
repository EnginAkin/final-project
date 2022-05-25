package org.norma.finalproject.account.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.norma.finalproject.account.entity.CheckingAccount;
import org.norma.finalproject.account.entity.SavingAccount;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SavingAccountRepositoryTest {

    @Autowired
    private SavingAccountRepository underTest;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    @Test
    public void givenCustomerId_whenFindAllByCustomerId_thenReturnsSavingAccountList(){
        // given
        Customer customer=new Customer();
        customer.setName("engin");
        customer.setPassword("123");
        customerRepository.save(customer);
        CheckingAccount checkingAccount=new CheckingAccount();
        checkingAccount.setBankCode("000000");
        checkingAccount.setBranchCode("000000");
        checkingAccount.setBlocked(true);
        checkingAccount.setCustomer(customer);
        checkingAccountRepository.save(checkingAccount);
        SavingAccount savingAccount=new SavingAccount();
        savingAccount.setAccountName("example account name");
        savingAccount.setParentAccount(checkingAccount);
        underTest.save(savingAccount);
        // when
        List<SavingAccount> savingAccounts = underTest.findAllByCustomer_Id(customer.getId());
        // then
        Assertions.assertThat(savingAccounts).isNotNull();
    }

    @Test
    public void givenInvalidCustomerId_whenFindAllByCustomerId_thenReturnsSavingAccountList(){
        // given
        Customer customer=new Customer();
        long invalidCustomerId=10l;
        customer.setId(100L);
        customer.setName("engin");
        customer.setPassword("123");
        customerRepository.save(customer);
        CheckingAccount checkingAccount=new CheckingAccount();
        checkingAccount.setBankCode("000000");
        checkingAccount.setBranchCode("000000");
        checkingAccount.setBlocked(true);
        checkingAccount.setCustomer(customer);
        checkingAccountRepository.save(checkingAccount);
        SavingAccount savingAccount=new SavingAccount();
        savingAccount.setAccountName("example account name");
        savingAccount.setParentAccount(checkingAccount);
        underTest.save(savingAccount);
        // when
        List<SavingAccount> savingAccounts = underTest.findAllByCustomer_Id(invalidCustomerId);
        // then
        Assertions.assertThat(savingAccounts).isNull();
    }
    @Test
    public void given_when_then1(){
        // given
        // when
        // then
    }

}