package org.norma.finalproject.customer.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.norma.finalproject.customer.core.utilities.CustomerConstant;
import org.norma.finalproject.customer.entity.Customer;
import org.norma.finalproject.customer.entity.Role;
import org.norma.finalproject.customer.repository.CustomerRepository;
import org.norma.finalproject.customer.service.impl.CustomerServiceImpl;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private RoleService roleService;
    @InjectMocks
    private CustomerServiceImpl underTest;

    @Test
    public void givenCustomer_whenSave_thenReturnsCustomerObject() {

        //given
        Customer customer = new Customer();
        customer.setName("ENGIN");
        customer.setSurname("AKIN");
        customer.setEmail("engin@test.com");
        customer.setIdentityNumber("111111111");
        Role role = new Role();
        role.setName(CustomerConstant.ROLE_USER);
        BDDMockito.given(roleService.getRoleByName(CustomerConstant.ROLE_USER)).willReturn(role);
        BDDMockito.given(customerRepository.save(customer)).willReturn(customer);
        // when
        Customer savedCustomer = underTest.save(customer);
        // then
        Assertions.assertThat(savedCustomer.getEmail()).isEqualTo(customer.getEmail());
        Assertions.assertThat(savedCustomer.getRoles().size()).isEqualTo(1);


    }

    @Test
    public void givenValidIdentityNumber_whenFindCustomerByIdentity_thenReturnsCustomerObject() {
        // given
        String validIdentity = "111111111";
        Customer customer = new Customer();
        customer.setName("ENGIN");
        customer.setSurname("AKIN");
        customer.setEmail("engin@test.com");
        customer.setIdentityNumber(validIdentity);
        BDDMockito.given(customerRepository.findByIdentityNumber(validIdentity)).willReturn(Optional.of(customer));

        // when
        Optional<Customer> optionalCustomer = underTest.findCustomerByIdentity(validIdentity);

        // then
        Assertions.assertThat(optionalCustomer).isNotEmpty();
        Assertions.assertThat(optionalCustomer.get().getIdentityNumber()).isEqualTo(validIdentity);

    }


    @Test
    public void givenValidIdentityNumber_whenFindCustomerById_thenReturnsCustomerObject() {
        // given
        Customer customer = new Customer();
        Long customerID = 1L;
        customer.setId(customerID);
        customer.setName("ENGIN");
        customer.setSurname("AKIN");
        customer.setEmail("engin@test.com");
        customer.setIdentityNumber("111111111");
        BDDMockito.given(customerRepository.findById(customerID)).willReturn(Optional.of(customer));

        // when
        Optional<Customer> optionalCustomer = underTest.findByCustomerById(customerID);

        // then
        Assertions.assertThat(optionalCustomer).isNotEmpty();
        Assertions.assertThat(optionalCustomer.get().getId()).isEqualTo(customerID);

    }

    @Test
    public void givenInvalidIdentityNumber_whenFindCustomerById_thenReturnsEmpty() {
        // given
        Long invalidID = 2L;
        BDDMockito.given(customerRepository.findById(invalidID)).willReturn(Optional.empty());
        // when
        Optional<Customer> optionalCustomer = underTest.findByCustomerById(invalidID);

        // then
        Assertions.assertThat(optionalCustomer).isEmpty();

    }

    @Test
    public void givenValidEmail_whenFindCustomerByEmail_thenReturns() {
        // given
        Customer customer = new Customer();
        String validEmail = "Valid@gmail.com";
        customer.setId(1L);
        customer.setName("ENGIN");
        customer.setSurname("AKIN");
        customer.setEmail(validEmail);
        customer.setIdentityNumber("111111111");
        BDDMockito.given(customerRepository.findByEmail(validEmail)).willReturn(Optional.of(customer));

        // when
        Optional<Customer> optionalCustomer = underTest.findCustomerByEmail(validEmail);

        // then
        Assertions.assertThat(optionalCustomer).isNotEmpty();
    }

    @Test
    public void givenCustomerList_whenGetall_thenReturnsCustomerList() {
        // given
        Customer custome1 = new Customer();
        custome1.setId(1L);
        custome1.setName("ENGIN");
        custome1.setSurname("AKIN");
        custome1.setEmail("email");
        custome1.setIdentityNumber("111111111");
        Customer custome2 = new Customer();
        custome2.setId(1L);
        custome2.setName("ENGIN");
        custome2.setSurname("AKIN");
        custome2.setEmail("email");
        custome2.setIdentityNumber("111111111");
        Customer custome3 = new Customer();
        custome3.setId(1L);
        custome3.setName("ENGIN");
        custome3.setSurname("AKIN");
        custome3.setEmail("email");
        custome3.setIdentityNumber("111111111");
        List<Customer> customers = List.of(custome1, custome2, custome3);
        customerRepository.saveAll(customers);
        BDDMockito.given(customerRepository.findAll()).willReturn(customers);

        // when
        List<Customer> customerList = underTest.getall();

        // then
        Assertions.assertThat(customerList).isNotEmpty();
        Assertions.assertThat(customerList.size()).isEqualTo(3);

    }
}