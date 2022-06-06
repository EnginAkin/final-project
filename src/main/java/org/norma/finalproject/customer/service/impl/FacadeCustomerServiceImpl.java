package org.norma.finalproject.customer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.account.core.exception.AccountNameAlreadyHaveException;
import org.norma.finalproject.account.service.FacadeCheckinAccountService;
import org.norma.finalproject.common.core.result.GeneralDataResponse;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.common.core.result.GeneralSuccessfullResponse;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author Engin Akin
 * @version v1.0.0
 * @since version v1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FacadeCustomerServiceImpl implements FacadeCustomerService {

    private final CustomerService customerService;
    private final IdentityVerifier identityVerifier;
    private final PasswordEncoder passwordEncoder;
    private final CustomerMapper customerMapper;
    private final FacadeCheckinAccountService facadeCheckinAccountService;


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public GeneralResponse signup(CreateCustomerRequest createCustomerRequest) throws NotAcceptableAgeException, IdentityNotValidException, CustomerAlreadyRegisterException, CustomerNotFoundException, AccountNameAlreadyHaveException {
        Customer customer = customerMapper.customerInfoDtoToCustomer(createCustomerRequest);

        boolean verifyIdentityNumber = identityVerifier.verify(customer.getIdentityNumber());
        if (!verifyIdentityNumber) {
            throw new IdentityNotValidException();
        }
        boolean isOver18YearsOld = Utils.isOver18YearsOld(customer.getBirthDay());
        if (!isOver18YearsOld) {
            throw new NotAcceptableAgeException();
        }
        Customer savedCustomer = customerService.save(customer);
        facadeCheckinAccountService.create(savedCustomer.getId(), createCustomerRequest.getCheckingAccount());
        return new GeneralSuccessfullResponse(CustomerConstant.SIGNUP_SUCESSFULL);
    }

    @Override
    public GeneralResponse update(Long customerId, UpdateCustomerRequest updateCustomerRequest) throws CustomerNotFoundException, UpdateCustomerSamePasswordException {
        Optional<Customer> customer = customerService.findByCustomerById(customerId);
        if (customer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        if (passwordEncoder.matches(updateCustomerRequest.getPassword(), customer.get().getPassword())) {
            throw new UpdateCustomerSamePasswordException();
        }
        if (updateCustomerRequest.getTelephone() != null) {
            customer.get().setTelephone(updateCustomerRequest.getTelephone());
        }
        customer.get().setPassword(passwordEncoder.encode(updateCustomerRequest.getPassword()));

        customerService.update(customer.get());
        return new GeneralSuccessfullResponse(CustomerConstant.UPDATE_SUCESSFULL);
    }

    @Override
    public GeneralResponse delete(Long customerId) throws CustomerNotFoundException, CustomerDeleteException {
        Optional<Customer> customer = customerService.findByCustomerById(customerId);
        if (customer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        boolean checkHasMoneyInCustomerAccounts = this.checkHasMoneyInCustomerAccounts(customer.get());
        if (checkHasMoneyInCustomerAccounts) {
            throw new CustomerDeleteException(CustomerConstant.DELETE_CUSTOMER_OPERATION_HAS_BALANCE_EXCEPTION);
        }
        customerService.delete(customer.get());
        return new GeneralSuccessfullResponse(CustomerConstant.CUSTOMER_DELETED_SUCCESSFULL);
    }

    @Override
    public GeneralResponse getall() {
        return new GeneralDataResponse<>(customerService.getall());
    }

    public boolean checkHasMoneyInCustomerAccounts(Customer customer) {
        return customer.getCheckingAccounts().stream().
                anyMatch(checkingAccount -> checkingAccount.getBalance().compareTo(BigDecimal.ZERO) > 0)
                || customer.getSavingAccounts().stream().
                anyMatch(savingAccount -> savingAccount.getBalance().compareTo(BigDecimal.ZERO) > 0);
    }
}
