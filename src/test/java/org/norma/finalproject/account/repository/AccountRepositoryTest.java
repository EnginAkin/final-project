package org.norma.finalproject.account.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.norma.finalproject.account.entity.base.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AccountRepositoryTest {

    @Autowired
    private AccountRepository underTest;

    @Test
    public void givenAccountNo_whenExistsAccountByAccountNo_thenReturnTrue() {
        // given
        String accountNo = "0000000000000000";
        Account account = new Account();
        account.setAccountName("account");
        account.setIbanNo("1111111111");
        account.setAccountNo(accountNo);
        underTest.save(account);
        // when
        boolean existsAccountByAccountNo = underTest.existsAccountByAccountNo(accountNo);
        // then
        Assertions.assertThat(existsAccountByAccountNo).isTrue();
    }

    @Test
    public void givenInvalidAccountNo_whenExistsAccountByAccountNo_thenReturnFalse() {
        // given
        String accountNo = "0000000000000000";
        String invalidAccountNo = "11111111111111111";
        Account account = new Account();
        account.setAccountName("account");
        account.setIbanNo("1111111111");
        account.setAccountNo(accountNo);
        underTest.save(account);
        // when
        boolean existsAccountByAccountNo = underTest.existsAccountByAccountNo(invalidAccountNo);
        // then
        Assertions.assertThat(existsAccountByAccountNo).isFalse();
    }

    @Test
    public void givenValidIbanNo_whenFindByIbanNo_thenReturnsAccount() {
        // given
        String ibanNo = "0000000000000000";

        Account account = new Account();
        account.setAccountName("account");
        account.setIbanNo(ibanNo);
        account.setAccountNo("11111111");
        underTest.save(account);
        // when
        Optional<Account> optionalAccount = underTest.findByIbanNo(ibanNo);
        // then
        Assertions.assertThat(optionalAccount).isNotEmpty();
        Assertions.assertThat(optionalAccount.get().getIbanNo()).isEqualTo(ibanNo);
    }

    @Test
    public void givenInValidIbanNo_whenFindByIbanNo_thenReturnsEmpty() {
        // given
        String ibanNo = "0000000000000000";
        String invalidIbanNo = "1111111111111111";

        Account account = new Account();
        account.setAccountName("account");
        account.setAccountNo("11111111111");
        account.setIbanNo(ibanNo);
        underTest.save(account);
        // when
        Optional<Account> optionalAccount = underTest.findByIbanNo(invalidIbanNo);
        // then
        Assertions.assertThat(optionalAccount).isEmpty();
    }


}