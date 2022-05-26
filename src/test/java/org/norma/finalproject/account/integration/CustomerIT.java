package org.norma.finalproject.account.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.norma.finalproject.common.security.jwt.JWTHelper;
import org.norma.finalproject.customer.core.utilities.CustomerConstant;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.entity.Role;
import org.norma.finalproject.customer.repository.CustomerRepository;
import org.norma.finalproject.customer.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;
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


    @WithMockUser(value = "spring")
    @Test
    public void getall() throws Exception {
        Customer customer=new Customer();
        customer.setIdentityNumber("11111111111");
        customer.setName("murat");
        customer.setSurname("ss");
        Customer customer1=new Customer();
        customer.setIdentityNumber("11111111111");
        customer.setName("murat");
        customer.setSurname("ss");
        customerRepository.saveAll(List.of(customer1,customer));
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers/getall").
                contentType(MediaType.APPLICATION_JSON));

        response.andDo(MockMvcResultHandlers.print());


    }



}
