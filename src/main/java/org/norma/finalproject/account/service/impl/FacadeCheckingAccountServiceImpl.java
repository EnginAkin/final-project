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
import org.norma.finalproject.account.entity.enums.CurrencyType;
import org.norma.finalproject.account.service.*;
import org.norma.finalproject.card.core.model.request.ActivityFilter;
import org.norma.finalproject.common.core.result.GeneralDataResponse;
import org.norma.finalproject.common.core.result.GeneralErrorResponse;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.common.core.result.GeneralSuccessfullResponse;
import org.norma.finalproject.common.core.utils.Message;
import org.norma.finalproject.common.core.utils.Utils;
import org.norma.finalproject.customer.core.exception.ActivitiesNotFoundException;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.service.CustomerService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import static org.norma.finalproject.common.core.utils.Utils.get30DaysAgo;

@Service
@RequiredArgsConstructor
@Slf4j
public class FacadeCheckingAccountServiceImpl implements FacadeCheckinAccountService {

    private final CustomerService customerService;
    private final CheckingAccountService checkingAccountService;
    private final FacadeSavingAccountService facadeSavingAccountService;
    private final UniqueNoCreator uniqueNoCreator;
    private final CheckingAccountMapper checkingAccountMapper;
    private final AccountActivityMapper accountActivityMapper;


    @Override
    public GeneralDataResponse create(long customerID, CreateCheckingAccountRequest createCheckingAccountRequest) throws CustomerNotFoundException, AccountNameAlreadyHaveException {
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        String accountName = createCheckingAccountRequest.getBranchName() + "-" + createCheckingAccountRequest.getBranchCode() + "/" + createCheckingAccountRequest.getBankCode();
        Optional<CheckingAccount> optionalDepositAccount = findCheckingAccountByAccountName(optionalCustomer.get().getCheckingAccounts(), accountName, createCheckingAccountRequest.getCurrencyType());
        if (optionalDepositAccount.isPresent()) {
            throw new AccountNameAlreadyHaveException(Message.CHECKING_ACCOUNT_ALREADY_HAVE_ACCOUNT_SAME_EXCEPTION);
        }
        CheckingAccount checkingAccount = checkingAccountMapper.toEntity(createCheckingAccountRequest);

        String accountNo = uniqueNoCreator.creatAccountNo();
        checkingAccount.setAccountNo(accountNo);
        String ibanNo = uniqueNoCreator.createIbanNo(accountNo, createCheckingAccountRequest.getBankCode());
        checkingAccount.setIbanNo(ibanNo);
        checkingAccount.setBalance(BigDecimal.ZERO);
        checkingAccount.setCustomer(optionalCustomer.get());
        CheckingAccount savedCheckingAccount = checkingAccountService.save(checkingAccount);
        log.info("Checking account created successfully");
        return new GeneralDataResponse<>(checkingAccountMapper.toCreateCheckingAccountDto(savedCheckingAccount));
    }

    @Override
    public GeneralResponse blockAccount(long accountId) throws CheckingAccountNotFoundException {
        Optional<CheckingAccount> checkingAccount = checkingAccountService.findById(accountId);
        if (checkingAccount.isEmpty()) {
            throw new CheckingAccountNotFoundException(Message.CHECKING_ACCOUNT_NOT_FOUND);
        }
        checkingAccount.get().setBlocked(true);
        checkingAccountService.save(checkingAccount.get());
        log.info("Checking account blocked.transfer authorization removed");
        return new GeneralSuccessfullResponse(Message.CHECKING_ACCOUNT_BLOCKED_SUCCESSFULLY);
    }

    @Override
    public GeneralResponse getCustomersUnblockedCheckingAccounts(long customerID) throws CustomerNotFoundException {
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        List<CheckingAccount> checkingAccounts = checkingAccountService.getUnBlockedAccounts(customerID);
        List<CheckingAccountResponse> accountResponses = checkingAccounts.stream().map(checkingAccountMapper::toAccountResponses).toList();
        log.info("returned unblocked Checking account successfull.");
        return new GeneralDataResponse<>(accountResponses);
    }

    @Override
    public GeneralResponse getCheckingAccountActivities(long customerID, long accountID, ActivityFilter filter) throws CustomerNotFoundException, ActivitiesNotFoundException, CheckingAccountNotFoundException {
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }

        Optional<CheckingAccount> optionalCheckingAccount = checkingAccountService.findById(accountID);
        if (optionalCheckingAccount.isEmpty()) {
            throw new CheckingAccountNotFoundException(Message.CHECKING_ACCOUNT_NOT_FOUND);
        }
        boolean checkAccountOwnersIsCustomer = checkAccountOwnersIsCustomer(optionalCustomer.get().getCheckingAccounts(), accountID);
        if (!checkAccountOwnersIsCustomer) {
            throw new CheckingAccountNotFoundException(Message.CHECKING_ACCOUNT_NOT_FOUND);
        }
        if(filter==null){
            Date today = new Date();
            Date aMonthAgo= Utils.get30DaysAgo(today); // get 30 day ago from today
            filter=new ActivityFilter(aMonthAgo,today); //  default filter a month ago
        }
        List<AccountActivity> accountActivities=optionalCheckingAccount.get().getActivityWithFilter(filter);
        if (accountActivities.isEmpty()) {
            throw new ActivitiesNotFoundException();
        }


        List<AccountActivityResponse> responseList = accountActivities.stream().map(accountActivityMapper::toDto).toList();
        log.info("Checking account activities successfully returned.");
        return new GeneralDataResponse<>(responseList);
    }


    @Override
    public GeneralResponse getCheckingAccountById(Long customerID, long accountID) throws CustomerNotFoundException, CheckingAccountNotFoundException {
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        Optional<CheckingAccount> optionalCheckingAccount = checkingAccountService.findById(accountID);
        if (optionalCheckingAccount.isEmpty()) {
            throw new CheckingAccountNotFoundException(Message.CHECKING_ACCOUNT_NOT_FOUND);
        }
        CheckingAccountResponse checkingAccountResponse = checkingAccountMapper.toAccountResponses(optionalCheckingAccount.get());
        log.info("Checking account : "+optionalCheckingAccount.get().getId()+"  returned");
        return new GeneralDataResponse(checkingAccountResponse);

    }

    @Override
    public GeneralResponse deleteById(long customerID, long accountID) throws CustomerNotFoundException, DeleteAccountHasBalanceException, CannotDeleteBlockedAccounException, SavingAccountNotFound, AccountBalanceGreatherThenZeroException, CheckingAccountNotFoundException {
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        Optional<CheckingAccount> checkingAccount = checkingAccountService.findById(accountID);
        if (checkingAccount.isEmpty()) {
            throw new CheckingAccountNotFoundException(Message.CHECKING_ACCOUNT_NOT_FOUND);
        }
        boolean checkAccountOwnersIsCustomer = checkAccountOwnersIsCustomer(optionalCustomer.get().getCheckingAccounts(), accountID);
        if (!checkAccountOwnersIsCustomer) {
            throw new CheckingAccountNotFoundException(Message.CHECKING_ACCOUNT_NOT_FOUND);
        }
        if (checkingAccount.get().isBlocked()) {
            throw new CannotDeleteBlockedAccounException();
        }
        if (checkingAccount.get().getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new DeleteAccountHasBalanceException(Message.ACCOUNT_HAS_BALANCE_DELETE_EXCEPTION);
        }
        // TODO bir cehcking hesap silindiğinde ona bağlı kart da silinir.

        try{
            facadeSavingAccountService.deleteSavingAccountByCheckingId(checkingAccount.get().getId());
            checkingAccountService.deleteCustomerCheckingAccountById(checkingAccount.get());
            log.info("Delete checking account successfully.");
            return new GeneralSuccessfullResponse(Message.CHECKING_ACCOUNT_DELETED_SUCCESSFULLY);
        }catch (AccountBalanceGreatherThenZeroException balanceException){
            log.info("Checking account has money in Saving account so you can't delete it.");
            throw new AccountBalanceGreatherThenZeroException("Checking account has money in Saving account so you can't delete it.");
        }

    }

    public Optional<CheckingAccount> findCheckingAccountByAccountName(List<CheckingAccount> checkingAccountList, String accountName, CurrencyType currencyType) {
        if (checkingAccountList == null) {
            return Optional.empty();
        }
        return checkingAccountList.stream().filter(checkingAccount -> checkingAccount.getAccountName().equals(accountName) && checkingAccount.getCurrencyType().equals(currencyType)).findFirst();
    }

    private boolean checkAccountOwnersIsCustomer(List<CheckingAccount> checkingAccounts, long accountID) {
        return checkingAccounts.stream().anyMatch(checkingAccount -> checkingAccount.getId().equals(accountID));
    }




}
