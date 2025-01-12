package com.lcvl.challenge.common.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * The Class BaseKafkaConsumer.
 *
 * @param <V> the value type
 */
public abstract class BaseKafkaConsumer<V> {

  private static final Logger log = LoggerFactory.getLogger(BaseKafkaConsumer.class);
  protected final String correlationId;

  /**
   * Instantiates a new base kafka consumer.
   *
   * @param correlationId the correlation id
   */
  public BaseKafkaConsumer(String correlationId) {
    this.correlationId = correlationId;
  }

  /**
   * Process message.
   *
   * @param message the message
   * @param requestId the request id
   */
  public void processMessage(V message, String requestId) {
    try {
      MDC.put(correlationId, requestId);
      log.info("Processing message: {}", message);
      handleMessage(message);
    }
    finally {
      MDC.clear();
    }
  }

  /**
   * Handle message.
   *
   * @param message the message
   */
  protected abstract void handleMessage(V message);
}