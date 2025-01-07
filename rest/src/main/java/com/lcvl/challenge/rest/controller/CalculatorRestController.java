package com.lcvl.challenge.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.lcvl.challenge.common.util.OperationEnum;
import com.lcvl.challenge.rest.dto.ResultDto;
import com.lcvl.challenge.rest.util.CalculationServiceHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class CalculatorRestController.
 */
@RestController
@RequestMapping("/api/calculator")
@Slf4j
public class CalculatorRestController {

  /** The calculation service helper. */
  @Autowired
  private CalculationServiceHelper calculationServiceHelper;

  /**
   * Gets the sum.
   *
   * @param num1 the num 1
   * @param num2 the num 2
   * @param requestId the request id
   * @return the sum
   */
  @GetMapping(value = "/sum")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<ResultDto> getSum(@RequestParam char[] num1, @RequestParam char[] num2,
      @RequestHeader(value = "Request-ID", required = false) String requestId) {

    return calculationServiceHelper.handleCalculationRequest(num1, num2, requestId,
        OperationEnum.SUM);

  }

  /**
   * Gets the substraction.
   *
   * @param num1 the num 1
   * @param num2 the num 2
   * @param requestId the request id
   * @return the substraction
   */
  @GetMapping(value = "/sub")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<ResultDto> getSubstraction(@RequestParam char[] num1,
      @RequestParam char[] num2,
      @RequestHeader(value = "Request-ID", required = false) String requestId) {

    return calculationServiceHelper.handleCalculationRequest(num1, num2, requestId,
        OperationEnum.SUBTRACT);
  }

  /**
   * Gets the multiplication.
   *
   * @param num1 the num 1
   * @param num2 the num 2
   * @param requestId the request id
   * @return the multiplication
   */
  @GetMapping(value = "/multi")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<ResultDto> getMultiplication(@RequestParam char[] num1,
      @RequestParam char[] num2,
      @RequestHeader(value = "Request-ID", required = false) String requestId) {

    return calculationServiceHelper.handleCalculationRequest(num1, num2, requestId,
        OperationEnum.MULTIPLY);
  }

  /**
   * Gets the division.
   *
   * @param num1 the num 1
   * @param num2 the num 2
   * @param requestId the request id
   * @return the division
   */
  @GetMapping(value = "/div")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<ResultDto> getDivision(@RequestParam char[] num1, @RequestParam char[] num2,
      @RequestHeader(value = "Request-ID", required = false) String requestId) {

    return calculationServiceHelper.handleCalculationRequest(num1, num2, requestId,
        OperationEnum.DIVIDE);
  }

}
