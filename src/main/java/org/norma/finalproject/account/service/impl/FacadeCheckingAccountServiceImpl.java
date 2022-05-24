package org.norma.finalproject.account.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.account.core.exception.*;
import org.norma.finalproject.account.core.mapper.AccountActivityMapper;
import org.norma.finalproject.account.core.mapper.CheckingAccountMapper;
import org.norma.finalproject.account.core.model.request.CreateCheckingAccountRequest;
import org.norma.finalproject.account.core.model.response.AccountActivityResponse;
import org.norma.finalproject.account.core.model.response.CheckingAccountResponse;
import org.norma.finalproject.account.core.utils.UniqueNoCreator;
import org.norma.finalproject.account.entity.CheckingAccount;
import org.norma.finalproject.account.entity.base.AccountActivity;
import org.norma.finalproject.account.entity.enums.AccountType;
import org.norma.finalproject.account.service.AccountActivityService;
import org.norma.finalproject.account.service.CheckingAccountService;
import org.norma.finalproject.account.service.FacadeCheckinAccountService;
import org.norma.finalproject.common.response.GeneralDataResponse;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.common.response.GeneralSuccessfullResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FacadeCheckingAccountServiceImpl implements FacadeCheckinAccountService {

    private final CustomerService customerService;
    private final CheckingAccountService checkingAccountService;
    private final AccountActivityService accountActivityService;
    private final UniqueNoCreator uniqueNoCreator;
    private final CheckingAccountMapper checkingAccountMapper;
    private final AccountActivityMapper accountActivityMapper;


    @Override
    public GeneralResponse create(long customerID, CreateCheckingAccountRequest createCheckingAccountRequest) throws CustomerNotFoundException, AccountNameAlreadyHaveException {
        Optional<Customer> optionalCustomer = customerService.findCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        String accountName = createCheckingAccountRequest.getBranchName() + "-" + createCheckingAccountRequest.getBranchCode();

        Optional<CheckingAccount> optionalDepositAccount = existsCheckingAccountByAccountNumber(optionalCustomer.get().getCheckingAccounts(), accountName);
        if (optionalDepositAccount.isPresent()) {
            throw new AccountNameAlreadyHaveException(accountName + " name already have account in your accounts.");
        }
        CheckingAccount checkingAccount = checkingAccountMapper.ToEntity(createCheckingAccountRequest);


        String accountNo = uniqueNoCreator.creatAccountNo();
        checkingAccount.setAccountNo(accountNo);
        String ibanNo = uniqueNoCreator.createIbanNo(accountNo, createCheckingAccountRequest.getBankCode());
        checkingAccount.setIbanNo(ibanNo);
        checkingAccount.setBalance(BigDecimal.ZERO);
        checkingAccount.setCustomer(optionalCustomer.get());
        CheckingAccount savedCheckingAccount = checkingAccountService.save(checkingAccount);

        return new GeneralDataResponse<>(checkingAccountMapper.toCreateCheckingAccountDto(savedCheckingAccount));
    }

    @Override
    public GeneralResponse blockAccount(long customerID) throws DepositAccountNotFoundException {
        Optional<CheckingAccount> depositAccount = checkingAccountService.findById(customerID);
        if (depositAccount.isEmpty()) {
            throw new DepositAccountNotFoundException("Checking Account  Not Found.");
        }
        depositAccount.get().setBlocked(true);
        checkingAccountService.save(depositAccount.get());
        log.info("Customer blocked.transfer authorization removed");
        return new GeneralSuccessfullResponse("Customer Blocked successfull. Customer cannot transfer anymore.");
    }

    @Override
    public GeneralResponse getCheckingAccounts(long customerID) throws CustomerNotFoundException {
        checkCustomerFound(customerID);
        List<CheckingAccount> checkingAccounts = checkingAccountService.getUnBlockedAccounts(customerID);
        List<CheckingAccountResponse> accountResponses = checkingAccounts.stream().map(checkingAccountMapper::toAccountResponses).toList();
        return new GeneralDataResponse<>(accountResponses);
    }

    @Override
    public GeneralResponse getCheckingAccountActivities(long customerID, long accountID) throws CustomerNotFoundException, CheckingAccountNotFoundException {
        checkCustomerFound(customerID);
        // gelen account id ile hesabı var mı

        List<AccountActivity> accountActivities = accountActivityService.getAccountActivitiesByAccountIdAndCustomerID(accountID,customerID, AccountType.CHECKING);
        if(accountActivities.isEmpty()){
            throw new CheckingAccountNotFoundException("account no found by id : "+accountID);
        }
        List<AccountActivityResponse> responseList = accountActivities.stream().map(accountActivityMapper::toDto).toList();
        return new GeneralDataResponse<>(responseList);
    }

    @Override
    public GeneralResponse getCheckingAccountById(Long customerID, long accountID) throws CustomerNotFoundException, CustomerAccountNotFoundException, CheckingAccountNotFoundException {
        checkCustomerFound(customerID);

        Optional<CheckingAccount> checkingAccount = checkingAccountService.findById(accountID);
        if (checkingAccount.isEmpty()) {
            throw new CustomerAccountNotFoundException("Account : " + accountID + " not found");
        }

        Optional<CheckingAccount> optionalCheckingAccount = checkingAccountService.findById(accountID);

        if(optionalCheckingAccount.isEmpty()){
            throw new CheckingAccountNotFoundException("not found");
        }
        CheckingAccountResponse checkingAccountResponse = checkingAccountMapper.toAccountResponses(optionalCheckingAccount.get());
        return new GeneralDataResponse(checkingAccountResponse);

    }

    @Override
    @Transactional
    public GeneralResponse deleteById(long customerID, long accountID) throws CustomerAccountNotFoundException, CustomerNotFoundException, DeleteAccountHasBalanceException, CannotDeleteBlockedAccounException {
        Optional<Customer> optionalCustomer = customerService.findCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }

        Optional<CheckingAccount> checkingAccount = checkingAccountService.findById(accountID);
        if (checkingAccount.isEmpty()) {
            throw new CustomerAccountNotFoundException("Account : " + accountID + " not found");
        }
        if (checkingAccount.get().isBlocked()) {
            throw new CannotDeleteBlockedAccounException();
        }
        if (checkingAccount.get().getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new DeleteAccountHasBalanceException("Balance greater than 0  in account.Cannot be deleted.");
        }
        // TODO bir cehcking hesap silindiğinde ona bağlı kart da silinir.
        // TODO hesap silinmeden önce hesaba bağlı bir birikim hesabı var mı varsa içinde bakiye var mı

        checkingAccountService.deleteCustomerCheckingAccountById(checkingAccount.get());
        return new GeneralSuccessfullResponse("Deleted successfully Customer Deposit account.");
    }

    public Optional<CheckingAccount> existsCheckingAccountByAccountNumber(List<CheckingAccount> checkingAccountList, String accountName) {
        if (checkingAccountList == null) {
            return Optional.empty();
        }
        return checkingAccountList.stream().filter(checkingAccount -> checkingAccount.getAccountNo().equals(accountName)).findFirst();

    }
    private void checkCustomerFound(long customerID) throws CustomerNotFoundException {
        Optional<Customer> optionalCustomer = customerService.findCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
    }


}
