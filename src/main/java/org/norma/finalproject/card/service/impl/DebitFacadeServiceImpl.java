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
import org.norma.finalproject.common.response.GeneralDataResponse;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.common.response.GeneralSuccessfullResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.service.CustomerService;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    public GeneralDataResponse getAllCustomerDebitCards(Long customerID) throws CustomerNotFoundException, DebitCardOperationException {
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
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
        debitCardService.delete(optionalDebitCard.get());
        return new GeneralSuccessfullResponse("Delete debit card successfully");
    }

    @Override
    public GeneralResponse getDebitCardActivities(Long customerID, long debitCardID,ActivityFilter filter) throws CustomerNotFoundException, DebitCardOperationException {
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        Optional<DebitCard> optionalDebitCard = debitCardService.findDebitCardWithCustomerIDAndCardID(customerID,debitCardID);
        if (optionalDebitCard.isEmpty()) {
            throw new DebitCardOperationException("debit card not found.");
        }

        List<AccountActivity> accountActivities=optionalDebitCard.get().getCheckingAccount().getActivities();
        if(filter!=null){
            accountActivities=accountActivities.stream().filter(accountActivity -> accountActivity.getDate().before(filter.getToDate()) &&
                    accountActivity.getDate().after(filter.getFromDate())).toList();
        }else{
            Date today = new Date();
            Calendar cal = new GregorianCalendar();
            cal.setTime(today);
            cal.add(Calendar.DAY_OF_MONTH, -30);// default a month getting activities
            Date aMonthAgo=cal.getTime();
            accountActivities= accountActivities.stream().filter(accountActivity -> accountActivity.getDate().before(today) &&
                            accountActivity.getDate().after(aMonthAgo)).toList();
        }

        List<AccountActivityResponse> responseList = accountActivities.stream().map(accountActivityMapper::toDto).toList();
        return new GeneralDataResponse<>(responseList);
    }


}
/*
1 numaralı hesap jwt
 usd iban TR3300000304980781235685261 4980781235685261
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMTExMTExMTExMSIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJleHAiOjE2NTM3MDY0MzAsImlhdCI6MTY1MzY3NjQzMH0.iIETN6JjrH_epJZZUiQzVVqO0AWOQqzNXxtGKksqVf52ltuGjxH9uLbGnKlcOcKfZIqmDOZaCmArc10-LtVx5g
2. numaralı hesa
TRY TR3300006107252072677675674  7252072677675674
eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMTExMTExMTExMiIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJleHAiOjE2NTM3MTE4MDMsImlhdCI6MTY1MzY4MTgwM30.i3z3njo6ozXDl6xvR5E8HsiAjJM0_-nH18uVNYlNmc_qckUUpYdp5dzGnSJGfaXwXGatwiY-QbGNNUWlpO4RYw
 */