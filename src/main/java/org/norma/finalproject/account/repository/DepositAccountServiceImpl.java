package org.norma.finalproject.account.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.norma.finalproject.account.entity.DepositAccount;
import org.norma.finalproject.account.repository.DepositAccountRepository;
import org.norma.finalproject.account.service.DepositAccountService;
import org.norma.finalproject.customer.entity.Customer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepositAccountServiceImpl implements DepositAccountService {

    private final DepositAccountRepository depositAccountRepository;

    @Override
    public boolean checkIsAccountNoUnique(String accountNo) {
        return !depositAccountRepository.existsDepositAccountByAccountNo(accountNo);
    }

    @Override
    public DepositAccount save(DepositAccount depositAccount) {
        return depositAccountRepository.save(depositAccount);
    }

    @Override
    public boolean existsCustomerDepositAccountByAccountName(Customer customer, String accountName) {
        if (accountName.isEmpty()) {
            throw new IllegalArgumentException("account name cannot be null");
        }
        return depositAccountRepository.existsDepositAccountByAccountNameAndCustomer(accountName, customer);
    }

    @Override
    public boolean checkCustomerHasMoneyInDepositAccounts(long id) {
        return depositAccountRepository.existsDepositAccountsBalanceGreatherThanZeroByCustomerId(id);
    }


}


