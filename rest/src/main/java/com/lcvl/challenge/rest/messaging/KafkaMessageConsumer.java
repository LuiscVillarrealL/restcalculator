package com.lcvl.challenge.rest.messaging;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.lcvl.challenge.common.dto.CalculationResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class KafkaMessageConsumer.
 */
@Service
@Slf4j
public class KafkaMessageConsumer {

  /** The response map. */
  private final ConcurrentMap<String, CalculationResponse> responseMap = new ConcurrentHashMap<>();

  /**
   * Topic listen.
   *
   * @param calculationResponse the calculation response
   */
  @KafkaListener(topics = "${kafka.result-topic}", groupId = "${spring.kafka.consumer.group-id}")
  public void topicListen(CalculationResponse calculationResponse) {
    log.info("Got message from calculation-response topic {}", calculationResponse);

    // Store the response keyed by requestId
    responseMap.put(calculationResponse.getRequestId(), calculationResponse);
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
}
