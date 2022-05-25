package org.norma.finalproject.account.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.account.entity.CheckingAccount;
import org.norma.finalproject.account.entity.base.AccountActivity;
import org.norma.finalproject.account.repository.CheckingAccountRepository;
import org.norma.finalproject.account.service.CheckingAccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckingAccountServiceImpl implements CheckingAccountService {

    private final CheckingAccountRepository checkingAccountRepository;


    @Override
    public CheckingAccount save(CheckingAccount checkingAccount) {
        return checkingAccountRepository.save(checkingAccount);
    }

    @Override
    public Optional<CheckingAccount> findById(long accountId) {
        return checkingAccountRepository.findById(accountId);
    }

    @Override
    public void deleteCustomerCheckingAccountById(CheckingAccount checkingAccount) {
        checkingAccountRepository.delete(checkingAccount);

    }

    @Override
    public List<CheckingAccount> getUnBlockedAccounts(long customerId) {
        return checkingAccountRepository.findAllByCustomer_IdAndBlocked(customerId,false);
    }

    @Override
    public Optional<CheckingAccount> getAccountByAccountNumber(String parentAccountNumber) {
        return checkingAccountRepository.findByAccountNo(parentAccountNumber);
    }




}


