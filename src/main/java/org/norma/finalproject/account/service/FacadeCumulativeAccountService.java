package org.norma.finalproject.account.service;

import org.norma.finalproject.account.core.model.request.CreateCumulativeAccountRequest;
import org.norma.finalproject.common.response.GeneralResponse;

public interface FacadeCumulativeAccountService {

    GeneralResponse create(Long customerId,CreateCumulativeAccountRequest createCumulativeAccountRequest);
}
