package org.norma.finalproject.account.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.norma.finalproject.account.entity.CheckingAccount;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CheckingAccountRepositoryTest {


    @Autowired
    private CheckingAccountRepository underTest;

    @Autowired
    private CustomerRepository customerRepository;


    @Test
    public void givenCustomerIDAndBlocked_whenFindAllByCustomer_IdAndBlocked_thenReturnsListCheckingAccount() {
        // given
        Customer customer = new Customer();
        customer.setName("engin");
        customer.setPassword("123");
        customerRepository.save(customer);
        CheckingAccount checkingAccount1 = new CheckingAccount();
        checkingAccount1.setBankCode("000000");
        checkingAccount1.setBranchCode("000000");
        checkingAccount1.setAccountName("account");
        checkingAccount1.setIbanNo("000000");
        checkingAccount1.setBranchName("branch");
        checkingAccount1.setAccountNo("000000");
        checkingAccount1.setBlocked(true);
        checkingAccount1.setCustomer(customer);
        CheckingAccount checkingAccount2 = new CheckingAccount();
        checkingAccount2.setBankCode("000000");
        checkingAccount2.setBranchCode("000000");
        checkingAccount2.setAccountName("account");
        checkingAccount2.setIbanNo("000000");
        checkingAccount2.setBranchName("branch");
        checkingAccount2.setAccountNo("000000");
        checkingAccount2.setBlocked(false);
        checkingAccount2.setCustomer(customer);
        CheckingAccount checkingAccount3 = new CheckingAccount();
        checkingAccount3.setBankCode("000000");
        checkingAccount3.setBranchCode("000000");
        checkingAccount3.setAccountName("account");
        checkingAccount3.setIbanNo("000000");
        checkingAccount3.setBranchName("branch");
        checkingAccount3.setAccountNo("000000");
        checkingAccount3.setBlocked(false);
        checkingAccount3.setCustomer(customer);
        underTest.saveAll(List.of(checkingAccount1, checkingAccount2, checkingAccount3));

        //when
        List<CheckingAccount> unBlockedAccountList = underTest.findAllByCustomer_IdAndBlocked(customer.getId(), false);

        // then
        Assertions.assertThat(unBlockedAccountList).isNotNull();
        Assertions.assertThat(unBlockedAccountList.size()).isEqualTo(2);

    }

    @Test
    public void givenAccountNo_whenFindAllByCustomer_IdAndBlocked_thenReturnsCheckingAccount() {
        // given
        CheckingAccount checkingAccount = new CheckingAccount();
        String accountNumber = "0000000000000000";
        checkingAccount.setAccountNo(accountNumber);
        checkingAccount.setBankCode("000000");
        checkingAccount.setBranchCode("000000");
        checkingAccount.setBranchName("FATIH");
        checkingAccount.setAccountName("dd");
        checkingAccount.setIbanNo("111111");
        checkingAccount.setBlocked(true);
        underTest.save(checkingAccount);
        // when
        Optional<CheckingAccount> optionalCheckingAccount = underTest.findByAccountNo(accountNumber);
        // then
        Assertions.assertThat(optionalCheckingAccount).isNotEmpty();
        Assertions.assertThat(optionalCheckingAccount.get().getAccountNo()).isEqualTo(accountNumber);


    }


}