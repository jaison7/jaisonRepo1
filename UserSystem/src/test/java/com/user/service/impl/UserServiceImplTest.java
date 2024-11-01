package com.user.service.impl;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import com.user.dto.RegistrationDto;
import com.user.sql.entity.User;
import com.user.sql.repo.UserRepository;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UserServiceImplTest {

	@InjectMocks
	UserServiceImpl usi;
	
	@Mock
	private UserRepository userRepository;
	
	private RegistrationDto rdto; 
	
	@Before
	public void init() {
		
		rdto = new RegistrationDto
				("Joe", "joe", "joe123", "joe@abc.com", 1);
	}
	
	@Test
	public void testRegisterUser() {
		
		when(userRepository.save(any())).thenReturn(new User());
		User user = usi.registerUser(rdto);
		assertNotNull(user);
	}

	@Test
	public void testSaveOrder() {
		fail("Not yet implemented");
	}

}
