package org.norma.finalproject.account.service;

import org.norma.finalproject.account.entity.base.Account;

import java.util.Optional;

public interface BaseAccountService {
   boolean checkIsAccountNoUnique(String accountNo);
   boolean checkIsIbanNoUnique(String ibanNo);
   Account update(Account account);

    Optional<Account> findAccountByIbanNumberAndCustomerId(String iban,long customerId);

    Optional<Account> findAccountByIbanNumber(String iban);
}
