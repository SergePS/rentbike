package test.by.postnikov.rentbike.validator;

import org.testng.annotations.Test;

import by.postnikov.rentbike.validator.UserParameterValidator;

import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.AfterClass;

public class UserParameterValidatorTest {

	@DataProvider
	public Object[][] birthDayPositive() {
		return new Object[][] { 
			new Object[] {"25.02.2007"}, 
			new Object[] {"22.01.2006"},
			new Object[] {"22.01.1970"}, 
			};
	}
	
	@DataProvider
	public Object[][] birthDayNegative() {
		return new Object[][] { 
			new Object[] {"31.02.2007"}, 
			new Object[] {"22.01.2017"},
			new Object[] {"-01.01.1970"}, 
			};
	}
	
	@DataProvider
	public Object[][] datePositive() {
		return new Object[][] { 
			new Object[] {"25.02.2007"}, 
			new Object[] {"22.01.2017"},
			new Object[] {"30.01.1970"}, 
			};
	}
	
	@DataProvider
	public Object[][] dateNegative() {
		return new Object[][] { 
			new Object[] {"31.02.2007"}, 
			new Object[] {"32.01.2017"},
			new Object[] {"-01.01.1970"}, 
			};
	}
	
	@DataProvider
	public Object[][] passwPositive() {
		return new Object[][] { 
			new Object[] {"123".toCharArray()}, 
			new Object[] {"ewqwe12_".toCharArray()},
			new Object[] {"Wsd123Q".toCharArray()}, 
			};
	}
	
	@DataProvider
	public Object[][] passwNegative() {
		return new Object[][] { 
			new Object[] {"<sdfs01".toCharArray()}, 
			new Object[] {"!sf22".toCharArray()},
			new Object[] {"-5465".toCharArray()}, 
			};
	}

	@BeforeClass
	public void beforeClass() {
	}

	@AfterClass
	public void afterClass() {
	}

	@Test(dataProvider = "birthDayPositive")
	public void birthdayValidatePositive(String birthday) {
		assertTrue(UserParameterValidator.birthdayValidate(birthday));
	}
	
	@Test(dataProvider = "birthDayNegative")
	public void birthdayValidateNegative(String birthday) {
		assertFalse(UserParameterValidator.birthdayValidate(birthday));
	}
	
	@Test(dataProvider = "datePositive")
	public void dateValidatePositive(String date) {
		assertTrue(UserParameterValidator.dateValidate(date));
	}
	
	@Test(dataProvider = "dateNegative")
	public void dateValidateNegative(String date) {
		assertFalse(UserParameterValidator.dateValidate(date));
	}

	@Test
	public void craditcardValidate() {

	}

	@Test
	public void emailValidate() {

	}

	@Test
	public void loginValidate() {

	}

	@Test
	public void nameValidate() {

	}

	@Test(dataProvider = "passwPositive")
	public void passwordValidatePositive(char[] password) {
		assertTrue(UserParameterValidator.passwordValidate(password));
	}
	
	@Test(dataProvider = "passwNegative")
	public void passwordValidateNegative(char[] password) {
		assertFalse(UserParameterValidator.passwordValidate(password));
	}
}
