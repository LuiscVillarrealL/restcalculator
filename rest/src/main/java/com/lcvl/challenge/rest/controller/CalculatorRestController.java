package com.lcvl.challenge.rest.controller;

import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.lcvl.challenge.rest.dto.CalculationRequest;
import com.lcvl.challenge.rest.dto.CalculationResponse;
import com.lcvl.challenge.rest.dto.ResultDto;
import com.lcvl.challenge.rest.messaging.KafkaMessageConsumer;
import com.lcvl.challenge.rest.messaging.KafkaMessageProducer;
import com.lcvl.challenge.util.OperationEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class CalculatorRestController.
 */
@RestController
@RequestMapping("/api/calculator")
@RequiredArgsConstructor
@Slf4j
public class CalculatorRestController {

  @Autowired
  private KafkaMessageProducer kafkaMessageProducer;

  @Autowired
  private KafkaMessageConsumer kafkaMessageConsumer;

  @GetMapping(value = "/sum")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<ResultDto> getSum(@RequestParam BigDecimal num1,
      @RequestParam BigDecimal num2,
      @RequestHeader(value = "Request-ID", required = false) String requestId) {

    // Generate a unique identifier if not provided
    if (requestId == null || requestId.isEmpty()) {
      requestId = UUID.randomUUID().toString();
    }

    // Log the request
    log.info("Request-ID: {}, Operation: sum, Operands: num1={}, num2={}", requestId, num1, num2);

    kafkaMessageProducer
        .sendCalculationRequest(new CalculationRequest(requestId, OperationEnum.SUM, num1, num2));

    // Wait for the response
    try {
      CalculationResponse response = kafkaMessageConsumer.getResponseById(requestId, 5000);
      
      if (response == null) {
        log.warn("No response received for Request-ID: {}", requestId);
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
            .body(new ResultDto("Timeout waiting for response"));
      }

      // Build the ResultDto from the response
      ResultDto result = new ResultDto(response.getResult().toString());
      return ResponseEntity.ok().header("Request-ID", requestId).body(result);

    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log.error("Error waiting for response", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ResultDto("Internal error occurred"));
    }
  }

//
//  /**
//   * Gets the substraction.
//   *
//   * @param num1 the num 1
//   * @param num2 the num 2
//   * @param requestId the request id
//   * @return the substraction
//   */
//  @GetMapping(value = "/sub")
//  @ResponseStatus(HttpStatus.OK)
//  public ResponseEntity<ResultResponse> getSubstraction(@RequestParam BigDecimal num1,
//      @RequestParam BigDecimal num2,
//      @RequestHeader(value = "Request-ID", required = false) String requestId) {
//
//    // Generate a unique identifier if not provided
//    if (requestId == null || requestId.isEmpty()) {
//      requestId = UUID.randomUUID().toString();
//    }
//
//    // Log the request
//    log.info("Request-ID: {}, Operation: substraction, Operands: a={}, b={}", 
//        requestId, num1, num2);
//
//    // Perform the calculation
//    BigDecimal result = num1.subtract(num2);
//
//    // Create a response
//    ResultResponse response = new ResultResponse(result);
//
//    return ResponseEntity.ok().header("Request-ID", requestId).body(response);
//  }
//
//  /**
//   * Gets the multiplication.
//   *
//   * @param num1 the num 1
//   * @param num2 the num 2
//   * @param requestId the request id
//   * @return the multiplication
//   */
//  @GetMapping(value = "/multi")
//  @ResponseStatus(HttpStatus.OK)
//  public ResponseEntity<ResultResponse> getMultiplication(@RequestParam BigDecimal num1,
//      @RequestParam BigDecimal num2,
//      @RequestHeader(value = "Request-ID", required = false) String requestId) {
//
//    // Generate a unique identifier if not provided
//    if (requestId == null || requestId.isEmpty()) {
//      requestId = UUID.randomUUID().toString();
//    }
//
//    // Log the request
//    log.info("Request-ID: {}, Operation: sum, Operands: a={}, b={}", requestId, num1, num2);
//
//    // Perform the calculation
//    BigDecimal result = num1.multiply(num2);
//
//    // Create a response
//    ResultResponse response = new ResultResponse(result);
//
//    return ResponseEntity.ok().header("Request-ID", requestId).body(response);
//  }
//  
//  /**
//   * Gets the division.
//   *
//   * @param num1 the num 1
//   * @param num2 the num 2
//   * @param requestId the request id
//   * @return the division
//   */
//  @GetMapping(value = "/div")
//  @ResponseStatus(HttpStatus.OK)
//  public ResponseEntity<ResultResponse> getDivision(@RequestParam BigDecimal num1,
//      @RequestParam BigDecimal num2,
//      @RequestHeader(value = "Request-ID", required = false) String requestId) {
//
//    // Generate a unique identifier if not provided
//    if (requestId == null || requestId.isEmpty()) {
//      requestId = UUID.randomUUID().toString();
//    }
//
//    // Log the request
//    log.info("Request-ID: {}, Operation: sum, Operands: a={}, b={}", requestId, num1, num2);
//
//    // Perform the calculation
//    BigDecimal result = num1.divide(num2, 2, RoundingMode.HALF_UP);
//
//    // Create a response
//    ResultResponse response = new ResultResponse(result);
//
//    return ResponseEntity.ok().header("Request-ID", requestId).body(response);
//  }

}
