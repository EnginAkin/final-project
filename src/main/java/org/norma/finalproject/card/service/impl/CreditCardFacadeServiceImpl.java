package org.norma.finalproject.card.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.account.core.utils.UniqueNoCreator;
import org.norma.finalproject.card.core.exception.CreditCardNotFoundException;
import org.norma.finalproject.card.core.exception.CreditCardOperationException;
import org.norma.finalproject.card.core.mapper.CreditCardMapper;
import org.norma.finalproject.card.core.model.request.CreateCreditCardRequest;
import org.norma.finalproject.card.core.model.response.CreditCardActivityResponse;
import org.norma.finalproject.card.core.model.response.CreditCardDebtResponse;
import org.norma.finalproject.card.core.model.response.CreditCardResponse;
import org.norma.finalproject.card.entity.CreditCard;
import org.norma.finalproject.card.entity.CreditCardAccount;
import org.norma.finalproject.card.service.CreditCardFacadeService;
import org.norma.finalproject.card.service.CreditCardService;
import org.norma.finalproject.card.service.CreditLimitCalculator;
import org.norma.finalproject.common.core.result.GeneralDataResponse;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.common.core.result.GeneralSuccessfullResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.service.CustomerService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * implementation of credit card facade service for credit card crud process
 *
 * @author Engin Akin
 * @version v1.0.0
 * @since version v1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CreditCardFacadeServiceImpl implements CreditCardFacadeService {
    private final CustomerService customerService;
    private final CreditLimitCalculator<BigDecimal> creditLimitCalculatorByIncome;
    private final UniqueNoCreator uniqueNoCreator;
    private final CreditCardService creditCardService;
    private final CreditCardMapper creditCardMapper;

    @Override
    public GeneralResponse create(long userID, CreateCreditCardRequest createCreditCardRequest) throws CustomerNotFoundException, CreditCardOperationException {
        log.debug("Create credit card started.");
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(userID);
        if (optionalCustomer.isEmpty()) {
            log.error("Customer not found");
            throw new CustomerNotFoundException();
        }
        BigDecimal income = optionalCustomer.get().getIncome();
        BigDecimal creditLimit = creditLimitCalculatorByIncome.getCreditLimit(income, createCreditCardRequest.getCreditCardLimit());
        if (creditLimit.equals(BigDecimal.ZERO)) {
            log.error("you are very risky so we cant give credit card");
            throw new CreditCardOperationException("you are very risky so we cant give credit card");
        }
        CreditCard creditCard = new CreditCard();
        creditCard.setCardNumber(uniqueNoCreator.creatCardNumber());
        creditCard.setCustomer(optionalCustomer.get());
        Random random = new Random();
        int cvv = random.nextInt(900) + 100;
        creditCard.setCvv(String.valueOf(cvv));
        Calendar expiry = Calendar.getInstance();
        expiry.add(Calendar.YEAR, 3); // 3 year expiry date
        creditCard.setExpiryDate(expiry.getTime());
        creditCard.setPassword(createCreditCardRequest.getPassword());
        CreditCardAccount creditCardAccount = createCreditAccount(); // default create credit card account.
        creditCardAccount.setTotalCreditLimit(creditLimit);
        creditCardAccount.setAvailableBalance(creditLimit);
        creditCard.setCreditCardAccount(creditCardAccount);
        CreditCard savedCreditCard = creditCardService.save(creditCard);
        CreditCardResponse creditCardResponse = creditCardMapper.toCreditCardActivityResponse(savedCreditCard);
        log.debug("Create credit card ended..");
        return new GeneralDataResponse<>(creditCardResponse);
    }

    @Override
    public GeneralResponse getCurrentTermTransactions(Long userID, long creditCardId) throws CustomerNotFoundException, CreditCardNotFoundException, CreditCardOperationException {
        log.debug("get current term transaction started.");
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(userID);
        if (optionalCustomer.isEmpty()) {
            log.error("Customer not found");
            throw new CustomerNotFoundException();
        }
        Optional<CreditCard> optionalCreditCard = creditCardService.findCreditCardById(creditCardId);
        if (optionalCreditCard.isEmpty()) {
            log.error("Credit card not found");
            throw new CreditCardNotFoundException();
        }
        if (!(optionalCreditCard.get().getCustomer().getId().equals(userID))) {
            log.error("Credit not found in customers credit cards.");
            throw new CreditCardOperationException("Credit not found in customers credit cards.");
        }
        List<CreditCardActivityResponse> responseList = optionalCreditCard.get().getCreditCardAccount().getCurrentTermExtract().getCreditCardActivities().stream().map(creditCardMapper::toCreditCardActivityResponse).toList();
        log.debug("get current term transaction ended.");
        return new GeneralDataResponse(responseList);

    }

    @Override
    public GeneralDataResponse getCustomerCreditCards(Long userID) throws CustomerNotFoundException {
        log.debug("Get customer credit cards started.");
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(userID);
        if (optionalCustomer.isEmpty()) {
            log.error("Customer not found");
            throw new CustomerNotFoundException();
        }
        List<CreditCardResponse> creditCardResponses = optionalCustomer.get().getCreditCards().stream().map(creditCardMapper::toCreditCardActivityResponse).toList();
        log.debug("Get customer credit cards ended.");
        return new GeneralDataResponse(creditCardResponses);
    }

    @Override
    public GeneralResponse getCreditCardDebt(Long userID, long creditCardID) throws CustomerNotFoundException, CreditCardOperationException, CreditCardNotFoundException {
        log.debug("Get Credit card debt started.");
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(userID);
        if (optionalCustomer.isEmpty()) {
            log.error("Customer not found");
            throw new CustomerNotFoundException();
        }
        Optional<CreditCard> optionalCreditCard = creditCardService.findCreditCardById(creditCardID);
        if (optionalCreditCard.isEmpty()) {
            log.error("Credit card not found");
            throw new CreditCardNotFoundException();
        }
        if (!(optionalCreditCard.get().getCustomer().getId().equals(userID))) {
            log.error("Credit not found in customers credit cards.");
            throw new CreditCardOperationException("Credit not found in customers credit cards.");
        }
        CreditCardAccount creditCardAccount = optionalCreditCard.get().getCreditCardAccount();

        CreditCardDebtResponse debtResponse = new CreditCardDebtResponse();
        debtResponse.setCurrentTermDebt(creditCardAccount.getTotalDebt().subtract(creditCardAccount.getLastExtractDebt()));
        debtResponse.setTotalDebt(creditCardAccount.getTotalDebt());
        debtResponse.setLastExtractDebt(creditCardAccount.getLastExtractDebt());
        log.debug("Get Credit card debt ended.");
        return new GeneralDataResponse<>(debtResponse);

    }

    @Override
    public GeneralResponse deleteCreditCard(Long userID, long creditCardId) throws CustomerNotFoundException, CreditCardNotFoundException, CreditCardOperationException {
        log.debug("Delete credit card started.");
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(userID);
        if (optionalCustomer.isEmpty()) {
            log.error("Customer not found");
            throw new CustomerNotFoundException();
        }
        Optional<CreditCard> optionalCreditCard = creditCardService.findCreditCardById(creditCardId);
        if (optionalCreditCard.isEmpty()) {
            log.error("Credit card not found");
            throw new CreditCardNotFoundException();
        }
        if (!(optionalCreditCard.get().getCustomer().getId().equals(userID))) {
            log.error("Credit not found in customers credit cards.");
            throw new CreditCardOperationException("Credit not found in customers credit cards.");
        }
        if (optionalCreditCard.get().getCreditCardAccount().getTotalDebt().compareTo(BigDecimal.ZERO) > 0) {
            log.error("Credit has a debt. you cannot delete credit card");
            throw new CreditCardOperationException("Credit has a debt. you cannot delete credit card.");
        }
        creditCardService.delete(optionalCreditCard.get());
        log.debug("Delete credit card ended.");
        return new GeneralSuccessfullResponse("Delete successfully");
    }

    private CreditCardAccount createCreditAccount() {
        CreditCardAccount creditCardAccount = new CreditCardAccount();
        creditCardAccount.setTotalDebt(BigDecimal.ZERO);

        Calendar cutOffDay = Calendar.getInstance();
        cutOffDay.add(Calendar.DAY_OF_MONTH, 30); // 3 year expiry date

        creditCardAccount.setCutOffDate(cutOffDay.getTime());
        creditCardAccount.setLastExtractDebt(BigDecimal.ZERO);
        LocalDate localDate = creditCardAccount.getCutOffDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        localDate = localDate.plusDays(10);
        creditCardAccount.setPaymentDate(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));// set 10 days later payment date
        return creditCardAccount;

    }
}
