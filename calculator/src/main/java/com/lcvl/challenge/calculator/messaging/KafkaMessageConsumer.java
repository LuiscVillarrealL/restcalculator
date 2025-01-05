package com.lcvl.challenge.calculator.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.lcvl.challenge.calculator.dto.CalculationRequest;
import com.lcvl.challenge.calculator.service.CalculationService;
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

    log.info("Got message from calculation-request topic {}", calculationRequest);

    calculationService.calculationDecision(calculationRequest);

  }



}
