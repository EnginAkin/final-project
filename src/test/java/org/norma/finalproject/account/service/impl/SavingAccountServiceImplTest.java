package org.norma.finalproject.account.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.norma.finalproject.account.entity.CheckingAccount;
import org.norma.finalproject.account.entity.SavingAccount;
import org.norma.finalproject.account.entity.enums.Maturity;
import org.norma.finalproject.account.repository.SavingAccountRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class SavingAccountServiceImplTest {

    @Mock
    private SavingAccountRepository savingAccountRepository;
    @InjectMocks
    private SavingAccountServiceImpl underTest;

    @BeforeEach
    public void setup() {
        savingAccountRepository.deleteAll();
    }

    @Test
    public void givenSavingAccount_whenSave_thenReturnSavedAccount() {
        // given
        SavingAccount savingAccount = createSavingAccount();
        BDDMockito.given(savingAccountRepository.save(savingAccount)).willReturn(savingAccount);
        // when
        SavingAccount savedAccount = underTest.save(savingAccount);
        // then
        Assertions.assertThat(savedAccount).isNotNull();
        Assertions.assertThat(savedAccount).isEqualTo(savingAccount);
    }

    @Test
    public void givenParentAccountId_whenGetByParentId_thenReturnSavingAccount() {
        // given
        SavingAccount savingAccount = createSavingAccount();
        CheckingAccount checkingAccount = new CheckingAccount();
        checkingAccount.setId(1L);
        checkingAccount.setAccountName("account name");
        checkingAccount.setAccountNo("111111");
        checkingAccount.setIbanNo("111111");
        savingAccount.setParentAccount(checkingAccount);
        BDDMockito.given(savingAccountRepository.findSavingAccountByParentAccount_Id(1L)).willReturn(Optional.of(savingAccount));
        // when
        Optional<SavingAccount> optionalSavingAccount = underTest.getByParentId(1L);
        // then
        assertThat(optionalSavingAccount).isNotEmpty();
    }

    @Test
    public void givenCustomerID_whenGetAllAccountsByCustomerId_thenReturnListObjects() {
        // given
        SavingAccount savingAccount1 = createSavingAccount();
        SavingAccount savingAccount2 = createSavingAccount();
        List<SavingAccount> savingAccountList = List.of(savingAccount1, savingAccount2);
        BDDMockito.given(savingAccountRepository.findAllByCustomer_Id(1L)).willReturn(savingAccountList);
        // when
        List<SavingAccount> responseList = underTest.getAllAccountsByCustomerId(1L);
        // then
        assertThat(responseList).isNotNull();
        assertThat(responseList.size()).isEqualTo(2);
    }

    private SavingAccount createSavingAccount() {
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setAccountName("account name");
        savingAccount.setIbanNo("111");
        savingAccount.setAccountNo("111");
        savingAccount.setMaturity(Maturity.SIXTY_DAY);
        savingAccount.setSuccessRate(BigDecimal.TEN);
        savingAccount.setBalance(BigDecimal.TEN);
        savingAccount.setTargetAmount(BigDecimal.TEN);
        return savingAccount;
    }
/*
    @Test
    public void given_when_then(){
        // given

        // when

        // then
    }


 */


}