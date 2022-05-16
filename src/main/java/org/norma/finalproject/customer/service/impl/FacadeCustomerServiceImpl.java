package org.norma.finalproject.customer.service.impl;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.common.core.mapper.ModelMapperService;
import org.norma.finalproject.common.response.GeneralDataResponse;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.common.response.GeneralSuccessfullResponse;
import org.norma.finalproject.customer.core.exception.customer.IdentityNotValidException;
import org.norma.finalproject.customer.core.exception.customer.NotAcceptableAgeException;
import org.norma.finalproject.customer.core.model.request.CreateCustomerRequest;
import org.norma.finalproject.customer.core.utilities.Utils;
import org.norma.finalproject.customer.entity.Address;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.service.CustomerService;
import org.norma.finalproject.customer.service.FacadeCustomerService;
import org.norma.finalproject.customer.service.IdentityVerifier;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FacadeCustomerServiceImpl implements FacadeCustomerService {

    private final CustomerService customerService;
    private final IdentityVerifier identityVerifier;

    private final ModelMapperService modelMapper;
    @Override
    public GeneralResponse signup(CreateCustomerRequest createCustomerRequest) throws IdentityNotValidException, NotAcceptableAgeException {
        Customer customer = modelMapper.forDto().map(createCustomerRequest,Customer.class);

        boolean verifyIdentityNumber = identityVerifier.verify(customer.getIdentityNumber());
        if(!verifyIdentityNumber){
            throw new IdentityNotValidException();
        }
        boolean isOver18YearsOld = Utils.isOver18YearsOld(customer.getBirthDay());
        if(!isOver18YearsOld){
            throw new NotAcceptableAgeException();
        }

        customerService.save(customer);

        return new GeneralSuccessfullResponse("customer created successfull");
    }
}
