package org.norma.finalproject.card.service.impl;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.account.core.exception.CheckingAccountNotFoundException;
import org.norma.finalproject.account.core.mapper.AccountActivityMapper;
import org.norma.finalproject.account.core.model.response.AccountActivityResponse;
import org.norma.finalproject.account.core.utils.UniqueNoCreator;
import org.norma.finalproject.account.entity.CheckingAccount;
import org.norma.finalproject.account.entity.base.AccountActivity;
import org.norma.finalproject.account.service.CheckingAccountService;
import org.norma.finalproject.card.core.exception.DebitCardNotFoundException;
import org.norma.finalproject.card.core.exception.DebitCardOperationException;
import org.norma.finalproject.card.core.mapper.DebitCardMapper;
import org.norma.finalproject.card.core.model.request.ActivityFilter;
import org.norma.finalproject.card.core.model.request.CreateDebitCardRequest;
import org.norma.finalproject.card.core.model.request.UpdateDebitCardRequest;
import org.norma.finalproject.card.core.model.response.DebitCardResponse;
import org.norma.finalproject.card.entity.DebitCard;
import org.norma.finalproject.card.service.DebitCardService;
import org.norma.finalproject.card.service.DebitFacadeService;
import org.norma.finalproject.common.core.result.GeneralDataResponse;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.common.core.result.GeneralSuccessfullResponse;
import org.norma.finalproject.common.core.utils.Messages;
import org.norma.finalproject.common.core.utils.Utils;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.service.CustomerService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;


@Service
@RequiredArgsConstructor
public class DebitFacadeServiceImpl implements DebitFacadeService {

    private final CustomerService customerService;
    private final CheckingAccountService checkingAccountService;
    private final UniqueNoCreator uniqueNoCreator;
    private final DebitCardService debitCardService;
    private final DebitCardMapper debitCardMapper;
    private final AccountActivityMapper accountActivityMapper;


    @Override
    public GeneralDataResponse create(long customerID,CreateDebitCardRequest createDebitCardRequest) throws DebitCardOperationException, CheckingAccountNotFoundException, CustomerNotFoundException {

        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if(optionalCustomer.isEmpty()){
            throw new CustomerNotFoundException();
        }
        Optional<CheckingAccount> optionalCheckingAccount = checkingAccountService.findById(createDebitCardRequest.getParentCheckingAccountId());
        if(optionalCheckingAccount.isEmpty() || !(optionalCheckingAccount.get().getCustomer().getId().equals(customerID))){
            throw new CheckingAccountNotFoundException(Messages.CHECKING_ACCOUNT_NOT_FOUND);
        }
        boolean existsDebitCardByCheckingAccountId = debitCardService.existsDebitCardByCheckingAccountId(createDebitCardRequest.getParentCheckingAccountId());
        if (existsDebitCardByCheckingAccountId){
            throw new DebitCardOperationException(Messages.DEBIT_CARD_OPERATION_ONE_ACCOUNT_MUST_ONE_CARD_EXCEPTION);
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
    public GeneralResponse getDebitCardActivities(Long customerID, long debitCardID,ActivityFilter filter) throws CustomerNotFoundException, DebitCardNotFoundException {
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        Optional<DebitCard> optionalDebitCard = debitCardService.findDebitCardWithCustomerIDAndCardID(customerID,debitCardID);
        if (optionalDebitCard.isEmpty()) {
            throw new DebitCardNotFoundException();
        }
        if(filter==null){
            Date today = new Date();
            Date aMonthAgo= Utils.get30DaysAgo(today); // get 30 day ago from today
            filter=new ActivityFilter(aMonthAgo,today); //  default filter a month ago
        }
        List<AccountActivity> accountActivities=optionalDebitCard.get().getCheckingAccount().getActivityWithFilterDate(filter);
        List<AccountActivityResponse> responseList = accountActivities.stream().map(accountActivityMapper::toDto).toList();
        return new GeneralDataResponse<>(responseList);
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
    public GeneralDataResponse getAllCustomerDebitCards(Long customerID) throws CustomerNotFoundException, DebitCardNotFoundException {
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        List<DebitCard> debitCards = debitCardService.getAllCustomersDebitCards(customerID);
        if (debitCards.isEmpty()) {
            throw new DebitCardNotFoundException();
        }
        List<DebitCardResponse> orders = debitCards.stream()
                .map(debitCardMapper::toDto)
                .toList();
        return new GeneralDataResponse<>(orders);
    }
    @Override
    public GeneralResponse update(Long customerID, long debitCardID, UpdateDebitCardRequest updateDebitCardRequest) throws CustomerNotFoundException, DebitCardOperationException {
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        Optional<DebitCard> optionalDebitCard = debitCardService.findDebitCardWithCustomerIDAndCardID(customerID,debitCardID);
        if (optionalDebitCard.isEmpty()) {
            throw new DebitCardOperationException("debit card not found.");
        }
        optionalDebitCard.get().setPassword(updateDebitCardRequest.getPassword());
        debitCardService.save(optionalDebitCard.get());
        return new GeneralSuccessfullResponse("Update successfull");
    }



    @Override
    public GeneralResponse deleteDebitCardById(Long customerID, long debitCardID) throws CustomerNotFoundException, DebitCardOperationException {
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        Optional<DebitCard> optionalDebitCard = debitCardService.findDebitCardWithCustomerIDAndCardID(customerID,debitCardID);
        if (optionalDebitCard.isEmpty()) {
            throw new DebitCardOperationException("debit card not found.");
        }
        if(optionalDebitCard.get().getBalance().compareTo(BigDecimal.ZERO)>0){
            throw new DebitCardOperationException("Debit card has a balance.");
        }
        debitCardService.delete(optionalDebitCard.get());
        return new GeneralSuccessfullResponse("Delete debit card successfully");
    }

}

// 1. useer   iban TR3300006100522441796955166
