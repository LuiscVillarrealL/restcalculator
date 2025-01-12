package com.lcvl.challenge.rest.messaging;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.lcvl.challenge.common.dto.CalculationResponse;
import com.lcvl.challenge.common.messaging.BaseKafkaConsumer;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class RestKafkaMessageConsumer.
 */
@Service
@Slf4j
public class RestKafkaMessageConsumer extends BaseKafkaConsumer<CalculationResponse> {

  /**
   * Instantiates a new rest kafka message consumer.
   *
   * @param correlationId the correlation id
   */
  public RestKafkaMessageConsumer(@Value("${mdc.correlation.id:Request-ID}") String correlationId) {
    super(correlationId);
  }

  /** Maps messages with ids. */
  private final ConcurrentMap<String, CalculationResponse> responseMap = new ConcurrentHashMap<>();

  /**
   * Handle message.
   *
   * @param message the message
   */
  @Override
  protected void handleMessage(CalculationResponse message) {
    log.info("Storing response for requestId: {}", message.getRequestId());
    responseMap.put(message.getRequestId(), message);
  }

  /**
   * Gets the response by id.
   *
   * @param requestId the request id
   * @param timeoutMillis the timeout millis
   * @return the response by id
   * @throws InterruptedException the interrupted exception
   */
  public CalculationResponse getResponseById(String requestId, long timeoutMillis)
      throws InterruptedException {
    long startTime = System.currentTimeMillis();
    while (System.currentTimeMillis() - startTime < timeoutMillis) {
      if (responseMap.containsKey(requestId)) {
        return responseMap.remove(requestId); // Remove the response once consumed
      }
      Thread.sleep(100); // Poll every 100ms
    }
    return null; // Return null if timeout occurs
  }

  /**
   * Topic listen.
   *
   * @param calculationResponse the calculation response
   */
  @KafkaListener(topics = "${kafka.result-topic}", groupId = "${spring.kafka.consumer.group-id}")
  public void topicListen(CalculationResponse calculationResponse) {
    processMessage(calculationResponse, calculationResponse.getRequestId());
  }
}