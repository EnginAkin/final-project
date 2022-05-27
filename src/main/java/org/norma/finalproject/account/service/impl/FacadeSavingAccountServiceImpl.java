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
import org.norma.finalproject.common.response.GeneralDataResponse;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.common.response.GeneralSuccessfullResponse;
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
        IbanTransferRequest transferRequest = new IbanTransferRequest(parentCheckingAccount.get().getIbanNo(), savingAccount.getIbanNo(),createSavingAccountRequest.getOpeningBalance(),"Opening balance", TransferType.OTHER);
        ibanTransferBase.transfer(customerID, transferRequest);
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
    public GeneralResponse getAccountActivities(Long customerID, long accountID) throws CustomerNotFoundException, CheckingAccountNotFoundException, ActivitiesNotFoundException, SavingAccountOperationException {
        Optional<Customer> optionalCustomer = customerService.findByCustomerById(customerID);
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
    public void deleteSavingAccountByCheckingId(Long checkingID) throws SavingAccountNotFound, AccountBalanceGreatherThenZeroException {
        Optional<SavingAccount> optionalSavingAccount = savingAccountService.getByParentId(checkingID);
        if(optionalSavingAccount.isEmpty()){
            throw new SavingAccountNotFound();
        }
        if(optionalSavingAccount.get().getBalance().compareTo(BigDecimal.ZERO)>0){
            throw new AccountBalanceGreatherThenZeroException("Saving account balance greather than zero");
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
            throw new AccountBalanceGreatherThenZeroException("Saving account balance greather than zero");
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
