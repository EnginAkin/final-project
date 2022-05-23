package org.norma.finalproject.account.service;

public interface BaseAccountService {
   boolean checkIsAccountNoUnique(String accountNo);
   boolean checkIsIbanNoUnique(String ibanNo);
}
