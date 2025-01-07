package com.lcvl.challenge.calculator.service;

import java.math.BigDecimal;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import com.lcvl.challenge.calculator.messaging.KafkaMessageProducer;
import com.lcvl.challenge.common.dto.CalculationRequest;
import com.lcvl.challenge.common.dto.CalculationResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class CalculationService.
 */
@Service
@Slf4j
@AllArgsConstructor
public class CalculationService {

  private final ArithmeticService arithmeticService;
  private final KafkaMessageProducer kafkaProducer;

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

      MDC.put("requestId", calculationRequest.getRequestId());

      log.info("Calculation completed for request");

      MDC.clear();
      kafkaProducer.sendCalculationResponse(
          new CalculationResponse(calculationRequest.getRequestId(), result, null));

    } catch (Exception e) {
      MDC.put("requestId", calculationRequest.getRequestId());
      log.error("Error processing request: {}", e.getMessage(), e);
      MDC.clear();

      kafkaProducer.sendCalculationResponse(
          new CalculationResponse(calculationRequest.getRequestId(), null, e.getMessage()));

    }

  }

}
