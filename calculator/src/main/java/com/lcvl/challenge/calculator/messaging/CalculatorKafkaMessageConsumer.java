package com.lcvl.challenge.calculator.messaging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.lcvl.challenge.calculator.service.CalculationService;
import com.lcvl.challenge.common.dto.CalculationRequest;
import com.lcvl.challenge.common.messaging.BaseKafkaConsumer;
import lombok.extern.slf4j.Slf4j;

// TODO: Auto-generated Javadoc
/**
 * The Class CalculatorKafkaMessageConsumer.
 */
@Service
@Slf4j
public class CalculatorKafkaMessageConsumer extends BaseKafkaConsumer<CalculationRequest> {

  private final CalculationService calculationService;

  /**
   * Instantiates a new calculator kafka message consumer.
   *
   * @param calculationService the calculation service
   * @param correlationId the correlation id
   */
  public CalculatorKafkaMessageConsumer(CalculationService calculationService,
      @Value("${mdc.correlation.id:Request-ID}") String correlationId) {
    super(correlationId);
    this.calculationService = calculationService;
  }

  /**
   * Handle message.
   *
   * @param message the message
   */
  @Override
  protected void handleMessage(CalculationRequest message) {
    log.info("Delegating request to calculation service for requestId: {}", message.getRequestId());
    calculationService.calculationDecision(message);
  }

  /**
   * Topic listen.
   *
   * @param calculationRequest the calculation request
   */
  @KafkaListener(topics = "${kafka.request-topic}", groupId = "${spring.kafka.consumer.group-id}")
  public void topicListen(CalculationRequest calculationRequest) {
    processMessage(calculationRequest, calculationRequest.getRequestId());
  }
}
