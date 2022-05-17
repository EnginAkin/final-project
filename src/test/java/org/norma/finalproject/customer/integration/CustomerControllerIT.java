package org.norma.finalproject.customer.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.norma.finalproject.customer.core.model.request.CreateCustomerRequest;
import org.norma.finalproject.customer.core.utilities.CustomerConstant;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(CustomerConstant.SIGNUP_SUCESSFULL)));
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
        createCustomerRequest.setIdentityNumber("11111111111");
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
