package test.by.postnikov.rentbike.dao.implementation;

import static org.testng.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import by.postnikov.rentbike.command.RequestParameter;
import by.postnikov.rentbike.entity.User;
import by.postnikov.rentbike.exception.ServiceException;
import by.postnikov.rentbike.service.ServiceFactory;
import by.postnikov.rentbike.service.UserService;

public class SqlUserDAOTest {
	private final static String TEST_USER_LOGIN = "stas";
	private final static String TEST_USER_PASSWORD = "qwerty";
	private final static String TEST_USER_NAME = "Станислав";
	ServiceFactory serviceFactory;
	UserService userService;
	User user;
	Map<String, String> requestParameters;

	@BeforeClass
	public void beforeClass() {
		serviceFactory = ServiceFactory.getInstance();
		userService = serviceFactory.getUserService();
		requestParameters = new HashMap<>();
		requestParameters.put(RequestParameter.LOGIN.parameter(), TEST_USER_LOGIN);
		
		user = new User();
		user.setName(TEST_USER_NAME);
	}

	@AfterClass
	public void afterClass() {
		serviceFactory = null;
		userService = null;
		user = null;
		requestParameters = null;
	}

	@Test
	public void getUser() throws ServiceException {
		User userExpected = new User();
		userService.login(requestParameters, TEST_USER_PASSWORD.toCharArray(), userExpected);
		assertEquals(userExpected.getName(), user.getName());

	}
}
