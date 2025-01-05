package com.lcvl.challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.lcvl.challenge.rest.dto.CalculationResponse;
import com.lcvl.challenge.rest.messaging.KafkaMessageConsumer;

class KafkaMessageConsumerTest {

  private KafkaMessageConsumer kafkaMessageConsumer;

  @BeforeEach
  void setUp() {
    kafkaMessageConsumer = new KafkaMessageConsumer();
  }

  @Test
  void testTopicListen() throws InterruptedException {
    // Arrange: Create a mock CalculationResponse
    CalculationResponse response = new CalculationResponse();
    response.setRequestId("test-id");
    response.setResult(BigDecimal.TEN);
    response.setError(null);

    // Act: Pass the message to the topicListen method
    kafkaMessageConsumer.topicListen(response);

    // Assert: Verify the response is stored in the map
    CalculationResponse storedResponse = kafkaMessageConsumer.getResponseById("test-id", 100);
    assertNotNull(storedResponse, "The response should be stored in the map");
    assertEquals(response.getRequestId(), storedResponse.getRequestId());
    assertEquals(response.getResult(), storedResponse.getResult());
  }

  @Test
  void testGetResponseByIdWithTimeout() throws InterruptedException {
    // Arrange: Create a mock CalculationResponse
    CalculationResponse response = new CalculationResponse();
    response.setRequestId("timeout-test-id");
    response.setResult(BigDecimal.ONE);
    response.setError(null);

    // Add the response to the consumer's map
    kafkaMessageConsumer.topicListen(response);

    // Act: Attempt to retrieve the response with a valid ID
    CalculationResponse retrievedResponse = kafkaMessageConsumer.getResponseById("timeout-test-id",
        500);

    // Assert: Verify the response is retrieved
    assertNotNull(retrievedResponse, "The response should be retrieved before the timeout");
    assertEquals(response.getRequestId(), retrievedResponse.getRequestId());
  }

  @Test
  void testGetResponseByIdTimeoutOccurred() throws InterruptedException {
    // Act: Attempt to retrieve a non-existent response
    CalculationResponse retrievedResponse = kafkaMessageConsumer.getResponseById("non-existent-id",
        500);

    // Assert: Verify that the method times out and returns null
    assertNull(retrievedResponse, "The response should be null if the timeout occurs");
  }

  @Test
  void testRemoveResponseAfterConsumption() throws InterruptedException {
    // Arrange: Add a response
    CalculationResponse response = new CalculationResponse();
    response.setRequestId("removal-test-id");
    response.setResult(BigDecimal.TEN);
    response.setError(null);

    kafkaMessageConsumer.topicListen(response);

    // Act: Retrieve the response once
    CalculationResponse firstRetrieved = kafkaMessageConsumer.getResponseById("removal-test-id",
        100);
    CalculationResponse secondRetrieved = kafkaMessageConsumer.getResponseById("removal-test-id",
        100);

    // Assert: Verify the response is removed after being consumed
    assertNotNull(firstRetrieved, "The first retrieval should return the response");
    assertNull(secondRetrieved, "The response should be removed after the first consumption");
  }
}
