package org.norma.finalproject.account.integration;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.norma.finalproject.account.core.model.request.CreateSavingAccountRequest;
import org.norma.finalproject.account.entity.CheckingAccount;
import org.norma.finalproject.account.entity.SavingAccount;
import org.norma.finalproject.account.entity.enums.AccountType;
import org.norma.finalproject.account.entity.enums.CurrencyType;
import org.norma.finalproject.account.entity.enums.Maturity;
import org.norma.finalproject.account.entity.enums.PurposeSaving;
import org.norma.finalproject.account.repository.CheckingAccountRepository;
import org.norma.finalproject.account.repository.SavingAccountRepository;
import org.norma.finalproject.common.security.token.entity.JWTToken;
import org.norma.finalproject.common.security.token.repository.TokenRepository;
import org.norma.finalproject.customer.core.utilities.CustomerConstant;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.entity.Role;
import org.norma.finalproject.customer.repository.CustomerRepository;
import org.norma.finalproject.customer.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SavingAccountIT {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CheckingAccountRepository checkingAccountRepository;
    @Autowired
    private SavingAccountRepository savingAccountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private PasswordEncoder encoder;
    private Customer customer = new Customer();
    private String tokenValue;

    @BeforeEach
    public void setup() {
        customerRepository.deleteAll();
        roleRepository.deleteAll();
        tokenRepository.deleteAll();
        checkingAccountRepository.deleteAll();

        customer.setIdentityNumber("11111111111");
        customer.setUserNumber("11111111111");
        customer.setTelephone("0000000");
        customer.setPassword(encoder.encode("000000"));
        customer.setName("TEST-FOR-SECURITY");
        customer.setSurname("TEST-FOR-SECURITY");
        customer.setEmail("TEST-FOR-SECURITY");
        Role role = new Role(CustomerConstant.ROLE_USER);
        roleRepository.save(role);
        customer.setRoles(Set.of(role));
        customerRepository.save(customer);

        tokenValue = JWT.create()
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 300000))
                .withSubject(customer.getIdentityNumber())
                .withClaim("roles", CustomerConstant.ROLE_USER)
                .sign(Algorithm.HMAC512("normasecretkey"));
        JWTToken token = new JWTToken();
        token.setToken(tokenValue);
        tokenRepository.save(token);
    }

    @Test
    public void createWithoutJWT_thenReturnUnauthorized() throws Exception {

        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/accounts/saving")
        );
        actions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void givenCreateSavingAccountRequest_whenCreate_thenReturun200() throws Exception {
        // given
        CheckingAccount checkingAccount = new CheckingAccount();
        checkingAccount.setBankCode("000000");
        checkingAccount.setBranchCode("000000");
        checkingAccount.setAccountName("account");
        checkingAccount.setIbanNo("000000");
        checkingAccount.setBranchName("branch");
        checkingAccount.setAccountType(AccountType.CHECKING);
        checkingAccount.setAccountNo("000000");
        checkingAccount.setCurrencyType(CurrencyType.TRY);
        checkingAccount.setBlocked(true);
        checkingAccount.setCustomer(customer);
        checkingAccountRepository.save(checkingAccount);
        CreateSavingAccountRequest request = new CreateSavingAccountRequest();
        request.setCurrencyType(CurrencyType.TRY);
        request.setAccountName("saving account");
        request.setPurposeSaving(PurposeSaving.EDUCATION);
        request.setMaturity(Maturity.NINETY_DAY);
        request.setOpeningBalance(BigDecimal.ZERO);
        request.setTargetAmount(BigDecimal.TEN);
        request.setParentAccountNumber(checkingAccount.getAccountNo());
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/accounts/saving")
                        .header("authorization", "Bearer " + tokenValue)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );
        actions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void getSavingAccountByIDWithoutJWT_thenReturnUnauthorized() throws Exception {

        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/accounts/saving/" + 1)
        );
        actions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void givenAccountId_whenGetSavingAccountByID_thenReturn200() throws Exception {
        // given
        CheckingAccount checkingAccount = new CheckingAccount();
        checkingAccount.setBankCode("000000");
        checkingAccount.setBranchCode("000000");
        checkingAccount.setAccountName("account");
        checkingAccount.setIbanNo("000000");
        checkingAccount.setBranchName("branch");
        checkingAccount.setAccountType(AccountType.CHECKING);
        checkingAccount.setAccountNo("000000");
        checkingAccount.setCurrencyType(CurrencyType.TRY);
        checkingAccount.setBlocked(true);
        checkingAccount.setCustomer(customer);
        checkingAccountRepository.save(checkingAccount);
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setParentAccount(checkingAccount);
        savingAccount.setIbanNo("11111111");
        savingAccount.setAccountNo("1111");
        savingAccount.setAccountName("account name");
        savingAccountRepository.save(savingAccount);
        // when
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/accounts/saving/" + savingAccount.getId())
                        .header("authorization", "Bearer " + tokenValue)

        );
        // then
        actions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        savingAccountRepository.delete(savingAccount);

    }
}
