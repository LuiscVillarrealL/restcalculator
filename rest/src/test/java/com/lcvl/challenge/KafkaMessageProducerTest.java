package com.lcvl.challenge;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import com.lcvl.challenge.rest.dto.CalculationRequest;
import com.lcvl.challenge.rest.messaging.KafkaMessageProducer;
import com.lcvl.challenge.util.OperationEnum;

class KafkaMessageProducerTest {

  private KafkaMessageProducer kafkaMessageProducer;
  private KafkaTemplate<String, CalculationRequest> kafkaTemplate;

  @SuppressWarnings("unchecked")
  @BeforeEach
  void setUp() {
    kafkaTemplate = mock(KafkaTemplate.class); // Mock the KafkaTemplate
    kafkaMessageProducer = new KafkaMessageProducer(kafkaTemplate, "test-request-topic");
  }

  @Test
  void testSendCalculationRequest() {
    // Arrange: Create a CalculationRequest object
    CalculationRequest request = new CalculationRequest("test-id", OperationEnum.SUM,
        BigDecimal.TEN, BigDecimal.ONE);

    // Act: Call the method under test
    kafkaMessageProducer.sendCalculationRequest(request);

    // Assert: Verify that KafkaTemplate's send method was called with the correct arguments
    verify(kafkaTemplate, times(1)).send("test-request-topic", request);
  }
}
