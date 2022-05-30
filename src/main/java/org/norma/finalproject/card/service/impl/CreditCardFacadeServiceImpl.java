package org.norma.finalproject.card.service.impl;

import lombok.RequiredArgsConstructor;
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
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.service.CustomerService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CreditCardFacadeServiceImpl implements CreditCardFacadeService {
    private final CustomerService customerService;
    private final CreditLimitCalculator<BigDecimal> creditLimitCalculatorByIncome;
    private final UniqueNoCreator uniqueNoCreator;
    private final CreditCardService creditCardService;
    private final CreditCardMapper creditCardMapper;

    @Override
    public GeneralResponse create(long customerID, CreateCreditCardRequest createCreditCardRequest) throws CustomerNotFoundException, CreditCardOperationException {
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if(optionalCustomer.isEmpty()){
            throw new CustomerNotFoundException();
        }
        BigDecimal income=optionalCustomer.get().getIncome();
        BigDecimal creditLimit = creditLimitCalculatorByIncome.getCreditLimit(income,createCreditCardRequest.getCreditCardLimit());
        if(creditLimit.equals(BigDecimal.ZERO)){
            throw new CreditCardOperationException("you are very risky so we cant give credit card");
        }
        CreditCard creditCard=new CreditCard();
        creditCard.setCardNumber(uniqueNoCreator.creatCardNumber());
        creditCard.setCustomer(optionalCustomer.get());
        Random random = new Random();
        int cvv = random.nextInt(900) + 100;
        creditCard.setCvv(String.valueOf(cvv));
        Calendar expiry = Calendar.getInstance();
        expiry.add(Calendar.YEAR, 3); // 3 year expiry date
        creditCard.setExpiryDate(expiry.getTime());
        creditCard.setPassword(createCreditCardRequest.getPassword());
        CreditCardAccount  creditCardAccount=createCreditAccount(); // default create credit card account.
        creditCardAccount.setTotalCreditLimit(creditLimit);
        creditCardAccount.setAvailableBalance(creditLimit);
        creditCard.setCreditCardAccount(creditCardAccount);
        CreditCard savedCreditCard = creditCardService.save(creditCard);
        CreditCardResponse creditCardResponse = creditCardMapper.toCreditCardResponse(savedCreditCard);
        return new GeneralDataResponse<>(creditCardResponse);
    }

    @Override
    public GeneralResponse getCurrentTermTransactions(Long customerID, long creditCardId) throws CustomerNotFoundException, CreditCardNotFoundException, CreditCardOperationException {
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if(optionalCustomer.isEmpty()){
            throw new CustomerNotFoundException();
        }
        Optional<CreditCard> optionalCreditCard = creditCardService.findCreditCardById(creditCardId);
        if(optionalCreditCard.isEmpty()){
            throw new CreditCardNotFoundException();
        }
        if(!(optionalCreditCard.get().getCustomer().getId().equals(customerID))){
            throw new CreditCardOperationException("Credit not found in customers credit cards.");
        }
        List<CreditCardActivityResponse> responseList = optionalCreditCard.get().getCreditCardAccount().getCurrentTermExtract().getCreditCardActivities().stream().map(creditCardMapper::toCreditCardResponse).toList();
        return new GeneralDataResponse(responseList);

    }

    @Override
    public GeneralResponse getCustomerCreditCards(Long customerID) throws CustomerNotFoundException {
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if(optionalCustomer.isEmpty()){
            throw new CustomerNotFoundException();
        }
        List<CreditCardResponse> creditCardResponses = optionalCustomer.get().getCreditCards().stream().map(creditCardMapper::toCreditCardResponse).toList();
        return new GeneralDataResponse(creditCardResponses);
    }

    @Override
    public GeneralResponse getCreditCardDebt(Long customerID, long creditCardID) throws CustomerNotFoundException, CreditCardOperationException, CreditCardNotFoundException {
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if(optionalCustomer.isEmpty()){
            throw new CustomerNotFoundException();
        }
        Optional<CreditCard> optionalCreditCard = creditCardService.findCreditCardById(creditCardID);
        if(optionalCreditCard.isEmpty()){
            throw new CreditCardNotFoundException();
        }
        if(!(optionalCreditCard.get().getCustomer().getId().equals(customerID))){
            throw new CreditCardOperationException("Credit not found in customers credit cards.");
        }
        CreditCardAccount creditCardAccount=optionalCreditCard.get().getCreditCardAccount();

        CreditCardDebtResponse debtResponse=new CreditCardDebtResponse();
        debtResponse.setCurrentTermDebt(creditCardAccount.getTotalDebt().subtract(creditCardAccount.getLastExtractDebt()));
        debtResponse.setTotalDebt(creditCardAccount.getTotalDebt());
        debtResponse.setLastExtractDebt(creditCardAccount.getLastExtractDebt());
        return new GeneralDataResponse<>(debtResponse);

    }

    private CreditCardAccount createCreditAccount(){
        CreditCardAccount creditCardAccount=new CreditCardAccount();
        creditCardAccount.setTotalDebt(BigDecimal.ZERO);
        creditCardAccount.setCutOffDate(new Date());
        creditCardAccount.setLastExtractDebt(BigDecimal.ZERO);
        LocalDate localDate=creditCardAccount.getCutOffDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        localDate=localDate.plusDays(10);
        creditCardAccount.setPaymentDate(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));// set 10 days later payment date
        return creditCardAccount;

    }
}
