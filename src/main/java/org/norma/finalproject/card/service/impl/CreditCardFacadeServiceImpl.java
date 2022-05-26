package org.norma.finalproject.card.service.impl;

import lombok.RequiredArgsConstructor;
import org.norma.finalproject.account.core.utils.UniqueNoCreator;
import org.norma.finalproject.card.core.mapper.CreditCardMapper;
import org.norma.finalproject.card.core.model.request.CreateCreditCardRequest;
import org.norma.finalproject.card.core.model.response.CreditCardResponse;
import org.norma.finalproject.card.entity.CreditCard;
import org.norma.finalproject.card.entity.CreditCardAccount;
import org.norma.finalproject.card.service.CreditCardFacadeService;
import org.norma.finalproject.card.service.CreditCardService;
import org.norma.finalproject.card.service.CreditLimitCalculator;
import org.norma.finalproject.card.service.CreditRateService;
import org.norma.finalproject.common.response.GeneralDataResponse;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CreditCardFacadeServiceImpl implements CreditCardFacadeService {
    private final CustomerService customerService;
    private final CreditLimitCalculator<BigDecimal> creditLimitCalculatorByIncome;
    private final UniqueNoCreator uniqueNoCreator;
    private final CreditCardService creditCardService;
    private final CreditCardMapper creditCardMapper;

    @Override
    public GeneralResponse create(long userID, CreateCreditCardRequest createCreditCardRequest) throws CustomerNotFoundException {
        Optional<Customer> optionalCustomer = customerService.findCustomerById(userID);
        if(optionalCustomer.isEmpty()){
            throw new CustomerNotFoundException();
        }
        BigDecimal income=optionalCustomer.get().getIncome();
        BigDecimal creditLimit = creditLimitCalculatorByIncome.getCreditLimit(income);
        CreditCard creditCard=new CreditCard();
        creditCard.setCardNumber(uniqueNoCreator.creatCardNumber());
        creditCard.setCustomer(optionalCustomer.get());
        Random random = new Random();
        int cvv = random.nextInt(900) + 100;
        creditCard.setCVV(String.valueOf(cvv));
        creditCard.setPassword(creditCard.getPassword());
        CreditCardAccount creditCardAccount=new CreditCardAccount();
        creditCardAccount.setCurrentDebt(BigDecimal.ZERO);
        creditCardAccount.setAvailableBalance(BigDecimal.ZERO);
        creditCardAccount.setCutOffDate(createCreditCardRequest.getCutOffDate());
        creditCardAccount.setTotalCreditLimit(creditLimit);
        creditCardAccount.setLastExtractDebt(BigDecimal.ZERO);
        LocalDate localDate=createCreditCardRequest.getCutOffDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        localDate=localDate.plusDays(10);
        creditCardAccount.setPaymentDate(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));// set 10 days later payment date
        creditCard.setCreditCardAccount(creditCardAccount);
        CreditCard savedCreditCard = creditCardService.save(creditCard);
        CreditCardResponse creditCardResponse = creditCardMapper.toCreditCardResponse(savedCreditCard);
        return new GeneralDataResponse<>(creditCardResponse);
    }
}
