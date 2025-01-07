package com.lcvl.challenge.rest.controller;

import java.math.BigDecimal;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.lcvl.challenge.common.dto.CalculationRequest;
import com.lcvl.challenge.common.dto.CalculationResponse;
import com.lcvl.challenge.common.util.OperationEnum;
import com.lcvl.challenge.rest.dto.ResultDto;
import com.lcvl.challenge.rest.messaging.KafkaMessageConsumer;
import com.lcvl.challenge.rest.messaging.KafkaMessageProducer;
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

  /** The kafka message producer. */
  @Autowired
  private KafkaMessageProducer kafkaMessageProducer;

  /** The kafka message consumer. */
  @Autowired
  private KafkaMessageConsumer kafkaMessageConsumer;

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
  public ResponseEntity<ResultDto> getSum(@RequestParam BigDecimal num1,
      @RequestParam BigDecimal num2,
      @RequestHeader(value = "Request-ID", required = false) String requestId) {

    if (requestId == null || requestId.isEmpty()) {
      requestId = MDC.get("Request-ID");
    }

    try {

      // Log the request
      log.info("Operation: sum, Operands: num1={}, num2={}", num1, num2);

      // Send the request to the Kafka topic with the generated Request-ID
      kafkaMessageProducer
          .sendCalculationRequest(new CalculationRequest(requestId, OperationEnum.SUM, num1, num2));

      return waitForResponse(requestId);

    }
    finally {
      // Clear MDC after the request is processed
      MDC.clear();
    }
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
  public ResponseEntity<ResultDto> getSubstraction(@RequestParam BigDecimal num1,
      @RequestParam BigDecimal num2,
      @RequestHeader(value = "Request-ID", required = false) String requestId) {

    // Generate a unique identifier if not provided
    if (requestId == null || requestId.isEmpty()) {
      requestId = UUID.randomUUID().toString();
    }

    // Log the request
    log.info("Operation: substraction, Operands: a={}, b={}", num1, num2);

    kafkaMessageProducer.sendCalculationRequest(
        new CalculationRequest(requestId, OperationEnum.SUBTRACT, num1, num2));

    return waitForResponse(requestId);
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
  public ResponseEntity<ResultDto> getMultiplication(@RequestParam BigDecimal num1,
      @RequestParam BigDecimal num2,
      @RequestHeader(value = "Request-ID", required = false) String requestId) {

    // Generate a unique identifier if not provided
    if (requestId == null || requestId.isEmpty()) {
      requestId = UUID.randomUUID().toString();
    }

    // Log the request
    log.info("Operation: multiplication, Operands: a={}, b={}", num1, num2);

    kafkaMessageProducer.sendCalculationRequest(
        new CalculationRequest(requestId, OperationEnum.MULTIPLY, num1, num2));

    return waitForResponse(requestId);
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
  public ResponseEntity<ResultDto> getDivision(@RequestParam BigDecimal num1,
      @RequestParam BigDecimal num2,
      @RequestHeader(value = "Request-ID", required = false) String requestId) {

    // Generate a unique identifier if not provided
    if (requestId == null || requestId.isEmpty()) {
      requestId = UUID.randomUUID().toString();
    }

    // Check for division by zero
    if (num2.compareTo(BigDecimal.ZERO) == 0) {
      log.warn("Operation: division, Division by zero attempted with num1={}", num1);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new ResultDto("Division by zero is not allowed"));
    }

    // Log the request
    log.info("Operation: division, Operands: num1={}, num2={}", num1, num2);

    kafkaMessageProducer.sendCalculationRequest(
        new CalculationRequest(requestId, OperationEnum.DIVIDE, num1, num2));

    return waitForResponse(requestId);
  }

  /**
   * Wait for response.
   *
   * @param requestId the request id
   * @return the response entity
   */
  private ResponseEntity<ResultDto> waitForResponse(String requestId) {
    // Wait for the response
    try {
      CalculationResponse response = kafkaMessageConsumer.getResponseById(requestId, 5000);

      if (response == null) {
        log.warn("No response received");
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

}
