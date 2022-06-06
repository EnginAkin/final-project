package org.norma.finalproject.customer.integration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.norma.finalproject.common.security.token.entity.JWTToken;
import org.norma.finalproject.common.security.token.repository.TokenRepository;
import org.norma.finalproject.customer.core.model.request.LoginFormRequest;
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
public class AuthenticationIT {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private PasswordEncoder encoder;
    private String tokenValue;

    @BeforeEach
    public void setup() {
        customerRepository.deleteAll();
        roleRepository.deleteAll();
        tokenRepository.deleteAll();
        Customer customer = new Customer();
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
    public void givenLoginFormRequest_whenLogin_thenReturn200() throws Exception {
        // given
        LoginFormRequest loginFormRequest = new LoginFormRequest();
        loginFormRequest.setUserNumber("11111111111");
        loginFormRequest.setPassword("000000");
        // when
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/authentication/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginFormRequest))
        );
        // then
        actions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful", CoreMatchers.is(true)));

    }

    @Test
    public void givenLoginFormRequestWithInvalidPassword_whenLogin_thenReturn401() throws Exception {
        // given
        LoginFormRequest loginFormRequest = new LoginFormRequest();
        loginFormRequest.setUserNumber("11111111111");
        loginFormRequest.setPassword("111111");// wrong password
        // when
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/authentication/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginFormRequest))
        );
        //then
        actions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful", CoreMatchers.is(false)));

    }

    @Test
    public void givenLoginFormRequestWithInvalidIdentity_whenLogin_thenReturn401() throws Exception {
        // given
        LoginFormRequest loginFormRequest = new LoginFormRequest();
        loginFormRequest.setUserNumber("00000000000");// wrong identity
        loginFormRequest.setPassword("000000");
        // when
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/authentication/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginFormRequest))
        );
        //then
        actions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful", CoreMatchers.is(false)));

    }

    @Test
    public void whenLogout_thenReturn200() throws Exception {

        // when
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/authentication/logout")
                        .header("authorization", "Bearer " + tokenValue)
        );
        //then
        actions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful", CoreMatchers.is(true)));

    }
}
