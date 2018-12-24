package com.webservice.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.BDDMockito.*;

import com.webservice.model.User;
import com.webservice.model.User.Gender;
import com.webservice.service.UserService;
import com.google.common.collect.ImmutableList;

public class UserServiceTest {
	
	@Mock
	private FakeDataDao fakeDataDao;
	
	private UserService userService;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		userService = new UserService(fakeDataDao);
	}
	
	@Test
	public void shouldGetAllUsers() throws Exception {
		UUID annaUserUid = UUID.randomUUID();
	    User anna = new User(annaUserUid, "anna",
	        "montana", Gender.FEMALE, 30, "anna@gmail.com");
	    ImmutableList<User> users = new ImmutableList.Builder<User>()
	    		.add(anna)
	    		.build();
	    given(fakeDataDao.selectAllUsers()).willReturn(users);
	    
	    
		List<User> allUsers = userService.getAllUsers();
		assertThat(allUsers).hasSize(1);
		
	    User user = users.get(0);

	    assertUserField(anna);
	}
	
	@Test
	public void shouldGetUser() throws Exception {
		UUID annaUserUid = UUID.randomUUID();
		User anna = new User(annaUserUid, "anna",
		        "montana", Gender.FEMALE, 30, "anna@gmail.com");
		
		given(fakeDataDao.selectUserByUserUid(annaUserUid))
			.willReturn(Optional.ofNullable(anna));
		
		Optional<User> userOptional = userService.getUser(annaUserUid);
		assertThat(userOptional.isPresent()).isTrue();
		User user = userOptional.get();
		assertUserField(user);
	}
	
	@Test
	public void updateUser() throws Exception {
		UUID annaUserUid = UUID.randomUUID();
		User anna = new User(annaUserUid, "anna",
		        "montana", Gender.FEMALE, 30, "anna@gmail.com");
		
		given(fakeDataDao.selectUserByUserUid(annaUserUid))
					.willReturn(Optional.ofNullable(anna));
		/*
		anna = new User(annaUserUid, "anna",
		        "tuta", Gender.FEMALE, 29, "anna22@yandex.com");
		given(fakeDataDao.updateUser(anna)).willReturn(1);
		*/
		ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
		
		
		int updateResult = userService.updateUser(anna);
		verify(fakeDataDao).selectUserByUserUid(annaUserUid);
		
		verify(fakeDataDao).updateUser(captor.capture());
		
		User user = captor.getValue();
		
		assertUserField(user);
		assertThat(updateResult).isEqualTo(1);
	}
	
	@Test
	public void removeUser() throws Exception {
		
	}

	@Test
	public void insertUser() throws Exception {
		
	}
	
	private void assertUserField(User user) {
	    assertThat(user.getAge()).isEqualTo(30);
	    assertThat(user.getFirstName()).isEqualTo("anna");
	    assertThat(user.getLastName()).isEqualTo("montana");
	    assertThat(user.getGender()).isEqualTo(Gender.FEMALE);
	    assertThat(user.getEmail()).isEqualTo("anna@gmail.com");
	    assertThat(user.getUserUid()).isNotNull();
	}
}