package org.norma.finalproject.account.service;

import org.norma.finalproject.account.core.model.request.CreateDepositAcoountRequest;
import org.norma.finalproject.common.response.GeneralResponse;

public interface FacadeDepositAccountService {
    GeneralResponse create(CreateDepositAcoountRequest createDepositAcoountRequest);


}
