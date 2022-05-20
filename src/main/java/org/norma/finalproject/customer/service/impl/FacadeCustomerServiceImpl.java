package org.norma.finalproject.customer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.account.entity.DepositAccount;
import org.norma.finalproject.account.service.DepositAccountService;
import org.norma.finalproject.common.response.GeneralDataResponse;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.common.response.GeneralSuccessfullResponse;
import org.norma.finalproject.customer.core.exception.*;
import org.norma.finalproject.customer.core.mapper.CustomerMapper;
import org.norma.finalproject.customer.core.model.request.CreateCustomerRequest;
import org.norma.finalproject.customer.core.model.request.UpdateCustomerRequest;
import org.norma.finalproject.customer.core.utilities.CustomerConstant;
import org.norma.finalproject.customer.core.utilities.Utils;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.service.CustomerService;
import org.norma.finalproject.customer.service.FacadeCustomerService;
import org.norma.finalproject.customer.service.IdentityVerifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FacadeCustomerServiceImpl implements FacadeCustomerService {

    private final CustomerService customerService;
    private final IdentityVerifier identityVerifier;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;
    private final DepositAccountService depositAccountService;

    @Override
    public GeneralResponse signup(CreateCustomerRequest createCustomerRequest) throws NotAcceptableAgeException, IdentityNotValidException, CustomerAlreadyRegisterException {
        Customer customer = customerMapper.customerDtoToCustomer(createCustomerRequest);

        boolean verifyIdentityNumber = identityVerifier.verify(customer.getIdentityNumber());
        if (!verifyIdentityNumber) {
            throw new IdentityNotValidException();
        }
        boolean isOver18YearsOld = Utils.isOver18YearsOld(customer.getBirthDay());
        if (!isOver18YearsOld) {
            throw new NotAcceptableAgeException();
        }
        customer.setPassword(passwordEncoder.encode(createCustomerRequest.getPassword()));
        Customer savedCustomer = customerService.save(customer);
        return new GeneralDataResponse<>(savedCustomer.getId(), CustomerConstant.SIGNUP_SUCESSFULL);
    }

    @Override
    public GeneralResponse update(Customer customer, UpdateCustomerRequest updateCustomerRequest) throws CustomerNotFoundException, UpdateCustomerSamePasswordException {
        if (customer==null) {
            throw new CustomerNotFoundException();
        }
        if (passwordEncoder.matches(updateCustomerRequest.getPassword(), customer.getPassword())) {
            throw new UpdateCustomerSamePasswordException();
        }
        customer.setPassword(passwordEncoder.encode(updateCustomerRequest.getPassword()));
        customer.setTelephone(updateCustomerRequest.getTelephone());
        customerService.update(customer);
        return new GeneralSuccessfullResponse(CustomerConstant.UPDATE_SUCESSFULL);
    }

    @Override
    public GeneralResponse delete(Customer customer) throws CustomerNotFoundException, CustomerDeleteException {
        if (customer==null) {
            throw new CustomerNotFoundException();
        }
        boolean checkHasMoneyInDepositAccounts = checkHasMoneyInDepositAccounts(customer.getDepositAccounts());
        if (checkHasMoneyInDepositAccounts) {
            throw new CustomerDeleteException(CustomerConstant.DELETE_CUSTOMER_OPERATION_HAS_BALANCE_EXCEPTION);
        }
        customerService.delete(customer);
        return new GeneralSuccessfullResponse("Customer deleted.");
    }

    public boolean checkHasMoneyInDepositAccounts(List<DepositAccount> depositAccountList){
        return depositAccountList.stream().anyMatch(depositAccount -> depositAccount.getBalance().compareTo(BigDecimal.ZERO)>0);
    }
}
