package org.norma.finalproject.account.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.account.core.exception.*;
import org.norma.finalproject.account.core.mapper.CheckingAccountMapper;
import org.norma.finalproject.account.core.model.request.CreateDepositAcoountRequest;
import org.norma.finalproject.account.core.utils.UniqueNoCreator;
import org.norma.finalproject.account.entity.CheckingAccount;
import org.norma.finalproject.account.service.CheckingAccountService;
import org.norma.finalproject.account.service.FacadeCheckinAccountService;
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
public class FacadeCheckinAccountServiceImpl implements FacadeCheckinAccountService {

    private final CustomerService customerService;
    private final CheckingAccountService checkingAccountService;

    private final UniqueNoCreator uniqueNoCreator;
    private final CheckingAccountMapper mapper;

    @Override
    public GeneralResponse create(Customer customer, CreateDepositAcoountRequest createDepositAcoountRequest) throws CustomerNotFoundException, AccountNameAlreadyHaveException {
        CheckingAccount checkingAccount = mapper.ToEntity(createDepositAcoountRequest);

        Optional<CheckingAccount> optionalDepositAccount = existsCustomerCheckingtAccountByAccountName(customer.getCheckingAccounts(), createDepositAcoountRequest.getAccountName());
        if (optionalDepositAccount.isPresent()) {
            throw new AccountNameAlreadyHaveException(createDepositAcoountRequest.getAccountName() + " name already have account in your accounts.");
        }

        String AccountNo = uniqueNoCreator.createDepositAccountNo();
        checkingAccount.setAccountNo(AccountNo);
        String IbanNo = uniqueNoCreator.createDepositIbanNo();
        checkingAccount.setIbanNo(IbanNo);
        checkingAccount.setBalance(BigDecimal.ZERO);
        checkingAccount.setCreatedAt(new Date());
        checkingAccount.setCreatedBy("ENGIN AKIN");
        checkingAccount.setCustomer(customer);
        CheckingAccount savedCheckingAccount = checkingAccountService.save(checkingAccount);
        return new GeneralDataResponse<>(mapper.toDto(savedCheckingAccount));
    }

    @Override
    public GeneralResponse blockAccount(long accountId) throws DepositAccountNotFoundException {
        Optional<CheckingAccount> depositAccount = checkingAccountService.findById(accountId);
        if (depositAccount.isEmpty()) {
            throw new DepositAccountNotFoundException("Checking Account  Not Found.");
        }
        depositAccount.get().setBlocked(true);
        checkingAccountService.save(depositAccount.get());
        log.info("Customer blocked.transfer authorization removed");
        return new GeneralSuccessfullResponse("Customer Blocked successfull. Customer cannot transfer anymore.");
    }

    // TODO by name iptal
    @Override
    public GeneralResponse delete(Customer customer, String accountName) throws CustomerAccountNotFoundException, CustomerNotFoundException, DeleteAccountHasBalanceException, CannotDeleteBlockedAccounException {
        if (customer == null) {
            throw new CustomerNotFoundException();
        }
        Optional<CheckingAccount> depositAccount = existsCustomerCheckingtAccountByAccountName(customer.getCheckingAccounts(), accountName);
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
        checkingAccountService.deleteCustomerCheckingAccount(depositAccount.get());
        return new GeneralSuccessfullResponse("Deleted successfully Customer Deposit account.");
    }

    public Optional<CheckingAccount> existsCustomerCheckingtAccountByAccountName(List<CheckingAccount> checkingAccountList, String accountName) {
        return checkingAccountList.stream().filter(depositAccount -> depositAccount.getAccountName().equals(accountName)).findFirst();
    }


}
