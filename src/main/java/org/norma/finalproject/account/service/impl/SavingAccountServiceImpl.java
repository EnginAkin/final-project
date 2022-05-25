package org.norma.finalproject.account.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.account.entity.SavingAccount;
import org.norma.finalproject.account.repository.SavingAccountRepository;
import org.norma.finalproject.account.service.SavingAccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SavingAccountServiceImpl implements SavingAccountService {
    private final SavingAccountRepository savingAccountRepository;
    @Override
    public SavingAccount save(SavingAccount savingAccount) {
        return savingAccountRepository.save(savingAccount);
    }

    @Override
    public List<SavingAccount> getAllAccountsByCustomerId(Long customerId) {
        return savingAccountRepository.findAllByCustomer_Id(customerId);
    }

    @Override
    public Optional<SavingAccount> getByParentId(Long parentAccountID) {
        return savingAccountRepository.findSavingAccountByParentAccount_Id(parentAccountID);
    }



    @Override
    public boolean isUsedParentAccountForSavingAccount(long customerId, long parentCheckingAccountId) {
        return savingAccountRepository.existsByParentAccount_IdAndCustomer_Id(parentCheckingAccountId,customerId);
    }
}
