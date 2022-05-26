package org.norma.finalproject.card.service.impl;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.account.core.exception.CheckingAccountNotFoundException;
import org.norma.finalproject.account.core.utils.UniqueNoCreator;
import org.norma.finalproject.account.entity.CheckingAccount;
import org.norma.finalproject.account.service.CheckingAccountService;
import org.norma.finalproject.card.core.exception.DebitCardNotFoundException;
import org.norma.finalproject.card.core.exception.DebitCardOperationException;
import org.norma.finalproject.card.core.mapper.DebitCardMapper;
import org.norma.finalproject.card.core.model.request.CreateDebitCardRequest;
import org.norma.finalproject.card.core.model.request.UpdateDebitCardRequest;
import org.norma.finalproject.card.core.model.response.DebitCardResponse;
import org.norma.finalproject.card.entity.DebitCard;
import org.norma.finalproject.card.service.DebitCardService;
import org.norma.finalproject.card.service.DebitFacadeService;
import org.norma.finalproject.common.response.GeneralDataResponse;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.common.response.GeneralSuccessfullResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
@RequiredArgsConstructor
public class DebitFacadeServiceImpl implements DebitFacadeService {

    private final CustomerService customerService;
    private final CheckingAccountService checkingAccountService;
    private final UniqueNoCreator uniqueNoCreator;
    private final DebitCardService debitCardService;
    private final DebitCardMapper debitCardMapper;


    @Override
    public GeneralDataResponse create(long customerID,CreateDebitCardRequest createDebitCardRequest) throws DebitCardOperationException, CheckingAccountNotFoundException, CustomerNotFoundException {

        Optional<Customer> optionalCustomer = customerService.findCustomerById(customerID);
        if(optionalCustomer.isEmpty()){
            throw new CustomerNotFoundException();
        }
        Optional<CheckingAccount> optionalCheckingAccount = checkingAccountService.findById(createDebitCardRequest.getParentCheckingAccountId());
        if(optionalCheckingAccount.isEmpty() || !(optionalCheckingAccount.get().getCustomer().getId().equals(customerID))){
            throw new CheckingAccountNotFoundException("parent checking account not found.");
        }
        boolean existsDebitCardByCheckingAccountId = debitCardService.existsDebitCardByCheckingAccountId(createDebitCardRequest.getParentCheckingAccountId());
        if (existsDebitCardByCheckingAccountId){
            throw new DebitCardOperationException("An account must have only one card.");
        }

        DebitCard debitCard = new DebitCard();
        debitCard.setCardNumber(uniqueNoCreator.creatAccountNo());
        debitCard.setBalance(optionalCheckingAccount.get().getBalance());
        Random random = new Random();
        int cvv = random.nextInt(900) + 100;
        debitCard.setCvv(String.valueOf(cvv));
        debitCard.setCheckingAccount(optionalCheckingAccount.get());
        debitCard.setPassword(createDebitCardRequest.getPassword());
        Calendar expiry = Calendar.getInstance();
        expiry.add(Calendar.YEAR, 3); // 3 year expiry date
        debitCard.setExpiryDate(expiry.getTime());
        DebitCard savedDebitCard = debitCardService.save(debitCard);
        DebitCardResponse debitCardResponse = debitCardMapper.toDto(savedDebitCard);
        return new GeneralDataResponse(debitCardResponse);


    }

    @Override
    public GeneralDataResponse getByID(Long customerID, long debitID) throws DebitCardNotFoundException {
        Optional<DebitCard> optionalDebitCard = debitCardService.findDebitCardWithCustomerIDAndCardID(customerID, debitID);
        if(optionalDebitCard.isEmpty()){
            throw new DebitCardNotFoundException();
        }
        DebitCardResponse debitCardResponse = debitCardMapper.toDto(optionalDebitCard.get());
        return new GeneralDataResponse(debitCardResponse);
    }

    @Override
    public GeneralDataResponse getAllCustomersDebitCards(Long customerID) throws CustomerNotFoundException, DebitCardOperationException {
        Optional<Customer> optionalCustomer = customerService.findCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        List<DebitCard> debitCards = debitCardService.getAllCustomersDebitCards(customerID);
        if (debitCards.isEmpty()) {
            throw new DebitCardOperationException("Debit cards not found");
        }
        List<DebitCardResponse> orders = debitCards.stream()
                .map(debitCardMapper::toDto)
                .toList();
        return new GeneralDataResponse<>(orders);
    }
    @Override
    public GeneralResponse update(Long customerID, long debitCardID, UpdateDebitCardRequest updateDebitCardRequest) throws CustomerNotFoundException, DebitCardOperationException {
        Optional<Customer> optionalCustomer = customerService.findCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        Optional<DebitCard> optionalDebitCard = debitCardService.findById(debitCardID);
        if (optionalDebitCard.isEmpty()) {
            throw new DebitCardOperationException("debit card not found.");
        }
        optionalDebitCard.get().setPassword(updateDebitCardRequest.getPassword());
        debitCardService.save(optionalDebitCard.get());
        return new GeneralSuccessfullResponse("Update successfull");
    }


}
