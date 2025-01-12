package com.lcvl.challenge.rest.util;

import java.math.BigDecimal;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.lcvl.challenge.common.dto.CalculationRequest;
import com.lcvl.challenge.common.dto.CalculationResponse;
import com.lcvl.challenge.common.util.OperationEnum;
import com.lcvl.challenge.rest.dto.ResultDto;
import com.lcvl.challenge.rest.exceptions.DividingByZeroException;
import com.lcvl.challenge.rest.messaging.RestKafkaMessageConsumer;
import com.lcvl.challenge.rest.messaging.RestKafkaMessageProducer;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class CalculationServiceHelper.
 */
@Service
@Slf4j
public class CalculationServiceHelper {

  /** The kafka message producer. */
  @Autowired
  private RestKafkaMessageProducer kafkaMessageProducer;

  /** The kafka message consumer. */
  @Autowired
  private RestKafkaMessageConsumer kafkaMessageConsumer;

  @Value("${mdc.correlation.id}")
  String correlationId;

  /**
   * Handle calculation request.
   *
   * @param num1 the num 1
   * @param num2 the num 2
   * @param requestId the request id
   * @param operation the operation
   * @return the response entity
   * @throws DividingByZeroException dividing by zero exception
   */
  public ResponseEntity<ResultDto> handleCalculationRequest(BigDecimal num1, BigDecimal num2,
      String requestId, OperationEnum operation) throws DividingByZeroException {

    if (requestId == null || requestId.isEmpty()) {
      requestId = MDC.get(correlationId);
    }

    try {

      // Check for division by zero
      if (operation == OperationEnum.DIVIDE && num2.compareTo(BigDecimal.ZERO) == 0) {
        log.warn("Operation: division, Division by zero attempted with num1={}, num2={}", num1,
            num2);
        throw new DividingByZeroException("Division by zero is not allowed");
      }

      // Log the request
      log.info("Operation: {}, Operands: num1={}, num2={}", operation.name(), num1, num2);

      // Send the request to the Kafka topic with the generated Request-ID
      kafkaMessageProducer.sendMessage(
          new CalculationRequest(requestId, operation, num1, num2), requestId);

      return waitForResponse(requestId);

    } catch (NumberFormatException e) {
      // Return a bad request response if numbers are invalid
      return ResponseEntity.badRequest().header(correlationId, requestId)
          .body(new ResultDto("Invalid number format. Please provide valid numbers."));
    }
    finally {
      // Clear MDC after the request is processed
      MDC.clear();
    }

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
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).header(correlationId, requestId)
            .body(new ResultDto("Timeout waiting for response"));
      }

      // Build the ResultDto from the response
      ResultDto result = new ResultDto(response.getResult().toString());
      return ResponseEntity.ok().header("Request-ID", requestId).body(result);

    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log.error("Error waiting for response", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .header(correlationId, requestId).body(new ResultDto("Internal error occurred"));
    }
  }
}
