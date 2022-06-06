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
import org.norma.finalproject.account.entity.SavingAccount;
import org.norma.finalproject.account.entity.base.AccountActivity;
import org.norma.finalproject.account.entity.enums.CurrencyType;
import org.norma.finalproject.account.service.CheckingAccountService;
import org.norma.finalproject.account.service.FacadeCheckinAccountService;
import org.norma.finalproject.account.service.SavingAccountService;
import org.norma.finalproject.card.core.model.request.ActivityFilter;
import org.norma.finalproject.card.service.DebitCardService;
import org.norma.finalproject.common.core.result.GeneralDataResponse;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.common.core.result.GeneralSuccessfullResponse;
import org.norma.finalproject.common.core.utils.Messages;
import org.norma.finalproject.common.core.utils.Utils;
import org.norma.finalproject.customer.core.exception.ActivitiesNotFoundException;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Engin Akin
 * @version v1.0.0
 * @since version v1.0.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FacadeCheckingAccountServiceImpl implements FacadeCheckinAccountService {

    private final CustomerService customerService;
    private final CheckingAccountService checkingAccountService;
    private final SavingAccountService savingAccountService;
    private final DebitCardService debitCardService;
    private final UniqueNoCreator uniqueNoCreator;
    private final CheckingAccountMapper checkingAccountMapper;
    private final AccountActivityMapper accountActivityMapper;


    @Override
    public GeneralDataResponse create(long customerID, CreateCheckingAccountRequest createCheckingAccountRequest) throws CustomerNotFoundException, AccountNameAlreadyHaveException {
        log.debug("create function started.");
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            log.error("Customer not found.");
            throw new CustomerNotFoundException();
        }
        Customer customer = optionalCustomer.get();
        String accountName = createCheckingAccountRequest.getBranchName() + "-" + createCheckingAccountRequest.getBranchCode() + "/" + createCheckingAccountRequest.getBankCode();
        Optional<CheckingAccount> optionalDepositAccount = findCheckingAccountByAccountName(customer.getCheckingAccounts(), accountName, createCheckingAccountRequest.getCurrencyType());
        if (optionalDepositAccount.isPresent()) {
            log.error("Checking account already have account has same name.");
            throw new AccountNameAlreadyHaveException(Messages.CHECKING_ACCOUNT_ALREADY_HAVE_ACCOUNT_SAME_EXCEPTION);
        }
        CheckingAccount checkingAccount = checkingAccountMapper.toEntity(createCheckingAccountRequest);

        String accountNo = uniqueNoCreator.creatAccountNo();
        checkingAccount.setAccountNo(accountNo);
        String ibanNo = uniqueNoCreator.createIbanNo(accountNo, createCheckingAccountRequest.getBankCode());
        checkingAccount.setIbanNo(ibanNo);
        checkingAccount.setBalance(BigDecimal.ZERO);
        checkingAccount.setCustomer(customer);
        CheckingAccount savedCheckingAccount = checkingAccountService.save(checkingAccount);
        log.info("Checking account created successfully");
        return new GeneralDataResponse<>(checkingAccountMapper.toCreateCheckingAccountDto(savedCheckingAccount));
    }

    @Override
    public GeneralResponse blockAccount(long accountId) throws CheckingAccountNotFoundException {
        log.debug("Checking account block function started.");
        Optional<CheckingAccount> optionalCheckingAccount = checkingAccountService.findById(accountId);
        if (optionalCheckingAccount.isEmpty()) {
            log.error("Checking account not found.");
            throw new CheckingAccountNotFoundException(Messages.CHECKING_ACCOUNT_NOT_FOUND);
        }
        optionalCheckingAccount.get().setBlocked(true);
        checkingAccountService.save(optionalCheckingAccount.get());
        log.debug("Checking account blocked.transfer authorization removed");
        return new GeneralSuccessfullResponse(Messages.CHECKING_ACCOUNT_BLOCKED_SUCCESSFULLY);
    }

    @Override
    public GeneralResponse getCustomersUnblockedCheckingAccounts(long customerID) throws CustomerNotFoundException {
        log.info(" getCustomersUnblockedCheckingAccounts : started.");
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            log.error("Customer not found.");
            throw new CustomerNotFoundException();
        }
        List<CheckingAccount> checkingAccounts = checkingAccountService.getUnBlockedAccounts(customerID);
        List<CheckingAccountResponse> accountResponses = checkingAccounts.stream().map(checkingAccountMapper::toAccountResponses).toList();
        log.info(" getCustomersUnblockedCheckingAccounts : ended.");
        return new GeneralDataResponse<>(accountResponses);
    }

    @Override
    public GeneralResponse getCheckingAccountActivities(long customerID, long accountID, ActivityFilter filter) throws CustomerNotFoundException, ActivitiesNotFoundException, CheckingAccountNotFoundException {
        log.debug("checking account activities getting is started.");
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            log.error("Customer not found.");

            throw new CustomerNotFoundException();
        }
        Optional<CheckingAccount> optionalCheckingAccount = checkingAccountService.findById(accountID);
        if (optionalCheckingAccount.isEmpty()) {
            log.error("Checking account not found.");
            throw new CheckingAccountNotFoundException(Messages.CHECKING_ACCOUNT_NOT_FOUND);
        }
        boolean checkAccountOwnersIsCustomer = checkAccountOwnersIsCustomer(optionalCustomer.get().getCheckingAccounts(), accountID);
        if (!checkAccountOwnersIsCustomer) {
            log.error("Checking account owners not found.");
            throw new CheckingAccountNotFoundException(Messages.CHECKING_ACCOUNT_NOT_FOUND);
        }
        if (filter == null) {
            Date today = new Date();
            Date aMonthAgo = Utils.get30DaysAgo(today); // get 30 day ago from today
            filter = new ActivityFilter(aMonthAgo, today); //  default filter a month ago
        }
        List<AccountActivity> accountActivities = optionalCheckingAccount.get().getActivityWithFilterDate(filter);
        if (accountActivities.isEmpty()) {
            log.error("Checking account activities not found.");
            throw new ActivitiesNotFoundException();
        }

        List<AccountActivityResponse> responseList = accountActivities.stream().map(accountActivityMapper::toDto).toList();
        log.info("Checking account activities successfully returned.");
        return new GeneralDataResponse<>(responseList);
    }


    @Override
    public GeneralResponse getCheckingAccountById(Long customerID, long accountID) throws CustomerNotFoundException, CheckingAccountNotFoundException {
        log.debug("get checking account by id  is started.");
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            log.error("Customer not found.");
            throw new CustomerNotFoundException();
        }
        Optional<CheckingAccount> optionalCheckingAccount = checkingAccountService.findById(accountID);
        if (optionalCheckingAccount.isEmpty()) {
            log.error("Checking account not found.");
            throw new CheckingAccountNotFoundException(Messages.CHECKING_ACCOUNT_NOT_FOUND);
        }
        CheckingAccountResponse checkingAccountResponse = checkingAccountMapper.toAccountResponses(optionalCheckingAccount.get());
        log.info("Checking account : " + optionalCheckingAccount.get().getId() + "  returned");
        return new GeneralDataResponse(checkingAccountResponse);

    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public GeneralResponse deleteById(long customerID, long accountID) throws CustomerNotFoundException, DeleteAccountHasBalanceException, CannotDeleteBlockedAccounException, SavingAccountNotFound, AccountBalanceGreatherThenZeroException, CheckingAccountNotFoundException {
        log.debug("checking account delete by id is  started.");
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            log.error("Customer not found.");
            throw new CustomerNotFoundException();
        }
        Optional<CheckingAccount> checkingAccount = checkingAccountService.findById(accountID);
        if (checkingAccount.isEmpty()) {
            log.error("Checking account not found.");
            throw new CheckingAccountNotFoundException(Messages.CHECKING_ACCOUNT_NOT_FOUND);
        }
        boolean checkAccountOwnersIsCustomer = checkAccountOwnersIsCustomer(optionalCustomer.get().getCheckingAccounts(), accountID);
        if (!checkAccountOwnersIsCustomer) {
            log.error("Checking account owners not found.");
            throw new CheckingAccountNotFoundException(Messages.CHECKING_ACCOUNT_NOT_FOUND);
        }
        if (checkingAccount.get().isBlocked()) {
            throw new CannotDeleteBlockedAccounException();
        }
        if (checkingAccount.get().getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new DeleteAccountHasBalanceException(Messages.ACCOUNT_HAS_BALANCE_DELETE_EXCEPTION);
        }
        // hesaba bağlı birikimli  varmı varsa sil
        Optional<SavingAccount> optionalSavingAccount = savingAccountService.getByParentId(checkingAccount.get().getId());
        if (optionalSavingAccount.isPresent() && optionalSavingAccount.get().getBalance().compareTo(BigDecimal.ZERO) == 0) {
            savingAccountService.deleteSavingAccount(optionalSavingAccount.get());
        }

        boolean existsDebitCard = debitCardService.existsDebitCardByCheckingAccountId(checkingAccount.get().getId());
        if (existsDebitCard) {
            debitCardService.deleteByCheckingAccountId(checkingAccount.get().getId());
        }
        checkingAccountService.deleteCustomerCheckingAccountById(checkingAccount.get().getId());
        return new GeneralSuccessfullResponse(Messages.CHECKING_ACCOUNT_DELETED_SUCCESSFULLY);
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
