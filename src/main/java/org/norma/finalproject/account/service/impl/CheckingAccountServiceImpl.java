package org.norma.finalproject.account.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.account.entity.CheckingAccount;
import org.norma.finalproject.account.repository.CheckingAccountRepository;
import org.norma.finalproject.account.service.CheckingAccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Engin Akin
 * @version v1.0.0
 * @since version v1.0.0
 */
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
    public void deleteCustomerCheckingAccountById(Long checkingAccountId) {
        checkingAccountRepository.deleteById(checkingAccountId);

    }

    @Override
    public List<CheckingAccount> getUnBlockedAccounts(long customerId) {
        return checkingAccountRepository.findAllByCustomer_IdAndBlocked(customerId, false);
    }

    @Override
    public Optional<CheckingAccount> findAccountByAccountNumber(String parentAccountNumber) {
        return checkingAccountRepository.findByAccountNo(parentAccountNumber);
    }

    @Override
    public Optional<CheckingAccount> findCheckingAccountByEmail(String toEmail) {
        return checkingAccountRepository.findFirstByCustomer_Email(toEmail);
    }


}


