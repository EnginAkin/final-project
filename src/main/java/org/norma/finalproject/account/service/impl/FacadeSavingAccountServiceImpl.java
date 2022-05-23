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
import org.springframework.stereotype.Service;

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

    @Override
    public GeneralResponse create(Long customerId, CreateSavingAccountRequest createSavingAccountRequest) throws CustomerNotFoundException, CheckingAccountNotFoundException, SavingAccountOperationException {
        Optional<Customer> optionalCustomer = customerService.getCustomerById(customerId);
        if(optionalCustomer.isEmpty()){
            throw new CustomerNotFoundException();
        }
        Optional<CheckingAccount> parentCheckingAccount = checkingAccountService.getAccountByAccountNumber(createSavingAccountRequest.getParentAccountNumber());
        if(parentCheckingAccount.isEmpty()){
            throw new CheckingAccountNotFoundException("Checking account not fount by : "+createSavingAccountRequest.getParentAccountNumber());
        }
        if(parentCheckingAccount.get().getCurrencyType()!=createSavingAccountRequest.getCurrencyType()){
            throw new SavingAccountOperationException("Parent currency type not matched for saving account,Please create checking Account currency type :"+createSavingAccountRequest.getCurrencyType());
        }
        boolean usedParentAccountForSavingAccount = savingAccountService.isUsedParentAccountForSavingAccount(optionalCustomer.get().getId(), parentCheckingAccount.get().getId());
        if(usedParentAccountForSavingAccount){
            throw new SavingAccountOperationException("Parent Used for saving account , change parent checking account.");
        }
        if(createSavingAccountRequest.getOpeningBalance().compareTo(parentCheckingAccount.get().getBalance())>0){
            throw new SavingAccountOperationException("Parent account balance not enough for saving balance.");
        }
        SavingAccount account=savingAccountMapper.createSavingAccountToEntity(createSavingAccountRequest);
        account.setCustomer(optionalCustomer.get());
        account.setParentAccount(parentCheckingAccount.get());
        account.setAccountNo(uniqueNoCreator.creatAccountNo());
        account.setIbanNo(uniqueNoCreator.createIbanNo(account.getAccountNo(),account.getParentAccount().getBankCode()));
        // TODO transfer olması gerekiyor mevduatsız hesaptan birikim hesabına.

        SavingAccount savedAccount = savingAccountService.save(account);
        return new GeneralDataResponse<>(savingAccountMapper.toCreateSavingAccountDto(savedAccount));
    }

    @Override
    public GeneralResponse getAccounts(Long customerId) throws CustomerNotFoundException {
        Optional<Customer> optionalCustomer = customerService.getCustomerById(customerId);
        if(optionalCustomer.isEmpty()){
            throw new CustomerNotFoundException();
        }
        List<SavingAccount> savingAccountList=savingAccountService.getAllAccountsByCustomerId(customerId);
        List<CreateSavingAccountResponse> response=savingAccountList.stream().map(savingAccountMapper::toCreateSavingAccountDto).collect(Collectors.toList());
        return new GeneralDataResponse<>(response);
    }
}
