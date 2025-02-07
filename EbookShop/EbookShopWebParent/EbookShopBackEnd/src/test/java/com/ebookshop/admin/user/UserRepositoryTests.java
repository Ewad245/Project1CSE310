package com.ebookshop.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.ebookshop.common.entity.Role;
import com.ebookshop.common.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

	@Autowired
	private UserRepository repo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User User1 = new User("user1@gmail.com", "nhan01212999877", "Tan", "Nhan");
		User1.addRole(roleAdmin);

		User savedUser = repo.save(User1);

		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateUserWithTwoRoles() {
		User user2 = new User("user2@gmail.com", "nhan123456", "Thanh", "Nhan");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);

		user2.addRole(roleEditor);
		user2.addRole(roleAssistant);

		User savedUser = repo.save(user2);

		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user -> System.out.println(user));

	}

	@Test
	public void testGetUserById() {
		User user1 = repo.findById(1).get();
		System.out.println(user1);
		assertThat(user1).isNotNull();

	}
	
	
	@Test
	public void testUpdateUsersDetails() {
		User user1 = repo.findById(1).get();
		user1.setEnabled(true);
		user1.setEmail("user123@gmail.com");
		
		repo.save(user1);
	}
	
	
	@Test
	public void testUpdateUserRole() {
		User user2 = repo.findById(2).get();
		Role roleEditor = new Role(3);
		Role roleSalePerson = new Role(2);
		
		user2.getRoles().remove(roleEditor);
		user2.addRole(roleSalePerson);
		
		repo.save(user2);
		
	}
	
	
	@Test
	public void testDeleteUser() {
		Integer userId = 18;
		repo.deleteById(userId);
		
	}
	
	
	@Test
	public void testGetUserByEmail() {
		String email = "nhanviporo258@gmail.com";
		User user = repo.getUserByEmail(email);
		
		
		assertThat(user).isNotNull();
	}
	
	
	@Test
	public void testCountById() {
		Integer id = 32;
		Long countById = repo.countById(id);
		
		assertThat(countById).isNotNull().isGreaterThan(0);
	}
	
	@Test
	public void testDisableUser() {
		Integer id = 25;
		repo.updateEnabledStatus(id, false);
		
	}
	
	@Test
	public void testEnableUser() {
		Integer id = 25;
		repo.updateEnabledStatus(id, true);
		
	}
	
	@Test
	public void testListFirstPage() {
		int pageNumber = 0;
		int pageSize = 4;
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(pageable);
		
		List<User> listUsers =page.getContent();
		
		listUsers.forEach(user -> System.out.println(user));
		
		
		assertThat(listUsers.size()).isEqualTo(pageSize);
	}

}
