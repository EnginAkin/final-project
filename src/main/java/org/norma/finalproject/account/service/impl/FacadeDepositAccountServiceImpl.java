package org.norma.finalproject.account.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.account.core.exception.AccountNameAlreadyHaveException;
import org.norma.finalproject.account.core.mapper.DepositAccountMapper;
import org.norma.finalproject.account.core.model.request.CreateDepositAcoountRequest;
import org.norma.finalproject.account.core.utils.UniqueNoCreator;
import org.norma.finalproject.account.entity.DepositAccount;
import org.norma.finalproject.account.service.DepositAccountService;
import org.norma.finalproject.account.service.FacadeDepositAccountService;
import org.norma.finalproject.common.response.GeneralDataResponse;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FacadeDepositAccountServiceImpl implements FacadeDepositAccountService {

    private final CustomerService customerService;
    private final DepositAccountService depositAccountService;

    private final UniqueNoCreator uniqueNoCreator;
    private final DepositAccountMapper mapper;

    @Override
    public GeneralResponse create(Long customerId, CreateDepositAcoountRequest createDepositAcoountRequest) throws CustomerNotFoundException, AccountNameAlreadyHaveException {
        DepositAccount depositAccount = mapper.ToEntity(createDepositAcoountRequest);

        Optional<Customer> customer = customerService.getCustomerById(customerId);
        if (customer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        boolean existDepositAccountByAccountName = depositAccountService.existsCustomerDepositAccountByAccountName(customer.get(), createDepositAcoountRequest.getAccountName());
        if (existDepositAccountByAccountName) {
            throw new AccountNameAlreadyHaveException(createDepositAcoountRequest.getAccountName() + " name already have account in your accounts.");
        }

        String AccountNo = uniqueNoCreator.createDepositAccountNo();
        depositAccount.setAccountNo(AccountNo);
        String IbanNo = uniqueNoCreator.createDepositIbanNo();
        depositAccount.setIbanNo(IbanNo);
        depositAccount.setCreatedAt(new Date());
        depositAccount.setCreatedBy("ENGIN AKIN");
        depositAccount.setCustomer(customer.get());
        DepositAccount savedDepositAccount = depositAccountService.save(depositAccount);
        return new GeneralDataResponse<>(mapper.toDto(savedDepositAccount));
    }


}
