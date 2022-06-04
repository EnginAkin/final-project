package org.norma.finalproject.card.integration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.norma.finalproject.account.entity.CheckingAccount;
import org.norma.finalproject.account.repository.CheckingAccountRepository;
import org.norma.finalproject.card.core.model.request.CreateDebitCardRequest;
import org.norma.finalproject.card.core.model.response.DebitCardResponse;
import org.norma.finalproject.card.entity.DebitCard;
import org.norma.finalproject.card.entity.enums.CardStatus;
import org.norma.finalproject.card.repository.DebitCardRepository;
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
import java.util.List;
import java.util.Set;
/*
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class DebitCardIT {

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
    private DebitCardRepository debitCardRepository;
    @Autowired
    private PasswordEncoder encoder;
    private Customer customer=new Customer();
    private String tokenValue;
    private CheckingAccount checkingAccount;

    @BeforeEach
    public  void setup() {
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
        checkingAccount=createCheckingAccount();
        customer.setCheckingAccounts(List.of(checkingAccount));
        customerRepository.save(customer);
        checkingAccount.setCustomer(customer);
        checkingAccountRepository.save(checkingAccount);

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
    public void accessCreateDebitCardUnauthorizedWithoutJwtToken() throws Exception {
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/cards/debits")
        );
        actions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void givenCreateDebitCardRequest_whenCreate_thenReturn201() throws Exception {
        // given

        CreateDebitCardRequest debitCardRequest=new CreateDebitCardRequest();
        debitCardRequest.setParentCheckingAccountId(checkingAccount.getId());
        debitCardRequest.setPassword("1111");
        // when
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/cards/debits")
                        .header("authorization", "Bearer "+tokenValue)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(debitCardRequest))
        );
        // then
        actions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful", CoreMatchers.is(true)));
    }
/*
    @Test
    public void givenCardId_whenDeleteDebitCardById_thenReturn200() throws Exception {
        DebitCard debitCard=new DebitCard();
        debitCard.setCheckingAccount(checkingAccount);
        debitCard.setCardNumber("11111");
        debitCard.setCvv("11111");
        debitCard.setStatus(CardStatus.ACTIVE);
        debitCard.setPassword("1111");
        debitCard.setExpiryDate(new Date());
        debitCardRepository.save(debitCard);

        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/cards/debits/"+debitCard.getId())
                        .header("authorization", "Bearer "+tokenValue)
        );
        actions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful", CoreMatchers.is(true)));
    }





    private CheckingAccount createCheckingAccount() {

        CheckingAccount checkingAccount = new CheckingAccount();
        checkingAccount.setAccountNo("1111");
        checkingAccount.setIbanNo("1111");
        checkingAccount.setBranchCode("00");
        checkingAccount.setBlocked(false);
        checkingAccount.setBankCode("000000");
        checkingAccount.setAccountName("account name");
        checkingAccount.setBranchName("FATIH");
        return checkingAccount;
    }
}
 */

