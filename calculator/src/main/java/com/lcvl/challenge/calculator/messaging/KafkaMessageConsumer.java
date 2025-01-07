package com.lcvl.challenge.calculator.messaging;

import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.lcvl.challenge.calculator.service.CalculationService;
import com.lcvl.challenge.common.dto.CalculationRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class KafkaConsumer.
 */
@Service
@Slf4j
@AllArgsConstructor
public class KafkaMessageConsumer {

  private final CalculationService calculationService;

  /**
   * Topic listen.
   *
   * @param calculationRequest the calculation request
   */
  @KafkaListener(topics = "${kafka.request-topic}", groupId = "${spring.kafka.consumer.group-id}")
  public void topicListen(CalculationRequest calculationRequest) {

    MDC.put("requestId", calculationRequest.getRequestId());
    
    log.info("Got message from calculation-request topic");
    
    MDC.clear();

    calculationService.calculationDecision(calculationRequest);

  }



}
