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
import org.norma.finalproject.common.response.GeneralDataResponse;
import org.norma.finalproject.common.response.GeneralErrorResponse;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.common.response.GeneralSuccessfullResponse;
import org.norma.finalproject.customer.core.exception.ActivitiesNotFoundException;
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
            throw new AccountNameAlreadyHaveException("You have a already bank account in this bank same currency and same branch .");
        }
        CheckingAccount checkingAccount = checkingAccountMapper.toEntity(createCheckingAccountRequest);

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
    public GeneralResponse blockAccount(long accountId) throws CheckingAccountNotFoundException {
        Optional<CheckingAccount> checkingAccount = checkingAccountService.findById(accountId);
        if (checkingAccount.isEmpty()) {
            throw new CheckingAccountNotFoundException("Checking Account  Not Found.");
        }
        checkingAccount.get().setBlocked(true);
        checkingAccountService.save(checkingAccount.get());
        log.info("Customer blocked.transfer authorization removed");
        return new GeneralSuccessfullResponse("Customer Blocked successfull. Customer cannot transfer anymore.");
    }

    @Override
    public GeneralResponse getCheckingAccounts(long customerID) throws CustomerNotFoundException {
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        List<CheckingAccount> checkingAccounts = checkingAccountService.getUnBlockedAccounts(customerID);
        List<CheckingAccountResponse> accountResponses = checkingAccounts.stream().map(checkingAccountMapper::toAccountResponses).toList();
        return new GeneralDataResponse<>(accountResponses);
    }

    @Override
    public GeneralResponse getCheckingAccountActivities(long customerID, long accountID) throws CustomerNotFoundException, ActivitiesNotFoundException {
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }

        Optional<CheckingAccount> optionalCheckingAccount = checkingAccountService.findById(accountID);
        if (optionalCheckingAccount.isEmpty()) {
            return new GeneralErrorResponse("boş");
        }
        boolean checkAccountOwnersIsCustomer = checkAccountOwnersIsCustomer(optionalCustomer.get().getCheckingAccounts(), accountID);
        if (!checkAccountOwnersIsCustomer) {
            throw new ActivitiesNotFoundException();
        }
        List<AccountActivity> accountActivities = optionalCheckingAccount.get().getActivities().stream().toList();
        if (accountActivities.isEmpty()) {
            throw new ActivitiesNotFoundException();
        }
        List<AccountActivityResponse> responseList = accountActivities.stream().map(accountActivityMapper::toDto).toList();
        return new GeneralDataResponse<>(responseList);
    }


    @Override
    public GeneralResponse getCheckingAccountById(Long customerID, long accountID) throws CustomerNotFoundException, CustomerAccountNotFoundException, CheckingAccountNotFoundException {
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }

        Optional<CheckingAccount> checkingAccount = checkingAccountService.findById(accountID);
        if (checkingAccount.isEmpty()) {
            throw new CustomerAccountNotFoundException("Account : " + accountID + " not found");
        }

        Optional<CheckingAccount> optionalCheckingAccount = checkingAccountService.findById(accountID);

        if (optionalCheckingAccount.isEmpty()) {
            throw new CheckingAccountNotFoundException("not found");
        }
        CheckingAccountResponse checkingAccountResponse = checkingAccountMapper.toAccountResponses(optionalCheckingAccount.get());
        return new GeneralDataResponse(checkingAccountResponse);

    }

    @Override
    @Transactional
    public GeneralResponse deleteById(long customerID, long accountID) throws CustomerAccountNotFoundException, CustomerNotFoundException, DeleteAccountHasBalanceException, CannotDeleteBlockedAccounException, SavingAccountNotFound {
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
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

        facadeSavingAccountService.deleteByCheckingParentId(checkingAccount.get().getId());

        checkingAccountService.deleteCustomerCheckingAccountById(checkingAccount.get());
        return new GeneralSuccessfullResponse("Deleted successfully Checking account.");
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
