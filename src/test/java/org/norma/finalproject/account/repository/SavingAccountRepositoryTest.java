package org.norma.finalproject.account.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.norma.finalproject.account.entity.CheckingAccount;
import org.norma.finalproject.account.entity.SavingAccount;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SavingAccountRepositoryTest {

    @Autowired
    private SavingAccountRepository underTest;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CheckingAccountRepository checkingAccountRepository;

    @BeforeEach
    public void setup() {
        underTest.deleteAll();
        customerRepository.deleteAll();
        checkingAccountRepository.deleteAll();
    }

    @Test
    public void given_when_then() {
        SavingAccount savingAccount1 = new SavingAccount();
        savingAccount1.setAccountName("1");
        savingAccount1.setIbanNo("1");
        savingAccount1.setAccountNo("1");
        savingAccount1.setMaturityDate(new Date());

        SavingAccount savingAccount2 = new SavingAccount();
        savingAccount2.setAccountName("12");
        savingAccount2.setIbanNo("12");
        savingAccount2.setAccountNo("12");
        Calendar maturity = Calendar.getInstance();
        maturity.add(Calendar.MONTH, 3);
        savingAccount2.setMaturityDate(maturity.getTime());
        underTest.saveAll(List.of(savingAccount1, savingAccount2));

        List<SavingAccount> maturityDateInToday = underTest.getAllSavingAccountMaturityDateInToday();

        Assertions.assertThat(maturityDateInToday.size()).isEqualTo(1);

    }

    @Test
    public void givenCustomerId_whenFindAllByCustomerId_thenReturnsSavingAccountList() {
        // given
        Customer customer = new Customer();
        customer.setName("engin");
        customer.setPassword("123");
        customerRepository.save(customer);
        CheckingAccount checkingAccount = new CheckingAccount();
        checkingAccount.setBankCode("000000");
        checkingAccount.setBranchCode("000000");
        checkingAccount.setBranchName("FATIH");
        checkingAccount.setBlocked(true);
        checkingAccount.setAccountNo("11");
        checkingAccount.setAccountName("account name");
        checkingAccount.setIbanNo("11");
        checkingAccount.setCustomer(customer);
        checkingAccountRepository.save(checkingAccount);
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setAccountName("1");
        savingAccount.setIbanNo("1");
        savingAccount.setAccountNo("1");
        savingAccount.setParentAccount(checkingAccount);
        underTest.save(savingAccount);
        // when
        List<SavingAccount> savingAccounts = underTest.findAllByCustomer_Id(customer.getId());
        // then
        Assertions.assertThat(savingAccounts).isNotNull();
    }


    @Test
    public void givenInvalidCustomerId_whenFindAllByCustomerId_thenReturnsSavingAccountList() {
        // given
        long invalidCustomerId = 10l;
        // when
        List<SavingAccount> savingAccounts = underTest.findAllByCustomer_Id(invalidCustomerId);
        // then
        Assertions.assertThat(savingAccounts.size()).isEqualTo(0);
    }


}