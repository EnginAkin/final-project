package org.norma.finalproject.account.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.account.core.exception.AccountBalanceGreatherThenZeroException;
import org.norma.finalproject.account.core.exception.CheckingAccountNotFoundException;
import org.norma.finalproject.account.core.exception.SavingAccountNotFound;
import org.norma.finalproject.account.core.exception.SavingAccountOperationException;
import org.norma.finalproject.account.core.mapper.AccountActivityMapper;
import org.norma.finalproject.account.core.mapper.SavingAccountMapper;
import org.norma.finalproject.account.core.model.request.CreateSavingAccountRequest;
import org.norma.finalproject.account.core.model.response.AccountActivityResponse;
import org.norma.finalproject.account.core.model.response.SavingAccountDto;
import org.norma.finalproject.account.core.utils.UniqueNoCreator;
import org.norma.finalproject.account.entity.CheckingAccount;
import org.norma.finalproject.account.entity.SavingAccount;
import org.norma.finalproject.account.entity.base.AccountActivity;
import org.norma.finalproject.account.service.AccountActivityService;
import org.norma.finalproject.account.service.CheckingAccountService;
import org.norma.finalproject.account.service.FacadeSavingAccountService;
import org.norma.finalproject.account.service.SavingAccountService;
import org.norma.finalproject.card.core.exception.DebitCardNotFoundException;
import org.norma.finalproject.card.core.model.request.ActivityFilter;
import org.norma.finalproject.common.core.result.GeneralDataResponse;
import org.norma.finalproject.common.core.result.GeneralResponse;
import org.norma.finalproject.common.core.result.GeneralSuccessfullResponse;
import org.norma.finalproject.common.core.utils.Message;
import org.norma.finalproject.common.core.utils.Utils;
import org.norma.finalproject.customer.core.exception.ActivitiesNotFoundException;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.service.CustomerService;
import org.norma.finalproject.exchange.core.exception.AmountNotValidException;
import org.norma.finalproject.transfer.core.exception.TransferOperationException;
import org.norma.finalproject.transfer.core.model.request.IbanTransferRequest;
import org.norma.finalproject.transfer.entity.enums.TransferType;
import org.norma.finalproject.transfer.service.base.TransferBase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FacadeSavingAccountServiceImpl implements FacadeSavingAccountService {

    private final SavingAccountService savingAccountService;
    private final CheckingAccountService checkingAccountService;
    private final CustomerService customerService;
    private final SavingAccountMapper savingAccountMapper;
    private final UniqueNoCreator uniqueNoCreator;
    private final TransferBase<IbanTransferRequest> ibanTransferBase;
    private final AccountActivityService accountActivityService;
    private final AccountActivityMapper accountActivityMapper;

    @Override
    @Transactional
    public GeneralResponse create(Long customerID, CreateSavingAccountRequest createSavingAccountRequest) throws CustomerNotFoundException, CheckingAccountNotFoundException, SavingAccountOperationException, AmountNotValidException, TransferOperationException, DebitCardNotFoundException {
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        Optional<CheckingAccount> parentCheckingAccount = checkingAccountService.findAccountByAccountNumber(createSavingAccountRequest.getParentAccountNumber());
        if (parentCheckingAccount.isEmpty()) {
            throw new CheckingAccountNotFoundException(Message.CHECKING_ACCOUNT_NOT_FOUND+"By "+createSavingAccountRequest.getParentAccountNumber());
        }
        if (parentCheckingAccount.get().getCurrencyType() != createSavingAccountRequest.getCurrencyType()) {
            throw new SavingAccountOperationException(Message.SAVING_ACCOUNT_OPERATION_NOT_MATCHED_CURRENCY_TYPE_EXCEPTION + createSavingAccountRequest.getCurrencyType());
        }
        boolean checkUsedParentAccountForSavingAccount = savingAccountService.isUsedParentAccountForSavingAccount(optionalCustomer.get().getId(), parentCheckingAccount.get().getId());
        if (checkUsedParentAccountForSavingAccount) {
            throw new SavingAccountOperationException(Message.SAVING_ACCOUNT_OPERATION_PARENT_USED_FOR_SAVING_ACCOUNT_EXCEPTION);
        }
        if (createSavingAccountRequest.getOpeningBalance().compareTo(parentCheckingAccount.get().getBalance()) > 0) {
            throw new SavingAccountOperationException(Message.SAVING_ACCOUNT_OPERATION_PARENT_BALANCE_NOT_ENOUGH_EXCEPTION);
        }
        SavingAccount savingAccount = savingAccountMapper.createSavingAccountToEntity(createSavingAccountRequest);
        savingAccount.setCustomer(optionalCustomer.get());
        savingAccount.setParentAccount(parentCheckingAccount.get());
        savingAccount.setAccountNo(uniqueNoCreator.creatAccountNo());
        savingAccount.setIbanNo(uniqueNoCreator.createIbanNo(savingAccount.getAccountNo(), savingAccount.getParentAccount().getBankCode()));

        SavingAccount savedAccount = savingAccountService.save(savingAccount);
        // Transfer to save account from checking account
        IbanTransferRequest transferRequest = new IbanTransferRequest(parentCheckingAccount.get().getIbanNo(), savingAccount.getIbanNo(),createSavingAccountRequest.getOpeningBalance(),"Opening balance", TransferType.OTHER);
        ibanTransferBase.transfer(customerID, transferRequest); // transfer with iban
        return new GeneralDataResponse<>(savingAccountMapper.toDto(savedAccount));
    }

    @Override
    public GeneralResponse getAccounts(Long customerID) throws CustomerNotFoundException {
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        List<SavingAccount> savingAccountList = savingAccountService.getAllAccountsByCustomerId(customerID);
        List<SavingAccountDto> response = savingAccountList.stream().map(savingAccountMapper::toDto).collect(Collectors.toList());
        return new GeneralDataResponse<>(response);
    }

    @Override
    public GeneralResponse getAccountActivities(Long customerID, long accountID, ActivityFilter filter) throws CustomerNotFoundException, ActivitiesNotFoundException, SavingAccountOperationException, SavingAccountNotFound {
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        Optional<SavingAccount> optionalSavingAccount=savingAccountService.findById(accountID);
        if(optionalSavingAccount.isEmpty()){
            throw new SavingAccountNotFound();
        }

        boolean checkOwnersAccountIsCustomer = checkOwnersAccountIsCustomer(optionalCustomer.get(),accountID);
        if(!checkOwnersAccountIsCustomer){
            throw new SavingAccountOperationException(Message.SAVING_ACCOUNT_OPERATION_ACTIVITIES_NOT_FOUND_EXCEPTION);
        }
        if(filter==null){
            Date today = new Date();
            Date aMonthAgo= Utils.get30DaysAgo(today); // get 30 day ago from today
            filter=new ActivityFilter(aMonthAgo,today); //  default filter a month ago
        }
        List<AccountActivity> accountActivities=optionalSavingAccount.get().getActivityWithFilterDate(filter);
        if (accountActivities.isEmpty()) {
            throw new ActivitiesNotFoundException();
        }
        List<AccountActivityResponse> responseList = accountActivities.stream().map(accountActivityMapper::toDto).toList();
        return new GeneralDataResponse<>(responseList);
    }

    @Override
    public void deleteSavingAccountByCheckingId(Long checkingID) throws SavingAccountNotFound, AccountBalanceGreatherThenZeroException {
        Optional<SavingAccount> optionalSavingAccount = savingAccountService.getByParentId(checkingID);
        if(optionalSavingAccount.isEmpty()){
            throw new SavingAccountNotFound();
        }
        if(optionalSavingAccount.get().getBalance().compareTo(BigDecimal.ZERO)>0){
            throw new AccountBalanceGreatherThenZeroException(Message.ACCOUNT_HAS_BALANCE_DELETE_EXCEPTION);
        }
        savingAccountService.deleteByParent(optionalSavingAccount.get());
    }

    @Override
    public GeneralResponse deleteById(Long customerID, long accountID) throws SavingAccountNotFound, AccountBalanceGreatherThenZeroException {
        Optional<SavingAccount> optionalSavingAccount = savingAccountService.findById(accountID);
        if(optionalSavingAccount.isEmpty()){
            throw new SavingAccountNotFound();
        }
        if(optionalSavingAccount.get().getBalance().compareTo(BigDecimal.ZERO)>0){
            throw new AccountBalanceGreatherThenZeroException(Message.ACCOUNT_HAS_BALANCE_DELETE_EXCEPTION);
        }
        savingAccountService.deleteByParent(optionalSavingAccount.get());
        return new GeneralSuccessfullResponse("Successfully deleted");
    }

    @Override
    public GeneralResponse getAccountByAccountID(Long customerID, long accountID) throws CustomerNotFoundException, SavingAccountNotFound {
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }

        Optional<SavingAccount> optionalSavingAccount = savingAccountService.findById(accountID);
        if(optionalSavingAccount.isEmpty()){
            throw new SavingAccountNotFound();
        }
        SavingAccountDto dto = savingAccountMapper.toDto(optionalSavingAccount.get());
        return new GeneralDataResponse<>(dto);

    }

    private boolean checkOwnersAccountIsCustomer(Customer customer, long accountId) {
        return customer.getSavingAccounts().stream().anyMatch(savingAccount -> savingAccount.getId().equals(accountId));
    }

}
