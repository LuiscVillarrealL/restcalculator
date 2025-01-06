package com.lcvl.challenge.rest.messaging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.lcvl.challenge.common.dto.CalculationRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class KafkaProducer.
 */
@Service
@Slf4j
public class KafkaMessageProducer {

  /** The kafka template. */
  private final KafkaTemplate<String, CalculationRequest> kafkaTemplate;

  /** The request topic. */
  private final String requestTopic;

  /**
   * Instantiates a new kafka message producer.
   *
   * @param kafkaTemplate the kafka template
   * @param requestTopic the request topic
   */
  public KafkaMessageProducer(KafkaTemplate<String, CalculationRequest> kafkaTemplate,
      @Value("${kafka.request-topic:default-result-topic}") String requestTopic) {
    this.kafkaTemplate = kafkaTemplate;
    this.requestTopic = requestTopic;
  }

  /**
   * Send calculation request.
   *
   * @param request the request
   */
  public void sendCalculationRequest(CalculationRequest request) {

    log.info("Sending message from {} topic {}", requestTopic, request);
    kafkaTemplate.send(requestTopic, request);
  }

}
