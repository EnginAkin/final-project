package org.norma.finalproject.customer.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.norma.finalproject.customer.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository underTest;

    @Test
    public void givenInvalidIdentityNumber_whenFindByIdentityNumber_thenReturnsCustomerObject() {
        // given
        Customer customer = new Customer();
        String invalidIdentityNumber = "00000000000";
        customer.setIdentityNumber("111111111111");
        customer.setName("ENGIN");
        customer.setSurname("AKIN");
        underTest.save(customer);
        // when
        Optional<Customer> optionalCustomer = underTest.findByIdentityNumber(invalidIdentityNumber);
        // then
        Assertions.assertThat(optionalCustomer).isEmpty();

    }

    @Test
    public void givenValidIdentityNumber_whenFindByIdentityNumber_thenReturnsCustomerObject() {
        // given
        Customer customer = new Customer();
        String validIdentityNumber = "111111111111";
        customer.setIdentityNumber(validIdentityNumber);
        customer.setName("ENGIN");
        customer.setSurname("AKIN");
        underTest.save(customer);
        // when
        Optional<Customer> optionalCustomer = underTest.findByIdentityNumber(validIdentityNumber);
        // then
        Assertions.assertThat(optionalCustomer).isNotEmpty();
        Assertions.assertThat(optionalCustomer.get().getIdentityNumber()).isEqualTo(validIdentityNumber);

    }

    @Test
    public void givenValidEmail_whenFindByEmail_thenReturnsCustomerObject() {
        // given
        Customer customer = new Customer();
        String validEmail = "norma.test@gmail.com";
        customer.setEmail(validEmail);
        customer.setName("ENGIN");
        customer.setSurname("AKIN");
        underTest.save(customer);
        // when
        Optional<Customer> optionalCustomer = underTest.findByEmail(validEmail);
        // then
        Assertions.assertThat(optionalCustomer).isNotEmpty();
        Assertions.assertThat(optionalCustomer.get().getEmail()).isEqualTo(validEmail);

    }

    @Test
    public void givenInvalidEmail_whenFindByEmail_thenReturnsCustomerObject() {
        // given
        Customer customer = new Customer();
        String inValidEmail = "norma.test@gmail.com";
        customer.setEmail("invalidEmail");
        customer.setName("ENGIN");
        customer.setSurname("AKIN");
        underTest.save(customer);
        // when
        Optional<Customer> optionalCustomer = underTest.findByEmail(inValidEmail);
        // then
        Assertions.assertThat(optionalCustomer).isEmpty();

    }


}