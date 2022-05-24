package org.norma.finalproject.account.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.account.core.exception.CheckingAccountNotFoundException;
import org.norma.finalproject.account.core.exception.SavingAccountOperationException;
import org.norma.finalproject.account.core.mapper.SavingAccountMapper;
import org.norma.finalproject.account.core.model.request.CreateSavingAccountRequest;
import org.norma.finalproject.account.core.model.response.CreateSavingAccountResponse;
import org.norma.finalproject.account.core.utils.UniqueNoCreator;
import org.norma.finalproject.account.entity.CheckingAccount;
import org.norma.finalproject.account.entity.SavingAccount;
import org.norma.finalproject.account.service.CheckingAccountService;
import org.norma.finalproject.account.service.FacadeSavingAccountService;
import org.norma.finalproject.account.service.SavingAccountService;
import org.norma.finalproject.common.response.GeneralDataResponse;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.customer.core.exception.CustomerNotFoundException;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.service.CustomerService;
import org.norma.finalproject.exchange.core.exception.AmountNotValidException;
import org.norma.finalproject.transfer.core.exception.TransferOperationException;
import org.norma.finalproject.transfer.core.model.request.CreateIbanTransferRequest;
import org.norma.finalproject.transfer.entity.enums.SendType;
import org.norma.finalproject.transfer.service.impl.IbanTransferBase;
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

    private final IbanTransferBase ibanTransferBase;

    @Override
    @Transactional
    public GeneralResponse create(Long customerID, CreateSavingAccountRequest createSavingAccountRequest) throws CustomerNotFoundException, CheckingAccountNotFoundException, SavingAccountOperationException, AmountNotValidException, TransferOperationException {
        Optional<Customer> optionalCustomer = customerService.getCustomerById(customerID);
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
        boolean usedParentAccountForSavingAccount = savingAccountService.isUsedParentAccountForSavingAccount(optionalCustomer.get().getId(), parentCheckingAccount.get().getId());
        if (usedParentAccountForSavingAccount) {
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
        CreateIbanTransferRequest transferRequest = new CreateIbanTransferRequest();
        transferRequest.setAmount(createSavingAccountRequest.getOpeningBalance());
        transferRequest.setDescription("Opening balance");
        transferRequest.setFromIban(parentCheckingAccount.get().getIbanNo());
        transferRequest.setSendType(SendType.OTHER);
        transferRequest.setToIban(savingAccount.getIbanNo());
        ibanTransferBase.transfer(customerID, transferRequest);
        return new GeneralDataResponse<>(savingAccountMapper.toCreateSavingAccountDto(savedAccount));
    }

    @Override
    public GeneralResponse getAccounts(Long customerID) throws CustomerNotFoundException {
        Optional<Customer> optionalCustomer = customerService.getCustomerById(customerID);
        if (optionalCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        List<SavingAccount> savingAccountList = savingAccountService.getAllAccountsByCustomerId(customerID);
        List<CreateSavingAccountResponse> response = savingAccountList.stream().map(savingAccountMapper::toCreateSavingAccountDto).collect(Collectors.toList());
        return new GeneralDataResponse<>(response);
    }
}
