package com.lcvl.challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import com.lcvl.challenge.calculator.service.ArithmeticService;

class ArithmeticServiceTests {

  private final ArithmeticService calculatorService = new ArithmeticService();

  @Test
  void testAddNumbers() {

    BigDecimal num1 = new BigDecimal("10.5");
    BigDecimal num2 = new BigDecimal("20.25");

    BigDecimal result = calculatorService.addNumbers(num1, num2);

    assertEquals(new BigDecimal("30.75"), result, "Addition result should be correct.");
  }

  @Test
  void testSubtractNumbers() {

    BigDecimal num1 = new BigDecimal("50");
    BigDecimal num2 = new BigDecimal("20");

    BigDecimal result = calculatorService.subtractNumbers(num1, num2);

    assertEquals(new BigDecimal("30"), result, "Subtraction result should be correct.");
  }

  @Test
  void testMultiplyNumbers() {

    BigDecimal num1 = new BigDecimal("5");
    BigDecimal num2 = new BigDecimal("4");

    BigDecimal result = calculatorService.multiplyNumbers(num1, num2);

    assertEquals(new BigDecimal("20"), result, "Multiplication result should be correct.");
  }

  @Test
  void testDivideNumbers() {

    BigDecimal num1 = new BigDecimal("100");
    BigDecimal num2 = new BigDecimal("4");

    BigDecimal result = calculatorService.divideNumbers(num1, num2);

    assertEquals(new BigDecimal("25"), result, "Division result should be correct.");
  }

  @Test
  void testDivideNumbersByZero() {

    BigDecimal num1 = new BigDecimal("100");
    BigDecimal num2 = BigDecimal.ZERO;

    ArithmeticException exception = assertThrows(ArithmeticException.class, () -> {
      calculatorService.divideNumbers(num1, num2);
    });

    assertEquals("Division by zero is not allowed.", exception.getMessage());
  }

}
