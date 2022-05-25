package org.norma.finalproject.account.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.account.core.exception.CheckingAccountNotFoundException;
import org.norma.finalproject.account.core.exception.SavingAccountNotFound;
import org.norma.finalproject.account.core.exception.SavingAccountOperationException;
import org.norma.finalproject.account.core.mapper.AccountActivityMapper;
import org.norma.finalproject.account.core.mapper.SavingAccountMapper;
import org.norma.finalproject.account.core.model.request.CreateSavingAccountRequest;
import org.norma.finalproject.account.core.model.response.AccountActivityResponse;
import org.norma.finalproject.account.core.model.response.CreateSavingAccountResponse;
import org.norma.finalproject.account.core.utils.UniqueNoCreator;
import org.norma.finalproject.account.entity.CheckingAccount;
import org.norma.finalproject.account.entity.SavingAccount;
import org.norma.finalproject.account.entity.base.AccountActivity;
import org.norma.finalproject.account.service.AccountActivityService;
import org.norma.finalproject.account.service.CheckingAccountService;
import org.norma.finalproject.account.service.FacadeSavingAccountService;
import org.norma.finalproject.account.service.SavingAccountService;
import org.norma.finalproject.common.response.GeneralDataResponse;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.customer.core.exception.ActivitiesNotFoundException;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.service.CustomerService;
import org.norma.finalproject.exchange.core.exception.AmountNotValidException;
import org.norma.finalproject.transfer.core.exception.TransferOperationException;
import org.norma.finalproject.transfer.core.model.request.CreateIbanTransferRequest;
import org.norma.finalproject.transfer.entity.enums.SendType;
import org.norma.finalproject.transfer.service.base.TransferBase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final TransferBase<CreateIbanTransferRequest> ibanTransferBase;
    private final AccountActivityService accountActivityService;
    private final AccountActivityMapper accountActivityMapper;

    @Override
    @Transactional
    public GeneralResponse create(Long customerID, CreateSavingAccountRequest createSavingAccountRequest) throws CustomerNotFoundException, CheckingAccountNotFoundException, SavingAccountOperationException, AmountNotValidException, TransferOperationException {
        Optional<Customer> optionalCustomer = customerService.findCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        Optional<CheckingAccount> parentCheckingAccount = checkingAccountService.getAccountByAccountNumber(createSavingAccountRequest.getParentAccountNumber());
        if (parentCheckingAccount.isEmpty()) {
            throw new CheckingAccountNotFoundException("Checking account not fount by : " + createSavingAccountRequest.getParentAccountNumber());
        }
        if (parentCheckingAccount.get().getCurrencyType() != createSavingAccountRequest.getCurrencyType()) {
            throw new SavingAccountOperationException("Parent currency type not matched for saving account,Please create checking Account currency type :" + createSavingAccountRequest.getCurrencyType());
        }
        boolean checkUsedParentAccountForSavingAccount = savingAccountService.isUsedParentAccountForSavingAccount(optionalCustomer.get().getId(), parentCheckingAccount.get().getId());
        if (checkUsedParentAccountForSavingAccount) {
            throw new SavingAccountOperationException("Parent Used for saving account , change parent checking account.");
        }
        if (createSavingAccountRequest.getOpeningBalance().compareTo(parentCheckingAccount.get().getBalance()) > 0) {
            throw new SavingAccountOperationException("Parent account balance not enough for saving balance.");
        }
        SavingAccount savingAccount = savingAccountMapper.createSavingAccountToEntity(createSavingAccountRequest);
        savingAccount.setCustomer(optionalCustomer.get());
        savingAccount.setParentAccount(parentCheckingAccount.get());
        savingAccount.setAccountNo(uniqueNoCreator.creatAccountNo());
        savingAccount.setIbanNo(uniqueNoCreator.createIbanNo(savingAccount.getAccountNo(), savingAccount.getParentAccount().getBankCode()));
        SavingAccount savedAccount = savingAccountService.save(savingAccount);
        // Transfer to save acccount from checking account
        CreateIbanTransferRequest transferRequest = new CreateIbanTransferRequest(parentCheckingAccount.get().getIbanNo(), savingAccount.getIbanNo(),createSavingAccountRequest.getOpeningBalance(),"Opening balance",SendType.OTHER);
        ibanTransferBase.transfer(customerID, transferRequest);
        return new GeneralDataResponse<>(savingAccountMapper.toCreateSavingAccountDto(savedAccount));
    }

    @Override
    public GeneralResponse getAccounts(Long customerID) throws CustomerNotFoundException {
        Optional<Customer> optionalCustomer = customerService.findCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        List<SavingAccount> savingAccountList = savingAccountService.getAllAccountsByCustomerId(customerID);
        List<CreateSavingAccountResponse> response = savingAccountList.stream().map(savingAccountMapper::toCreateSavingAccountDto).collect(Collectors.toList());
        return new GeneralDataResponse<>(response);
    }

    @Override
    public GeneralResponse getAccountActivities(Long customerID, long accountID) throws CustomerNotFoundException, CheckingAccountNotFoundException, ActivitiesNotFoundException, SavingAccountOperationException {
        Optional<Customer> optionalCustomer = customerService.findCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        boolean checkOwnersAccountIsCustomer = checkOwnersAccountIsCustomer(optionalCustomer.get(),accountID);
        if(!checkOwnersAccountIsCustomer){
            throw new SavingAccountOperationException("Saving account activities not found.");
        }
        List<AccountActivity> accountActivities = accountActivityService.getAccountActivitiesByAccountId(accountID);
        if (accountActivities.isEmpty()) {
            throw new ActivitiesNotFoundException();
        }
        List<AccountActivityResponse> responseList = accountActivities.stream().map(accountActivityMapper::toDto).toList();
        return new GeneralDataResponse<>(responseList);

    }

    @Override
    public void deleteByCheckingParentId(Long checkingId) throws SavingAccountNotFound {
        Optional<SavingAccount> optionalSavingAccount = savingAccountService.getByParentId(checkingId);
        if(optionalSavingAccount.isEmpty()){
            throw new SavingAccountNotFound();
        }
    }

    private boolean checkOwnersAccountIsCustomer(Customer customer, long accountId) {
        return customer.getSavingAccounts().stream().anyMatch(savingAccount -> savingAccount.getId().equals(accountId));
    }

    // 1. vadesiz. hesap 80 tl TR3300006103170781464664297 eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMTExMTExMTExMSIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJleHAiOjE2NTM0OTEyNDcsImlhdCI6MTY1MzQ2MTI0N30.O3NzpW6BIrCwnhnJgJLPbRAuFKASQek5uhJx6qvKSNH5WuznVPGDppm_V1RO5f0_awayfh5azyQTFY710U82FA
    //1. hesap vadeli birikimli hesap 20 tl TR3300006108707433911638621
    // 2. hesap 100 tl TR3300006104557638514004892  eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMTExMTExMTExMiIsInJvbGVzIjpbIlJPTEVfVVNFUiJdLCJleHAiOjE2NTM0OTMyMDksImlhdCI6MTY1MzQ2MzIwOX0.6Jn3XR3k8Rt6br_0cQi111oJR5WAvOcs-5kmlZyaDi9j_LMlrPNgCCRShEwAC3u_cgUeO2jJAaeOVMBb1oCggA

}
