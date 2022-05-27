package org.norma.finalproject.account.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.norma.finalproject.account.entity.CheckingAccount;
import org.norma.finalproject.account.entity.base.Account;
import org.norma.finalproject.account.entity.enums.AccountType;
import org.norma.finalproject.account.repository.AccountRepository;
import org.norma.finalproject.account.service.BaseAccountService;
import org.norma.finalproject.account.service.CheckingAccountService;
import org.norma.finalproject.card.core.exception.DebitCardNotFoundException;
import org.norma.finalproject.card.entity.DebitCard;
import org.norma.finalproject.card.service.DebitCardService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BaseAccountServiceImpl implements BaseAccountService {
    private final AccountRepository repository;
    private final DebitCardService debitCardService;


    @Override
    public boolean checkIsAccountNoUnique(String accountNo) {
        return repository.existsAccountByAccountNo(accountNo);
    }

    @Override
    public boolean checkIsIbanNoUnique(String ibanNo) {
        return repository.existsAccountByIbanNo(ibanNo);
    }

    @Override
    public Account update(Account account) {
        return repository.save(account);
    }


    @Override
    public Optional<Account> findAccountByIbanNumber(String iban) {
        if (StringUtils.isEmpty(iban)) {
            throw new IllegalArgumentException("Iban cannot bu null");
        }
        return repository.findByIbanNo(iban);
    }

    @Override
    public Optional<Account> findById(long id) {
        return repository.findById(id);
    }

    @Override
    public void refresh(Account account)  {
        if (account.getAccountType().equals(AccountType.CHECKING)) {

            Optional<DebitCard> optionalDebitCard = debitCardService.findByParentCheckingAccount(account.getId());
            if(optionalDebitCard.isPresent()){
                optionalDebitCard.get().setBalance(account.getBalance());
                debitCardService.save(optionalDebitCard.get());
            }

        }
    }


}
