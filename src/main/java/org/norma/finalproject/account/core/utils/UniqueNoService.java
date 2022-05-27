package org.norma.finalproject.account.core.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.norma.finalproject.account.service.BaseAccountService;
import org.springframework.stereotype.Component;


public interface UniqueNoService {


    public String creatAccountNo();
    public String creatCardNumber();
    public String createIbanNo(String accountNo, String bankCode) ;




}
