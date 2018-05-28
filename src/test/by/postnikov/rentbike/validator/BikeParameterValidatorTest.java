package test.by.postnikov.rentbike.validator;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import by.postnikov.rentbike.validator.BikeParameterValidator;

public class BikeParameterValidatorTest {

  @DataProvider
  public Object[][] dpCountPositive() {
    return new Object[][] {
      new Object[] {"1"},
  new Object[] {"9"},
  new Object[] {"4"},
    };
  } 
  
  //count
  @DataProvider
  public Object[][] dpCountNegative() {
    return new Object[][] {
      new Object[] {"<2"},
      new Object[] {"0"},
      new Object[] {"10"},
    };
  }
  
  //amount
  @DataProvider
  public Object[][] dpAmountPositive() {
    return new Object[][] {
      new Object[] {"725.01"},
      new Object[] {"110.52" },
      new Object[] {"15"},
    };
  }
  
  @DataProvider
  public Object[][] dpAmountNegative() {
    return new Object[][] {
      new Object[] {"025.01"},
      new Object[] {"x10.52"},
      new Object[] {"15.."},
    };
  }
  
  //model
  @DataProvider
  public Object[][] dpModelPositive() {
    return new Object[][] {
      new Object[] {"model 9.0"},
      new Object[] {"Модель" },
      new Object[] {"MODEL 9_ 0"},
    };
  }
  
  @DataProvider
  public Object[][] dpModelNegative() {
    return new Object[][] {
      new Object[] {"<model"},
      new Object[] {"модель!*"},
      new Object[] {"модель>"},
    };
  } 
  
  //wheelSize
  @DataProvider
  public Object[][] dpWheelSizePositive() {
    return new Object[][] {
      new Object[] {"1"},
      new Object[] {"99"},
      new Object[] {"12"},
    };
  } 
  
  @DataProvider
  public Object[][] dpWheelSizeNegative() {
    return new Object[][] {
      new Object[] {"<10"},
      new Object[] {"0"},
      new Object[] {"100"},
      new Object[] {""},
    };
  }
  
  //speedCount
  @DataProvider
  public Object[][] dpSpeedCountPositive() {
    return new Object[][] {
      new Object[] {"1"},
      new Object[] {"99"},
      new Object[] {"12"},
    };
  } 
  
  @DataProvider
  public Object[][] dpSpeedCountNegative() {
    return new Object[][] {
      new Object[] {"<10"},
      new Object[] {"0"},
      new Object[] {"100"},
      new Object[] {""},
    };
  }
  
  //bykeType
  @DataProvider
  public Object[][] dpBikeTypePositive() {
    return new Object[][] {
      new Object[] {"type"},
      new Object[] {"Тип"},
      new Object[] {"Type"},
    };
  } 
  
  @DataProvider
  public Object[][] dpBikeTypeNegative() {
    return new Object[][] {
      new Object[] {"<type"},
      new Object[] {"!type"},
      new Object[] {"11"},
    };
  }
  
  //brand
  @DataProvider
  public Object[][] dpBrandPositive() {
    return new Object[][] {
      new Object[] {"Brand"},
      new Object[] {"brand123"},
      new Object[] {"аяАЯёЁazAZ -_"},
    };
  } 
  
  @DataProvider
  public Object[][] dpBrandNegative() {
    return new Object[][] {
      new Object[] {"<brand"},
      new Object[] {"!brand"},
      new Object[] {"*.брэнд"},
    };
  }

  //count
  @Test(dataProvider = "dpCountPositive")
  public void countValidatePostitve(String count) {
	  assertTrue(BikeParameterValidator.countValidate(count));
  }
  
  @Test(dataProvider = "dpCountNegative")
  public void countValidateNegative(String count) {
	  assertFalse(BikeParameterValidator.countValidate(count));
  }
  
  //amount
  @Test(dataProvider = "dpAmountPositive")
  public void amountValidatePositive(String amount) {
	  assertTrue(BikeParameterValidator.amountValidate(amount));
  }
  
  @Test(dataProvider = "dpAmountNegative")
  public void amountValidateNegative(String amount) {
	  assertFalse(BikeParameterValidator.amountValidate(amount));
  }
  
  //model
  @Test(dataProvider = "dpModelPositive")
  public void modelValidatePositive(String model) {
	  assertTrue(BikeParameterValidator.modelValidate(model));
  }
  
  @Test(dataProvider = "dpModelNegative")
  public void modelValidateNegative(String model) {
	  assertFalse(BikeParameterValidator.modelValidate(model));
  }

  //wheelsize
  @Test(dataProvider = "dpWheelSizePositive")
  public void wheelSizeValidatePositive(String wheelSize) {
	  assertTrue(BikeParameterValidator.wheelSizeValidate(wheelSize));
  }
  
  @Test(dataProvider = "dpWheelSizeNegative")
  public void wheelSizeValidateNegative(String wheelSize) {
	  assertFalse(BikeParameterValidator.wheelSizeValidate(wheelSize));
  }
  
  //speedCount
  @Test(dataProvider = "dpSpeedCountPositive")
  public void speedCountValidatePositive(String speedCount) {
	  assertTrue(BikeParameterValidator.speedCountValidate(speedCount));
  }
  
  @Test(dataProvider = "dpSpeedCountNegative")
  public void speedCountValidateNegative(String speedCount) {
	  assertFalse(BikeParameterValidator.speedCountValidate(speedCount));
  }
 
  //bikeType
  @Test(dataProvider = "dpBikeTypePositive")
  public void bikeTypeValidationPositive(String bikeType) {
	  assertTrue(BikeParameterValidator.bikeTypeValidate(bikeType));
  }
  
  @Test(dataProvider = "dpBikeTypeNegative")
  public void bikeTypeValidationNegative(String bikeType) {
	  assertFalse(BikeParameterValidator.bikeTypeValidate(bikeType));
  }
 
  //brand
  @Test(dataProvider = "dpBrandPositive")
  public void brandValidationPositive(String brand) {
	  assertTrue(BikeParameterValidator.brandValidate(brand));
  }
  
  @Test(dataProvider = "dpBrandNegative")
  public void brandValidationNegative(String brand) {
	  assertFalse(BikeParameterValidator.brandValidate(brand));
  }
}
