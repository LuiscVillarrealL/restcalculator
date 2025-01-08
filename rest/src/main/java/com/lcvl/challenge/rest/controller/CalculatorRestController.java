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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class CalculatorRestController.
 */
@RestController
@RequestMapping("/api/calculator")
@Slf4j
@Tag(name = "Calculator", description = "Endpoints for arithmetic operations")
@ApiResponses({ @ApiResponse(responseCode = "400", description = "Invalid request"),
    @ApiResponse(responseCode = "500", description = "Internal server error"),
    @ApiResponse(responseCode = "200", description = "Calculation successful") })
public class CalculatorRestController {

  /** The calculation service helper. */
  @Autowired
  private CalculationServiceHelper calculationServiceHelper;

  /**
   * Gets the sum.
   *
   * @param a the num 1
   * @param b the num 2
   * @param requestId the request id
   * @return the sum
   */
  @Operation(summary = "Add two numbers", description = "Returns the sum of num1 and num2.")
  @GetMapping(value = "/sum")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<ResultDto> getSum(@RequestParam char[] a, @RequestParam char[] b,
      @RequestHeader(value = "Request-ID", required = false) String requestId) {

    return calculationServiceHelper.handleCalculationRequest(a, b, requestId, OperationEnum.SUM);

  }

  /**
   * Gets the substraction.
   *
   * @param a the num 1
   * @param b the num 2
   * @param requestId the request id
   * @return the substraction
   */
  @Operation(
      summary = "Substract two numbers",
      description = "Returns the substractions of num1 and num2.")
  @GetMapping(value = "/sub")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<ResultDto> getSubstraction(@RequestParam char[] a, @RequestParam char[] b,
      @RequestHeader(value = "Request-ID", required = false) String requestId) {

    return calculationServiceHelper.handleCalculationRequest(a, b, requestId,
        OperationEnum.SUBTRACT);
  }

  /**
   * Gets the multiplication.
   *
   * @param a the num 1
   * @param b the num 2
   * @param requestId the request id
   * @return the multiplication
   */
  @Operation(
      summary = "Multiply two numbers",
      description = "Returns the multiplication of num1 and num2.")
  @GetMapping(value = "/multi")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<ResultDto> getMultiplication(@RequestParam char[] a, @RequestParam char[] b,
      @RequestHeader(value = "Request-ID", required = false) String requestId) {

    return calculationServiceHelper.handleCalculationRequest(a, b, requestId,
        OperationEnum.MULTIPLY);
  }

  /**
   * Gets the division.
   *
   * @param a the num 1
   * @param b the num 2
   * @param requestId the request id
   * @return the division
   */
  @Operation(summary = "Divide two numbers", description = "Returns the division of num1 and num2.")
  @GetMapping(value = "/div")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<ResultDto> getDivision(@RequestParam char[] a, @RequestParam char[] b,
      @RequestHeader(value = "Request-ID", required = false) String requestId) {

    return calculationServiceHelper.handleCalculationRequest(a, b, requestId, OperationEnum.DIVIDE);
  }

}
