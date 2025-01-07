package com.lcvl.challenge.calculator.messaging;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.lcvl.challenge.common.dto.CalculationResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class KafkaProducer.
 */
@Service
@Slf4j
public class KafkaMessageProducer {

  private final KafkaTemplate<String, CalculationResponse> kafkaTemplate;

  private final String resultTopic;
  
  @Value("${mdc.correlation.id}")
  private String correlationId;

  /**
   * Instantiates a new kafka message producer.
   *
   * @param kafkaTemplate the kafka template
   * @param resultTopic the result topic
   */
  public KafkaMessageProducer(KafkaTemplate<String, CalculationResponse> kafkaTemplate,
      @Value("${kafka.result-topic:default-result-topic}") String resultTopic) {
    this.kafkaTemplate = kafkaTemplate;
    this.resultTopic = resultTopic;
  }

  /**
   * Send calculation response.
   *
   * @param request the request
   */
  public void sendCalculationResponse(CalculationResponse request) {
    
    MDC.put(correlationId, request.getRequestId());
    log.info("Sending message from {} topic {}", resultTopic, request);    
    MDC.clear();
    
    
    kafkaTemplate.send(resultTopic, request);
  }

}
