package com.ebookshop.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.ebookshop.common.entity.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTests {
	@Autowired
	private CustomerRepository repo;
	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateCustomer1() {
		Integer countryId = 234;
		Country country = entityManager.find(Country.class, countryId);

		Customer customer = new Customer();
		customer.setCountry(country);
		customer.setFirstName("David");
		customer.setLastName("Fountaine");
		customer.setPassword("password123");
		customer.setEmail("david.s.fountaine@gmail.com");
		customer.setPhoneNumber("312-462-7518");
		customer.setAddressLine1("1927 West Drive");
		customer.setCity("Sacramento");
		customer.setState("California");
		customer.setPostalCode("95867");
		customer.setCreatedTime(new Date());

		Customer savedCustomer = repo.save(customer);

		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isGreaterThan(0);

	}

	@Test
	public void testListCustomers() {
		Iterable<Customer> customers = repo.findAll();
		customers.forEach(System.out::println);

		assertThat(customers).hasSizeGreaterThan(1);
	}

	@Test
	public void testUpdateCustomer() {
		Integer customerId = 1;
		String lastName = "Stanfield";

		Customer customer = repo.findById(customerId).get();
		customer.setLastName(lastName);
		customer.setEnable(true);

		Customer updatedCustomer = repo.save(customer);
		assertThat(updatedCustomer.getLastName()).isEqualTo(lastName);
	}
	
	@Test
	public void testGetCustomer() {
		Integer customerId = 2;
		Optional<Customer> findById = repo.findById(customerId);
		
		assertThat(findById).isPresent();
		
		Customer customer = findById.get();
		System.out.println(customer);
	}
	@Test
	public void testDeleteCustomer() {
		Integer customerId = 2;
		repo.deleteById(customerId);
		
		Optional<Customer> findById = repo.findById(customerId);
		assertThat(findById).isNotPresent();
	}
	
	@Test
	public void testFindByEmail() {
		String email = "david.s.fountaine@gmail.com";
		Customer customer = repo.findByEmail(email);
		
		assertThat(customer).isNotNull();
		System.out.println(customer);
	}
	
	@Test
	public void testFindByVerification() {
		String code ="code_123";
		Customer customer = repo.findByVerificationCode(code);
		
		assertThat(customer).isNotNull();
		System.out.println(customer);
	}
	
	@Test
	public void testEnableCustomer() {
		Integer customerId = 1;
		repo.enable(customerId);
		
		Customer customer = repo.findById(customerId).get();
		assertThat(customer.isEnable()).isTrue();
	}
}
