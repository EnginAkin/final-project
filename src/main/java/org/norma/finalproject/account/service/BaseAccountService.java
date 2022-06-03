package org.norma.finalproject.account.service;

import org.norma.finalproject.account.entity.base.Account;
import org.norma.finalproject.card.core.exception.DebitCardNotFoundException;

import java.util.Optional;

public interface BaseAccountService {
   boolean checkIsAccountNoUnique(String accountNo);
   boolean checkIsIbanNoUnique(String ibanNo);
   Account update(Account account);
    Optional<Account> findAccountByIbanNumber(String iban);
    Optional<Account> findById(long id);

}
