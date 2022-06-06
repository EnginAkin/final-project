package org.norma.finalproject.card.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

/**
 * @author Engin Akin
 * @version v1.0.0
 * @since version v1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DebitFacadeServiceImpl implements DebitFacadeService {

    private final CustomerService customerService;
    private final CheckingAccountService checkingAccountService;
    private final UniqueNoCreator uniqueNoCreator;
    private final DebitCardService debitCardService;
    private final DebitCardMapper debitCardMapper;
    private final AccountActivityMapper accountActivityMapper;


    @Override
    public GeneralDataResponse create(long customerID, CreateDebitCardRequest createDebitCardRequest) throws DebitCardOperationException, CheckingAccountNotFoundException, CustomerNotFoundException {
        log.debug("create debit card started.");
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            log.error("Customer not found.");
            throw new CustomerNotFoundException();
        }
        Optional<CheckingAccount> optionalCheckingAccount = checkingAccountService.findById(createDebitCardRequest.getParentCheckingAccountId());
        if (optionalCheckingAccount.isEmpty() || !(optionalCheckingAccount.get().getCustomer().getId().equals(customerID))) {
            log.error(Messages.CHECKING_ACCOUNT_NOT_FOUND);
            throw new CheckingAccountNotFoundException(Messages.CHECKING_ACCOUNT_NOT_FOUND);
        }
        boolean existsDebitCardByCheckingAccountId = debitCardService.existsDebitCardByCheckingAccountId(createDebitCardRequest.getParentCheckingAccountId());
        if (existsDebitCardByCheckingAccountId) {
            log.error(Messages.DEBIT_CARD_OPERATION_ONE_ACCOUNT_MUST_ONE_CARD_EXCEPTION);
            throw new DebitCardOperationException(Messages.DEBIT_CARD_OPERATION_ONE_ACCOUNT_MUST_ONE_CARD_EXCEPTION);
        }
        DebitCard debitCard = new DebitCard();
        debitCard.setCardNumber(uniqueNoCreator.creatAccountNo());
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
        log.debug("create debit card ended.");
        return new GeneralDataResponse(debitCardResponse);


    }

    @Override
    public GeneralResponse getDebitCardActivities(Long customerID, long debitCardID, ActivityFilter filter) throws CustomerNotFoundException, DebitCardNotFoundException {
        log.debug("Get debit card activities started.");
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            log.error("Customer not found.");
            throw new CustomerNotFoundException();
        }
        Optional<DebitCard> optionalDebitCard = debitCardService.findDebitCardWithCustomerIDAndCardID(customerID, debitCardID);
        if (optionalDebitCard.isEmpty()) {
            log.error("Debit card not found.");
            throw new DebitCardNotFoundException();
        }
        if (filter == null) {
            Date today = new Date();
            Date aMonthAgo = Utils.get30DaysAgo(today); // get 30 day ago from today
            filter = new ActivityFilter(aMonthAgo, today); //  default filter a month ago
        }
        List<AccountActivity> accountActivities = optionalDebitCard.get().getCheckingAccount().getActivityWithFilterDate(filter);
        List<AccountActivityResponse> responseList = accountActivities.stream().map(accountActivityMapper::toDto).toList();
        log.info("End of the debit card activities.");
        return new GeneralDataResponse<>(responseList);
    }

    @Override
    public GeneralDataResponse getDebitCardByID(Long customerID, long debitID) throws DebitCardNotFoundException, CustomerNotFoundException {
        log.debug("Get debit card by id started.");
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        Optional<DebitCard> optionalDebitCard = debitCardService.findDebitCardWithCustomerIDAndCardID(customerID, debitID);
        if (optionalDebitCard.isEmpty()) {
            log.error("Debit card not found.");
            throw new DebitCardNotFoundException();
        }
        DebitCardResponse debitCardResponse = debitCardMapper.toDto(optionalDebitCard.get());
        log.debug("Get debit card by id ended.");
        return new GeneralDataResponse(debitCardResponse);
    }

    @Override
    public GeneralDataResponse getAllCustomerDebitCards(Long customerID) throws CustomerNotFoundException, DebitCardNotFoundException {
        log.debug("Get all customer Debit Cards by id started.");
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            log.error("Customer not found.");
            throw new CustomerNotFoundException();
        }
        List<DebitCard> debitCards = debitCardService.getAllCustomersDebitCards(customerID);
        if (debitCards.isEmpty()) {
            log.error("Debit card not found.");
            throw new DebitCardNotFoundException();
        }
        List<DebitCardResponse> orders = debitCards.stream()
                .map(debitCardMapper::toDto)
                .toList();
        log.debug("Get all customer Debit Cards by id ended.");
        return new GeneralDataResponse<>(orders);
    }

    @Override
    public GeneralResponse update(Long customerID, long debitCardID, UpdateDebitCardRequest updateDebitCardRequest) throws CustomerNotFoundException, DebitCardOperationException {

        log.debug("Update Debit Card started.");
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            log.error("Customer not found.");
            throw new CustomerNotFoundException();
        }
        Optional<DebitCard> optionalDebitCard = debitCardService.findDebitCardWithCustomerIDAndCardID(customerID, debitCardID);
        if (optionalDebitCard.isEmpty()) {
            log.error("Debit card not found.");
            throw new DebitCardOperationException("debit card not found.");
        }
        optionalDebitCard.get().setPassword(updateDebitCardRequest.getPassword());
        debitCardService.save(optionalDebitCard.get());
        log.debug("Update Debit Card ended.");
        return new GeneralSuccessfullResponse("Update successfull");
    }


    @Override
    public GeneralResponse deleteDebitCardById(Long customerID, long debitCardID) throws CustomerNotFoundException, DebitCardOperationException {
        log.debug("Delete debit card by id started.");
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            log.error("Customer not found.");
            throw new CustomerNotFoundException();
        }
        Optional<DebitCard> optionalDebitCard = debitCardService.findDebitCardWithCustomerIDAndCardID(customerID, debitCardID);
        if (optionalDebitCard.isEmpty()) {
            log.error("Debit card not found.");
            throw new DebitCardOperationException("debit card not found.");
        }
        if (optionalDebitCard.get().getCheckingAccount().getBalance().compareTo(BigDecimal.ZERO) > 0) {
            log.error("Debit card has balance so cannot deleted..");
            throw new DebitCardOperationException("Debit card has a balance.");
        }
        debitCardService.delete(optionalDebitCard.get());
        log.debug("Delete debit card by id ended..");
        return new GeneralSuccessfullResponse("Delete debit card successfully");
    }

}

