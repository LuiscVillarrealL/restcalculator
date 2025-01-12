package com.lcvl.challenge.rest.messaging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.lcvl.challenge.common.dto.CalculationRequest;
import com.lcvl.challenge.common.messaging.BaseKafkaProducer;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class RestKafkaMessageProducer.
 */
@Service
@Slf4j
public class RestKafkaMessageProducer extends BaseKafkaProducer<String, CalculationRequest> {


  /**
   * Instantiates a new rest kafka message producer.
   *
   * @param kafkaTemplate the kafka template
   * @param requestTopic the request topic
   * @param correlationId the correlation id
   */
  public RestKafkaMessageProducer(KafkaTemplate<String, CalculationRequest> kafkaTemplate,
      @Value("${kafka.request-topic}") String requestTopic,
      @Value("${mdc.correlation.id:Request-ID}") String correlationId) {
    super(kafkaTemplate, requestTopic, correlationId);
  }
}