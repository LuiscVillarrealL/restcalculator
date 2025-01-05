package com.lcvl.challenge.calculator.service;

import java.math.BigDecimal;
import java.math.MathContext;
import org.springframework.stereotype.Service;

/**
 * The Class ArithmeticService.
 */
@Service
public class ArithmeticService {

  /**
   * Adds the numbers.
   *
   * @param num1 the num 1
   * @param num2 the num 2
   * @return the big decimal
   */
  public BigDecimal addNumbers(BigDecimal num1, BigDecimal num2) {
    return num1.add(num2);
  }

  /**
   * Subtract numbers.
   *
   * @param num1 the num 1
   * @param num2 the num 2
   * @return the big decimal
   */
  public BigDecimal subtractNumbers(BigDecimal num1, BigDecimal num2) {
    return num1.subtract(num2);
  }

  /**
   * Multiply numbers.
   *
   * @param num1 the num 1
   * @param num2 the num 2
   * @return the big decimal
   */
  public BigDecimal multiplyNumbers(BigDecimal num1, BigDecimal num2) {
    return num1.multiply(num2);
  }

  /**
   * Divide numbers.
   *
   * @param num1 the num 1
   * @param num2 the num 2
   * @return the big decimal
   */
  public BigDecimal divideNumbers(BigDecimal num1, BigDecimal num2) {
    if (num2.equals(BigDecimal.ZERO)) {
      throw new ArithmeticException("Division by zero is not allowed.");
    }
    return num1.divide(num2, MathContext.DECIMAL128);
  }

}
