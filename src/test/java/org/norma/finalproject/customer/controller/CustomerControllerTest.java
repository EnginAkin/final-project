package org.norma.finalproject.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.norma.finalproject.common.response.GeneralErrorResponse;
import org.norma.finalproject.common.response.GeneralResponse;
import org.norma.finalproject.common.security.user.CustomUserDetailsService;
import org.norma.finalproject.common.security.user.UserDetail;
import org.norma.finalproject.customer.core.exception.NotAcceptableAgeException;
import org.norma.finalproject.customer.core.model.request.CreateCustomerRequest;
import org.norma.finalproject.customer.core.utilities.CustomerConstant;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.service.FacadeCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;


@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacadeCustomerService facadeCustomerService;



    // TODO Controller testleri çalışmıyor.

/*0
    // response içinde general response yok
    @Test
    void givenCreateCustomerRequest_whenCreate_thenReturnsSuccessfullResponse() throws Exception {
        // given
        CreateCustomerRequest createCustomerRequest=new CreateCustomerRequest();
        createCustomerRequest.setTelephone("5452320454");
        createCustomerRequest.setPassword("123456");
        createCustomerRequest.setIdentityNumber("11111111111");
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate birthday = LocalDate.of(1998, 3, 3);
        createCustomerRequest.setBirthDay(Date.from(birthday.atStartOfDay(defaultZoneId).toInstant()));
        createCustomerRequest.setName("Engin");
        createCustomerRequest.setSurname("Akin");
        GeneralResponse generalSuccessfullResponse=new GeneralResponse(CustomerConstant.SIGNUP_SUCESSFULL,true);
        BDDMockito.given(facadeCustomerService.signup(createCustomerRequest)).willReturn(generalSuccessfullResponse);
        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customers/sing-up")
                .with(user("test_user").roles("ROLE_USER")).with(csrf()).
                contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCustomerRequest)));



        // then

        resultActions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());



    }

/*
// TODO response donmuyor .
    @Test
    void givenUnder18YearsOld_whenCreate_thenReturnsSuccessfullResponse() throws Exception {
        // given
        CreateCustomerRequest createCustomerRequest=new CreateCustomerRequest();
        createCustomerRequest.setTelephone("5452320454");
        createCustomerRequest.setPassword("123456");
        createCustomerRequest.setIdentityNumber("11111111111");
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate birthday = LocalDate.of(2018, 3, 3);
        createCustomerRequest.setBirthDay(Date.from(birthday.atStartOfDay(defaultZoneId).toInstant()));
        createCustomerRequest.setName("Engin");
        createCustomerRequest.setSurname("Akin");
        BDDMockito.given(facadeCustomerService.signup(createCustomerRequest)).willThrow(NotAcceptableAgeException.class);
        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/customers/sing-up").
                contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCustomerRequest)));

        // then

        GeneralErrorResponse errorResponse=new GeneralErrorResponse("You must be over 18 years old.");
        resultActions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());



    }


 */





}