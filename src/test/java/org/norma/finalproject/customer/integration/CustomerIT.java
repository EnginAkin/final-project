package org.norma.finalproject.customer.integration;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.norma.finalproject.account.core.model.request.CreateCheckingAccountRequest;
import org.norma.finalproject.account.entity.enums.CurrencyType;
import org.norma.finalproject.common.security.token.entity.JWTToken;
import org.norma.finalproject.common.security.token.repository.TokenRepository;
import org.norma.finalproject.customer.core.model.AddressDto;
import org.norma.finalproject.customer.core.model.CustomerInfoDto;
import org.norma.finalproject.customer.core.model.request.CreateCustomerRequest;
import org.norma.finalproject.customer.core.model.request.UpdateCustomerRequest;
import org.norma.finalproject.customer.core.utilities.CustomerConstant;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.entity.Role;
import org.norma.finalproject.customer.entity.enums.AddressType;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CustomerIT {
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
    public void accessUpdateUnauthorizedWithoutJwtToken() throws Exception {
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/customers/get-all"));
        actions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void givenCustomerJwtToken_whenDelete_thenReturn200Ok() throws Exception {
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/customers/delete")
                        .header("authorization", "Bearer " + tokenValue));
        actions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void givenCustomerWithJwtToken_whenUpdate_thenReturnGeneralSuccessfullResponse() throws Exception {
        //given
        UpdateCustomerRequest updateCustomerRequest = new UpdateCustomerRequest();
        updateCustomerRequest.setTelephone("1111");
        updateCustomerRequest.setPassword("111111");
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/customers/update")
                        .header("authorization", "Bearer " + tokenValue)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCustomerRequest))
        );
        actions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful", CoreMatchers.is(true)));
    }


    @Test
    public void giveninvalidUpdatedCustomer_whenUpdate_thenReturnGeneralBadRequest() throws Exception {
        //given
        UpdateCustomerRequest updateCustomerRequest = new UpdateCustomerRequest();
        updateCustomerRequest.setTelephone("1111");
        updateCustomerRequest.setPassword("000000"); // same password throw error
        // when
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/v1/customers/update")
                        .header("authorization", "Bearer " + tokenValue)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCustomerRequest))
        );
        // then
        actions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful", CoreMatchers.is(false)));

    }

    @Test
    public void givenCreateCustomerRequest_whenCreate_thenReturn201() throws Exception {
        //given
        CreateCustomerRequest createCustomerRequest = createCustomerRequest();
        // when
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/customers/sing-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCustomerRequest))
        );
        //then
        actions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful", CoreMatchers.is(true)));

    }

    @Test
    public void givenCreateCustomerRequestInvalidBirthday_whenCreate_thenReturn400() throws Exception {
        //given
        CreateCustomerRequest createCustomerRequest = createCustomerRequest();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, 2, 2);
        createCustomerRequest.getCustomerInfo().setBirthDay(calendar.getTime());
        // when
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/customers/sing-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCustomerRequest))
        );
        //then
        actions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful", CoreMatchers.is(false)));

    }

    private CreateCustomerRequest createCustomerRequest() {
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest();
        CustomerInfoDto customerInfoDto = new CustomerInfoDto();
        customerInfoDto.setSurname("norma");
        customerInfoDto.setName("norma");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, 03, 03);
        customerInfoDto.setBirthDay(calendar.getTime());
        customerInfoDto.setEmail("norma@gmail.com");
        customerInfoDto.setIdentityNumber("11111111122");
        customerInfoDto.setTelephone("1111111111");
        customerInfoDto.setPassword("111111");
        customerInfoDto.setIncome(BigDecimal.TEN);

        AddressDto addressDto = new AddressDto();
        addressDto.setStreetNumber("norma");
        addressDto.setDistrict("norma");
        addressDto.setState("norma");
        addressDto.setAddressType(AddressType.HOME);
        addressDto.setCountry("norma");
        addressDto.setCity("norma");


        CreateCheckingAccountRequest createCheckingAccountRequest = new CreateCheckingAccountRequest();
        createCheckingAccountRequest.setCurrencyType(CurrencyType.TRY);
        createCheckingAccountRequest.setBranchCode("11");
        createCheckingAccountRequest.setBankCode("000000");
        createCheckingAccountRequest.setBranchName("FATIH");
        createCustomerRequest.setCustomerInfo(customerInfoDto);
        createCustomerRequest.setAddress(addressDto);
        createCustomerRequest.setCheckingAccount(createCheckingAccountRequest);

        return createCustomerRequest;
    }


}
