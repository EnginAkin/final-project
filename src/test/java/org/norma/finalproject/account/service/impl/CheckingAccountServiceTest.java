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
import org.norma.finalproject.account.repository.CheckingAccountRepository;
import org.norma.finalproject.customer.entity.Customer;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CheckingAccountServiceTest {
    @Mock
    private CheckingAccountRepository checkingAccountRepository;
    @InjectMocks
    private CheckingAccountServiceImpl underTest;

    @BeforeEach
    public void setup() {
        checkingAccountRepository.deleteAll();
    }

    @Test
    public void givenCheckingAccount_whenSave_thenReturnSavedAccount() {
        // given
        CheckingAccount checkingAccount = new CheckingAccount();
        checkingAccount.setAccountNo("1111");
        checkingAccount.setBranchCode("00");
        checkingAccount.setBankCode("000000");
        checkingAccount.setAccountName("account name");
        checkingAccount.setBranchName("FATIH");
        BDDMockito.given(checkingAccountRepository.save(checkingAccount)).willReturn(checkingAccount);
        // when
        CheckingAccount savedAccount = underTest.save(checkingAccount);
        // then
        Assertions.assertThat(savedAccount).isNotNull();
    }

    @Test
    public void givenAccountId_whenFindById_thenReturnCheckingAccount() {
        // given
        CheckingAccount checkingAccount = new CheckingAccount();
        checkingAccount.setAccountNo("1111");
        checkingAccount.setId(1L);
        checkingAccount.setBranchCode("00");
        checkingAccount.setBankCode("000000");
        checkingAccount.setAccountName("account name");
        checkingAccount.setBranchName("FATIH");
        BDDMockito.given(checkingAccountRepository.findById(checkingAccount.getId())).willReturn(Optional.of(checkingAccount));
        // when
        Optional<CheckingAccount> optionalCheckingAccount = underTest.findById(checkingAccount.getId());
        // then
        Assertions.assertThat(optionalCheckingAccount).isNotEmpty();
        Assertions.assertThat(optionalCheckingAccount.get().getAccountNo()).isEqualTo(checkingAccount.getAccountNo());
    }

    @Test
    public void givenCheckingAccount_whenDeleteCustomerCheckingAccountById() {
        // given

        Long invalidAccountId = 1L;
        BDDMockito.given(checkingAccountRepository.findById(invalidAccountId)).willReturn(Optional.empty());
        // when
        Optional<CheckingAccount> optionalCheckingAccount = underTest.findById(invalidAccountId);
        // then
        Assertions.assertThat(optionalCheckingAccount).isEmpty();
    }


    @Test
    public void givenCustomerId_whenGetUnblockedAccounts_thenReturnUnblockedAccountList() {
        // given
        Customer customer = new Customer();
        customer.setId(1L);

        CheckingAccount checkingAccount1 = new CheckingAccount();
        checkingAccount1.setAccountNo("1111");
        checkingAccount1.setBranchCode("00");
        checkingAccount1.setBlocked(false);
        checkingAccount1.setBankCode("000000");
        checkingAccount1.setAccountName("account name");
        checkingAccount1.setBranchName("FATIH");
        checkingAccount1.setCustomer(customer);

        CheckingAccount checkingAccount3 = new CheckingAccount();
        checkingAccount3.setAccountNo("1111");
        checkingAccount3.setBranchCode("00");
        checkingAccount3.setBlocked(false);
        checkingAccount3.setBankCode("000000");
        checkingAccount3.setAccountName("account name");
        checkingAccount3.setBranchName("FATIH");
        checkingAccount3.setCustomer(customer);

        CheckingAccount checkingAccount2 = new CheckingAccount();
        checkingAccount2.setAccountNo("1111");
        checkingAccount2.setBranchCode("00");
        checkingAccount2.setBankCode("000000");
        checkingAccount2.setBlocked(true);
        checkingAccount2.setAccountName("account name");
        checkingAccount2.setBranchName("FATIH");
        checkingAccount2.setCustomer(customer);
        List<CheckingAccount> unblockedList = List.of(checkingAccount2, checkingAccount1);
        BDDMockito.given(checkingAccountRepository.findAllByCustomer_IdAndBlocked(customer.getId(), false)).willReturn(unblockedList);
        // when
        List<CheckingAccount> unBlockedAccounts = underTest.getUnBlockedAccounts(customer.getId());
        // then
        Assertions.assertThat(unBlockedAccounts.size()).isEqualTo(2);
    }

    @Test
    public void givenEmail_whenFindCheckingAccountByEmail_thenReturnCheckingAccount() {
        // given
        Customer customer = new Customer();
        customer.setEmail("email");
        customer.setId(1L);

        CheckingAccount checkingAccount = new CheckingAccount();
        checkingAccount.setAccountNo("1111");
        checkingAccount.setBranchCode("00");
        checkingAccount.setBlocked(false);
        checkingAccount.setBankCode("000000");
        checkingAccount.setAccountName("account name");
        checkingAccount.setBranchName("FATIH");
        checkingAccount.setCustomer(customer);
        BDDMockito.given(checkingAccountRepository.findFirstByCustomer_Email(customer.getEmail())).willReturn(Optional.of(checkingAccount));

        // when
        Optional<CheckingAccount> checkingAccountByEmail = underTest.findCheckingAccountByEmail(customer.getEmail());
        //then
        Assertions.assertThat(checkingAccountByEmail).isNotEmpty();

    }

    @Test
    public void givenAccountNumber_whenFindAccountByAccountNumber_thenReturnCheckingAccount() {
        // given
        Customer customer = new Customer();
        customer.setEmail("email");
        customer.setId(1L);
        CheckingAccount checkingAccount = new CheckingAccount();
        checkingAccount.setAccountNo("1111");
        checkingAccount.setBranchCode("00");
        checkingAccount.setBlocked(false);
        checkingAccount.setBankCode("000000");
        checkingAccount.setAccountName("account name");
        checkingAccount.setBranchName("FATIH");
        checkingAccount.setCustomer(customer);
        BDDMockito.given(checkingAccountRepository.findByAccountNo(checkingAccount.getAccountNo())).willReturn(Optional.of(checkingAccount));

        // when
        Optional<CheckingAccount> checkingAccountByEmail = underTest.findAccountByAccountNumber(checkingAccount.getAccountNo());
        //then
        Assertions.assertThat(checkingAccountByEmail).isNotEmpty();

    }


    @Test
    public void givenInvalidEmail_whenFindCheckingAccountByEmail_thenReturnEmpty() {
        // given
        BDDMockito.given(checkingAccountRepository.findFirstByCustomer_Email("invalidEmail")).willReturn(Optional.empty());
        // when
        Optional<CheckingAccount> checkingAccountByEmail = underTest.findCheckingAccountByEmail("invalidEmail");
        //then
        Assertions.assertThat(checkingAccountByEmail).isEmpty();

    }

}
