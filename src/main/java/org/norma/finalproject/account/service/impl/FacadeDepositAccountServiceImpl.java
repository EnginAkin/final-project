package org.norma.finalproject.account.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.account.core.exception.*;
import org.norma.finalproject.account.core.mapper.DepositAccountMapper;
import org.norma.finalproject.account.core.model.request.CreateDepositAcoountRequest;
import org.norma.finalproject.account.core.utils.UniqueNoCreator;
import org.norma.finalproject.account.entity.DepositAccount;
import org.norma.finalproject.account.service.DepositAccountService;
import org.norma.finalproject.account.service.FacadeDepositAccountService;
import org.norma.finalproject.common.response.GeneralDataResponse;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.common.response.GeneralSuccessfullResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.service.CustomerService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FacadeDepositAccountServiceImpl implements FacadeDepositAccountService {

    private final CustomerService customerService;
    private final DepositAccountService depositAccountService;

    private final UniqueNoCreator uniqueNoCreator;
    private final DepositAccountMapper mapper;

    @Override
    public GeneralResponse create(Customer customer, CreateDepositAcoountRequest createDepositAcoountRequest) throws CustomerNotFoundException, AccountNameAlreadyHaveException {
        DepositAccount depositAccount = mapper.ToEntity(createDepositAcoountRequest);

        Optional<DepositAccount> optionalDepositAccount = existsCustomerDepositAccountByAccountName(customer.getDepositAccounts(), createDepositAcoountRequest.getAccountName());
        if (optionalDepositAccount.isPresent()) {
            throw new AccountNameAlreadyHaveException(createDepositAcoountRequest.getAccountName() + " name already have account in your accounts.");
        }

        String AccountNo = uniqueNoCreator.createDepositAccountNo();
        depositAccount.setAccountNo(AccountNo);
        String IbanNo = uniqueNoCreator.createDepositIbanNo();
        depositAccount.setIbanNo(IbanNo);
        depositAccount.setBalance(BigDecimal.ZERO);
        depositAccount.setCreatedAt(new Date());
        depositAccount.setCreatedBy("ENGIN AKIN");
        depositAccount.setCustomer(customer);
        DepositAccount savedDepositAccount = depositAccountService.save(depositAccount);
        return new GeneralDataResponse<>(mapper.toDto(savedDepositAccount));
    }

    @Override
    public GeneralResponse blockAccount(long accountId) throws DepositAccountNotFoundException {
        Optional<DepositAccount> depositAccount = depositAccountService.findById(accountId);
        if (depositAccount.isEmpty()) {
            throw new DepositAccountNotFoundException("Deposit Account  Not Found.");
        }
        depositAccount.get().setBlocked(true);
        depositAccountService.save(depositAccount.get());
        log.info("Customer blocked.transfer authorization removed");
        return new GeneralSuccessfullResponse("Customer Blocked successfull. Customer cannot transfer anymore.");
    }

    // TODO by name iptal
    @Override
    public GeneralResponse delete(Customer customer, String accountName) throws CustomerAccountNotFoundException, CustomerNotFoundException, DeleteAccountHasBalanceException, CannotDeleteBlockedAccounException {
        if (customer == null) {
            throw new CustomerNotFoundException();
        }
        Optional<DepositAccount> depositAccount = existsCustomerDepositAccountByAccountName(customer.getDepositAccounts(), accountName);
        if (depositAccount.isEmpty()) {
            throw new CustomerAccountNotFoundException("Account : " + accountName + " not found");
        }
        if(depositAccount.get().isBlocked()){
            throw new CannotDeleteBlockedAccounException();
        }
        if (depositAccount.get().getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new DeleteAccountHasBalanceException("Balance greater than 0 in " + accountName + ".Cannot be deleted.");
        }
        // TODO bir deposit hesap silindiğinde ona bağlı kart da silinir.
        depositAccountService.deleteCustomerDepositAccount(depositAccount.get());
        return new GeneralSuccessfullResponse("Deleted successfully Customer Deposit account.");
    }

    public Optional<DepositAccount> existsCustomerDepositAccountByAccountName(List<DepositAccount> depositAccountList, String accountName) {
        return depositAccountList.stream().filter(depositAccount -> depositAccount.getAccountName().equals(accountName)).findFirst();
    }


}
