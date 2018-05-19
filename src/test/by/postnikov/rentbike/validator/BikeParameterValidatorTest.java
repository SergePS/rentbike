package test.by.postnikov.rentbike.validator;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import by.postnikov.rentbike.validator.BikeParameterValidator;

public class BikeParameterValidatorTest {


  @DataProvider
  public Object[][] dpAmountPositive() {
    return new Object[][] {
      new Object[] { "725.01"},
      new Object[] { "110.52" },
      new Object[] { "15" },
    };
  }
  
  @DataProvider
  public Object[][] dpAmountNegative() {
    return new Object[][] {
      new Object[] { "025.01"},
      new Object[] { "x10.52" },
      new Object[] { "15.." },
    };
  }
  
  @BeforeClass
  public void beforeClass() {
  }

  @AfterClass
  public void afterClass() {

  }


  @Test(dataProvider = "dpAmountPositive")
  public void amountValidatePositive(String amount) {
	  assertTrue(BikeParameterValidator.amountValidate(amount));
  }
  
  @Test(dataProvider = "dpAmountNegative")
  public void amountValidateNegative(String amount) {
	  assertFalse(BikeParameterValidator.amountValidate(amount));
  }

  @Test
  public void bikeTypeIdValidation() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void brandValidation() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void countValidate() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void modelValidate() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void speedCountValidate() {
    throw new RuntimeException("Test not implemented");
  }

  @Test
  public void wheelSizeValidate() {
    throw new RuntimeException("Test not implemented");
  }
}
