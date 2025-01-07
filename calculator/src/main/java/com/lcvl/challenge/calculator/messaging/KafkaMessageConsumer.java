package com.lcvl.challenge.calculator.messaging;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.lcvl.challenge.calculator.service.CalculationService;
import com.lcvl.challenge.common.dto.CalculationRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class KafkaConsumer.
 */
@Service
@Slf4j
public class KafkaMessageConsumer {
  
  /**
   * Instantiates a new kafka message consumer.
   *
   * @param calculationService the calculation service
   */
  public KafkaMessageConsumer(CalculationService calculationService) {
    this.calculationService = calculationService;
  }

  private final CalculationService calculationService;
  
  @Value("${mdc.correlation.id}")
  private String correlationId;

  /**
   * Topic listen.
   *
   * @param calculationRequest the calculation request
   */
  @KafkaListener(topics = "${kafka.request-topic}", groupId = "${spring.kafka.consumer.group-id}")
  public void topicListen(CalculationRequest calculationRequest) {

    MDC.put(correlationId, calculationRequest.getRequestId());
    
    log.info("Got message from calculation-request topic");
    
    MDC.clear();

    calculationService.calculationDecision(calculationRequest);

  }



}
