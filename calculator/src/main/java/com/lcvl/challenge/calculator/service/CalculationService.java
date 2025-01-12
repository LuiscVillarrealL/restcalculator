package com.lcvl.challenge.calculator.service;

import java.math.BigDecimal;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.lcvl.challenge.calculator.messaging.CalculatorKafkaMessageProducer;
import com.lcvl.challenge.common.dto.CalculationRequest;
import com.lcvl.challenge.common.dto.CalculationResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class CalculationService.
 */
@Service
@Slf4j
public class CalculationService {

  /**
   * Instantiates a new calculation service.
   *
   * @param arithmeticService the arithmetic service
   * @param kafkaProducer the kafka producer
   */
  public CalculationService(ArithmeticService arithmeticService,
      CalculatorKafkaMessageProducer kafkaProducer) {
    this.arithmeticService = arithmeticService;
    this.kafkaProducer = kafkaProducer;
    this.correlationId = "Request-ID"; // Default value in case @Value injection fails
  }

  private final ArithmeticService arithmeticService;
  private final CalculatorKafkaMessageProducer kafkaProducer;

  @Value("${mdc.correlation.id")
  private String correlationId;

  /**
   * Calculation decision.
   *
   * @param calculationRequest the calculation request
   */
  public void calculationDecision(CalculationRequest calculationRequest) {

    BigDecimal result = null;

    try {

      switch (calculationRequest.getOperation()) {
        case SUM:
          result = arithmeticService.addNumbers(calculationRequest.getNum1(),
              calculationRequest.getNum2());
          System.out.println("result " + result);
          break;
        case SUBTRACT:
          result = arithmeticService.subtractNumbers(calculationRequest.getNum1(),
              calculationRequest.getNum2());
          break;
        case MULTIPLY:
          result = arithmeticService.multiplyNumbers(calculationRequest.getNum1(),
              calculationRequest.getNum2());
          break;
        case DIVIDE:
          result = arithmeticService.divideNumbers(calculationRequest.getNum1(),
              calculationRequest.getNum2());
          break;
        default:
          throw new IllegalArgumentException(
              "Unsupported operation: " + calculationRequest.getOperation());
      }

      MDC.put(correlationId, calculationRequest.getRequestId());

      log.info("Calculation completed for request");

      MDC.clear();
      kafkaProducer.sendMessage(
          new CalculationResponse(calculationRequest.getRequestId(), result, null),
          calculationRequest.getRequestId());

    } catch (Exception e) {
      MDC.put(correlationId, calculationRequest.getRequestId());
      log.error("Error processing request: {}", e.getMessage(), e);
      MDC.clear();

      kafkaProducer.sendMessage(
          new CalculationResponse(calculationRequest.getRequestId(), result, e.getMessage()),
          calculationRequest.getRequestId());

    }

  }

}
