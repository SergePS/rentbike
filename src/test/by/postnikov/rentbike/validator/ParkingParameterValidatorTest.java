package test.by.postnikov.rentbike.validator;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import by.postnikov.rentbike.validator.ParkingParameterValidator;

public class ParkingParameterValidatorTest {
	
	
	  //address
	  @DataProvider
	  public Object[][] dpAddressPositive() {
	    return new Object[][] {
	      new Object[] {"г.Минск, ул Бядули. 25"},
	      new Object[] {"New York, Manhattan, Central Park"},
	    };
	  } 
	  
	  @DataProvider
	  public Object[][] dpAddressNegative() {
	    return new Object[][] {
	      new Object[] {"!Минск"},
	      new Object[] {"*Минск"},
	    };
	  }
	  
	  
	  //capacity
	  @DataProvider
	  public Object[][] dpCapacityPositive() {
	    return new Object[][] {
	      new Object[] {"1"},
	      new Object[] {"3"},
	      new Object[] {"99"},
	    };
	  } 
	  
	  @DataProvider
	  public Object[][] dpCapacityNegative() {
	    return new Object[][] {
	      new Object[] {"0"},
	      new Object[] {""},
	      new Object[] {"s1"},
	      new Object[] {"1000"},
	    };
	  }

	  @Test(dataProvider = "dpAddressPositive")
	  public void addressValidatePositive(String address) {
		  assertTrue(ParkingParameterValidator.addressValidate(address));
	  }
	  
	  @Test(dataProvider = "dpAddressNegative")
	  public void addressValidateNegative(String address) {
		  assertFalse(ParkingParameterValidator.addressValidate(address));
	  }
	
	  @Test(dataProvider = "dpCapacityPositive")
	  public void capacityValidatePositive(String capacity) {
		  assertTrue(ParkingParameterValidator.capacityValidate(capacity));
	  }
	  
	  @Test(dataProvider = "dpCapacityNegative")
	  public void capacityValidateNegative(String capacity) {
		  assertFalse(ParkingParameterValidator.capacityValidate(capacity));
	  }
}
