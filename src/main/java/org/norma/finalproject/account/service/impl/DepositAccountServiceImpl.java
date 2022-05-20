package org.norma.finalproject.account.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.account.entity.DepositAccount;
import org.norma.finalproject.account.repository.DepositAccountRepository;
import org.norma.finalproject.account.service.DepositAccountService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepositAccountServiceImpl implements DepositAccountService {

    private final DepositAccountRepository depositAccountRepository;

    @Override
    public boolean checkIsAccountNoUnique(String accountNo) {
        return !depositAccountRepository.existsDepositAccountByAccountNo(accountNo);
    }

    @Override
    public DepositAccount save(DepositAccount depositAccount) {
        return depositAccountRepository.save(depositAccount);
    }

    @Override
    public Optional<DepositAccount> findById(long accountId) {
        return depositAccountRepository.findById(accountId);
    }

    @Override
    public void deleteCustomerDepositAccount(DepositAccount depositAccount) {
        depositAccountRepository.delete(depositAccount);

    }


}


