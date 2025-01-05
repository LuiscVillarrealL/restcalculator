package com.lcvl.challenge.rest.messaging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.lcvl.challenge.rest.dto.CalculationRequest;
import com.lcvl.challenge.rest.dto.CalculationResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class KafkaProducer.
 */
@Service
@Slf4j
public class KafkaMessageProducer {

  private final KafkaTemplate<String, CalculationRequest> kafkaTemplate;

  private final String requestTopic;

  public KafkaMessageProducer(KafkaTemplate<String, CalculationRequest> kafkaTemplate,
      @Value("${kafka.request-topic:default-result-topic}") String requestTopic) {
    this.kafkaTemplate = kafkaTemplate;
    this.requestTopic = requestTopic;
  }

  public void sendCalculationRequest(CalculationRequest request) {

    log.info("Sending message from {} topic {}", requestTopic, request);
    kafkaTemplate.send(requestTopic, request);
  }

}
