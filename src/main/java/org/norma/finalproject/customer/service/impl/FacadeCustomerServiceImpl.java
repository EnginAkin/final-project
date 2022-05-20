package org.norma.finalproject.customer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public GeneralResponse update(long id, UpdateCustomerRequest updateCustomerRequest) throws CustomerNotFoundException, UpdateCustomerSamePasswordException {
        Optional<Customer> customer = customerService.getCustomerById(id);
        if (customer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        if (passwordEncoder.matches(updateCustomerRequest.getPassword(), customer.get().getPassword())) {
            throw new UpdateCustomerSamePasswordException();
        }
        customer.get().setPassword(passwordEncoder.encode(updateCustomerRequest.getPassword()));
        customer.get().setTelephone(updateCustomerRequest.getTelephone());
        customerService.update(customer.get());
        return new GeneralSuccessfullResponse(CustomerConstant.UPDATE_SUCESSFULL);
    }

    @Override
    public GeneralResponse delete(long id) throws CustomerNotFoundException, CustomerDeleteException {
        boolean existCustomer = customerService.existCustomerById(id);
        if (!existCustomer) {
            throw new CustomerNotFoundException();
        }
        boolean checkCustomerHasMoneyInDepositAccount = depositAccountService.checkCustomerHasMoneyInDepositAccounts(id);
        if (checkCustomerHasMoneyInDepositAccount) {
            throw new CustomerDeleteException(CustomerConstant.DELETE_CUSTOMER_OPERATION_HAS_BALANCE_EXCEPTION);
        }
        log.info("customer deleted ");
        customerService.delete(id);
        return new GeneralSuccessfullResponse("Customer deleted.");
    }
}
