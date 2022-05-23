package org.norma.finalproject.transfer.service;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.account.service.BaseAccountService;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.service.CustomerService;
import org.norma.finalproject.transfer.core.model.request.CreateTransferRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FacadeTransferService<T> implements TransferService<CreateTransferRequest> {

    private final CustomerService customerService;
    private final BaseAccountService accountService;


    @Override
    public GeneralResponse transfer(long customerId,CreateTransferRequest transferRequest) throws CustomerNotFoundException {
        Optional<Customer> optionalCustomer = customerService.getCustomerById(customerId);
        if(optionalCustomer.isEmpty()){
            throw new CustomerNotFoundException();
        }
        // from iban kullanıcının hesabı mı ?
        //accountService.getAccountByIban(transferRequest.getFromIban());
        // gondericinin hesabında yeterli para var mı ?
        // to iban var mı
        // to iban ile from iban arasında currency sağlanıyor mu
        // from ibandan para düşülür.
        // to ibana para eklenir.
        // from ibanın hesap haraktlerine ekleni.
        // from ibanın hesap hareketlerine eklenir.

        return null;
    }
}
