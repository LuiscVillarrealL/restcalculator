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
import com.lcvl.challenge.calculator.messaging.KafkaMessageProducer;
import com.lcvl.challenge.common.dto.CalculationResponse;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application.properties")
class KafkaMessageProducerTest {

  @Mock
  private KafkaTemplate<String, CalculationResponse> kafkaTemplate;

  @InjectMocks
  private KafkaMessageProducer kafkaMessageProducer;
  
  @BeforeEach
  void setup() throws Exception {
    Field field = KafkaMessageProducer.class.getDeclaredField("correlationId");
    field.setAccessible(true);
    field.set(kafkaMessageProducer, "Request-ID");
  }

  @Test
  void testSendCalculationResponse() {
    CalculationResponse response = new CalculationResponse("123", BigDecimal.TEN, null);
    String resultTopic = "test-topic";
    ReflectionTestUtils.setField(kafkaMessageProducer, "resultTopic", resultTopic);

    kafkaMessageProducer.sendCalculationResponse(response);

    verify(kafkaTemplate).send(resultTopic, response);
  }
}