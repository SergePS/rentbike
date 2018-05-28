package test.by.postnikov.rentbike.validator;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import by.postnikov.rentbike.validator.UserParameterValidator;

public class UserParameterValidatorTest {
	
	//id
	@DataProvider
	public Object[][] idPositive() {
		return new Object[][] { 
			new Object[] {"1"}, 
			new Object[] {"500"},
			new Object[] {"125000000"}, 
			};
	}
	
	@DataProvider
	public Object[][] idNegative() {
		return new Object[][] { 
			new Object[] {"0"}, 
			new Object[] {"-1"},
			new Object[] {"<3"},
			new Object[] {"f"}, 
			};
	}	
	
	//login
	@DataProvider
	public Object[][] loginPositive() {
		return new Object[][] { 
			new Object[] {"login"}, 
			new Object[] {"login25"},
			new Object[] {"l25"}, 
			};
	}
	
	@DataProvider
	public Object[][] loginNegative() {
		return new Object[][] { 
			new Object[] {"0login"}, 
			new Object[] {"<login"},
			new Object[] {"логин"},
			new Object[] {"lo"}, 
			};
	}
	
	//password
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
	
	//name
	@DataProvider
	public Object[][] namePositive() {
		return new Object[][] { 
			new Object[] {"name"}, 
			new Object[] {"Имя"},
			new Object[] {"Name"}, 
			};
	}
	
	@DataProvider
	public Object[][] nameNegative() {
		return new Object[][] { 
			new Object[] {"0name"}, 
			new Object[] {"<name"},
			new Object[] {"имя_"},
			new Object[] {"им"},
			new Object[] {"Имя."},
			};
	}
	

	//email
	@DataProvider
	public Object[][] emailPositive() {
		return new Object[][] { 
			new Object[] {"email@tut.by"}, 
			new Object[] {"email123@tut.com"},
			new Object[] {"email_124@gmail.com"}, 
			};
	}
	
	@DataProvider
	public Object[][] emailNegative() {
		return new Object[][] { 
			new Object[] {"0email@tut.by"}, 
			new Object[] {"<email@tut.by"},
			new Object[] {"email@tu.t.by"},
			new Object[] {"почта@tut.by"},
			};
	}
	
	
	//birthday
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
	
	//date
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
	


	//id
	@Test(dataProvider = "idPositive")
	public void idValidatePositive(String id) {
		assertTrue(UserParameterValidator.idValidate(id));
	}
	
	@Test(dataProvider = "idNegative")
	public void idValidateNegative(String id) {
		assertFalse(UserParameterValidator.idValidate(id));
	}
	
	
	//login
	@Test(dataProvider = "loginPositive")
	public void loginValidatePositive(String login) {
		assertTrue(UserParameterValidator.loginValidate(login));
	}
	
	@Test(dataProvider = "loginNegative")
	public void loginValidateNegative(String login) {
		assertFalse(UserParameterValidator.loginValidate(login));
	}
	
	
	//password
	@Test(dataProvider = "passwPositive")
	public void passwordValidatePositive(char[] password) {
		assertTrue(UserParameterValidator.passwordValidate(password));
	}
	
	@Test(dataProvider = "passwNegative")
	public void passwordValidateNegative(char[] password) {
		assertFalse(UserParameterValidator.passwordValidate(password));
	}
	
	//name
	@Test(dataProvider = "namePositive")
	public void nameValidatePositive(String name) {
		assertTrue(UserParameterValidator.nameValidate(name));
	}
	
	@Test(dataProvider = "nameNegative")
	public void nameValidateNegative(String name) {
		assertFalse(UserParameterValidator.nameValidate(name));
	}
	
	//email
	@Test(dataProvider = "emailPositive")
	public void emailValidatePositive(String email) {
		assertTrue(UserParameterValidator.emailValidate(email));
	}
	
	@Test(dataProvider = "emailNegative")
	public void emailValidateNegative(String email) {
		assertFalse(UserParameterValidator.emailValidate(email));
	}
	
	//birthday
	@Test(dataProvider = "birthDayPositive")
	public void birthdayValidatePositive(String birthday) {
		assertTrue(UserParameterValidator.birthdayValidate(birthday));
	}
	
	@Test(dataProvider = "birthDayNegative")
	public void birthdayValidateNegative(String birthday) {
		assertFalse(UserParameterValidator.birthdayValidate(birthday));
	}
	
	//date
	@Test(dataProvider = "datePositive")
	public void dateValidatePositive(String date) {
		assertTrue(UserParameterValidator.dateValidate(date));
	}
	
	@Test(dataProvider = "dateNegative")
	public void dateValidateNegative(String date) {
		assertFalse(UserParameterValidator.dateValidate(date));
	}
}
