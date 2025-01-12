package com.lcvl.challenge.common.messaging;

import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class BaseKafkaProducer.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
@Slf4j
public abstract class BaseKafkaProducer<K, V> {

  private final KafkaTemplate<K, V> kafkaTemplate;
  private final String topicName;
  private final String correlationId;

  /**
   * Instantiates a new base kafka producer.
   *
   * @param kafkaTemplate the Kafka template
   * @param topicName the topic name
   * @param correlationId the correlation ID key for MDC
   */
  protected BaseKafkaProducer(KafkaTemplate<K, V> kafkaTemplate, String topicName,
      String correlationId) {
    this.kafkaTemplate = kafkaTemplate;
    this.topicName = topicName;
    this.correlationId = correlationId;
  }

  /**
   * Send message.
   *
   * @param message the message
   * @param requestId the request ID
   */
  public void sendMessage(V message, String requestId) {
    try {
      MDC.put(correlationId, requestId);
      log.info("Sending message to topic {}: {}", topicName, message);
      kafkaTemplate.send(topicName, message);
    }
    finally {
      MDC.clear();
    }
  }
}
