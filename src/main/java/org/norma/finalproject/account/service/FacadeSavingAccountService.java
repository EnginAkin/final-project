package org.norma.finalproject.account.service;

import org.norma.finalproject.account.core.model.request.CreateSavingAccountRequest;
import org.norma.finalproject.common.response.GeneralResponse;

public interface FacadeSavingAccountService {

    GeneralResponse create(Long customerId, CreateSavingAccountRequest createSavingAccountRequest);
}
