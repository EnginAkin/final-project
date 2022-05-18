package org.norma.finalproject.customer.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.norma.finalproject.common.security.user.CustomUserDetailsService;
import org.norma.finalproject.common.security.user.UserDetail;
import org.norma.finalproject.customer.core.model.request.CreateCustomerRequest;
import org.norma.finalproject.customer.core.utilities.CustomerConstant;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CustomerControllerIT {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @MockBean
    private CustomUserDetailsService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;



    @BeforeEach
    public void init() {
        customerRepository.deleteAll();
        Customer customer=new Customer();
        customer.setPassword("123");
        customer.setIdentityNumber("123");

        UserDetail userDetail=new UserDetail(customer);
        Mockito.when(userService.loadUserByUsername(Mockito.anyString())).thenReturn(userDetail);
        Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(customer.getIdentityNumber(),customer.getPassword()));

    }


    @Test
    void givenCreateCustomerRequest_whenCreate_thenReturnsSuccessfullResponse() throws Exception {
        // given
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest();
        createCustomerRequest.setTelephone("5452320454");
        createCustomerRequest.setPassword("123456");
        createCustomerRequest.setIdentityNumber("11111111111");
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate birthday = LocalDate.of(1998, 3, 3);
        createCustomerRequest.setBirthDay(Date.from(birthday.atStartOfDay(defaultZoneId).toInstant()));
        createCustomerRequest.setName("Engin");
        createCustomerRequest.setSurname("Akin");
        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customers/sing-up").
                contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCustomerRequest)));

        // then
        resultActions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void givenInvalidAgeCustomerRequest_whenCreate_thenReturnsSuccessfullResponse() throws Exception {
        // given
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest();
        createCustomerRequest.setTelephone("5452320454");
        createCustomerRequest.setPassword("123456");
        createCustomerRequest.setIdentityNumber("11111111111");
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate birthday = LocalDate.of(2018, 3, 3); // age under 18
        createCustomerRequest.setBirthDay(Date.from(birthday.atStartOfDay(defaultZoneId).toInstant()));
        createCustomerRequest.setName("Engin");
        createCustomerRequest.setSurname("Akin");
        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customers/sing-up").
                contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCustomerRequest)));
        // then
        resultActions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful", CoreMatchers.is(false)));
    }

    @Test
    void givenAlreadyRegistered_whenCreate_thenThrowsAlredyRegisterException() throws Exception {
        // given
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest();
        createCustomerRequest.setTelephone("5452320454");
        createCustomerRequest.setPassword("123456");
        createCustomerRequest.setIdentityNumber("111111111110");
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate birthday = LocalDate.of(2018, 3, 3); // age under 18
        createCustomerRequest.setBirthDay(Date.from(birthday.atStartOfDay(defaultZoneId).toInstant()));
        createCustomerRequest.setName("Engin");
        createCustomerRequest.setSurname("Akin");
        Customer customer= Customer.builder().identityNumber(createCustomerRequest.getIdentityNumber()).birthDay(createCustomerRequest.getBirthDay()).name(createCustomerRequest.getName()).password(createCustomerRequest.getPassword()).surname(createCustomerRequest.getSurname()).build();
        customerRepository.save(customer);
        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customers/sing-up").
                contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCustomerRequest)));
        // then
        resultActions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful", CoreMatchers.is(false)));
    }



}
