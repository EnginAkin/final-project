package org.norma.finalproject.account.integration;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.norma.finalproject.account.core.model.request.CreateCheckingAccountRequest;
import org.norma.finalproject.account.entity.CheckingAccount;
import org.norma.finalproject.account.entity.enums.CurrencyType;
import org.norma.finalproject.account.repository.CheckingAccountRepository;
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

import java.util.Date;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CheckingAccountIT {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CheckingAccountRepository checkingAccountRepository;
    @Autowired
    private CustomerRepository customerRepository;
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
    public void givenCreateCheckingAccountRequestWithoutJWT_thenReturnUnauthorized() throws Exception {
        CreateCheckingAccountRequest request = new CreateCheckingAccountRequest();
        request.setBranchName("FATIH");
        request.setBankCode("50");
        request.setBankCode("000601");
        request.setCurrencyType(CurrencyType.TRY);


        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/accounts/checking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );
        actions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void givenCreateCheckingAccountRequest_whenCreate_thenReturn201() throws Exception {
        // given
        CreateCheckingAccountRequest request = new CreateCheckingAccountRequest();
        request.setBranchName("FATIH");
        request.setBranchCode("51");
        request.setBankCode("000601");
        request.setCurrencyType(CurrencyType.TRY);
        // when
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/accounts/checking")
                        .header("authorization", "Bearer " + tokenValue)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );
        // then
        actions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());

    }

    @Test
    public void givenAccountIdWithoutJWT_thenReturnUnauthorized() throws Exception {

        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/accounts/checking/" + 1)
        );
        actions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void givenAccountId_whenGetById_thenReturn200() throws Exception {
        // given
        CheckingAccount checkingAccount = new CheckingAccount();
        checkingAccount.setBankCode("000000");
        checkingAccount.setBranchCode("000000");
        checkingAccount.setAccountName("account");
        checkingAccount.setIbanNo("000000");
        checkingAccount.setBranchName("branch");
        checkingAccount.setAccountNo("000000");
        checkingAccount.setBlocked(true);
        checkingAccount.setCustomer(customer);
        checkingAccountRepository.save(checkingAccount);
        // when
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/accounts/checking/" + checkingAccount.getId())
                        .header("authorization", "Bearer " + tokenValue)
        );
        // then
        actions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful", CoreMatchers.is(true)));

    }

}
