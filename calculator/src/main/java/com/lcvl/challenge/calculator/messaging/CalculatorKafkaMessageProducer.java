package com.lcvl.challenge.calculator.messaging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.lcvl.challenge.common.dto.CalculationResponse;
import com.lcvl.challenge.common.messaging.BaseKafkaProducer;
import lombok.extern.slf4j.Slf4j;


/**
 * The Class CalculatorKafkaMessageProducer.
 */
@Service
@Slf4j
public class CalculatorKafkaMessageProducer extends BaseKafkaProducer<String, CalculationResponse> {

  /**
   * Instantiates a new calculator Kafka message producer.
   *
   * @param kafkaTemplate the Kafka template
   * @param resultTopic the result topic
   * @param correlationId the correlation ID key for MDC
   */
  public CalculatorKafkaMessageProducer(
      KafkaTemplate<String, CalculationResponse> kafkaTemplate,
      @Value("${kafka.result-topic}") String resultTopic,
      @Value("${mdc.correlation.id:Request-ID}") String correlationId) {
    super(kafkaTemplate, resultTopic, correlationId);
  }
}
