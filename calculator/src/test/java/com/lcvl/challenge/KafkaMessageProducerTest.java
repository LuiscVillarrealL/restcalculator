package com.lcvl.challenge;

import static org.mockito.Mockito.verify;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import com.lcvl.challenge.calculator.messaging.CalculatorKafkaMessageProducer;
import com.lcvl.challenge.common.dto.CalculationResponse;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application.properties")
class KafkaMessageProducerTest {

  @Mock
  private KafkaTemplate<String, CalculationResponse> kafkaTemplate;

  private CalculatorKafkaMessageProducer kafkaMessageProducer;

  @BeforeEach
  void setup() {
    String resultTopic = "test-topic";
    String correlationId = "Request-ID";
    kafkaMessageProducer = new CalculatorKafkaMessageProducer(kafkaTemplate, resultTopic,
        correlationId);
  }

  @Test
  void testSendMessage() {
    CalculationResponse response = new CalculationResponse("123", BigDecimal.TEN, null);

    kafkaMessageProducer.sendMessage(response, "123");

    verify(kafkaTemplate).send("test-topic", response);
  }
}