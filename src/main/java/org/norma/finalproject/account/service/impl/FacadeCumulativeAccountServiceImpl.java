package org.norma.finalproject.account.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.account.core.model.request.CreateCumulativeAccountRequest;
import org.norma.finalproject.account.service.FacadeCumulativeAccountService;
import org.norma.finalproject.common.response.GeneralResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FacadeCumulativeAccountServiceImpl implements FacadeCumulativeAccountService {


    @Override
    public GeneralResponse create(Long customerId, CreateCumulativeAccountRequest createCumulativeAccountRequest) {
        // Asqari tutarın üstünde para yatırmalıdır.
        // TODO vadesiz hesap ilk yapılmalı


        // vadesiz hesap olmak zorunda ,
        // mevudatsız hesabı tipi ile açmak istediği hesap tipi aynı mı ?


        return null;
    }
}
