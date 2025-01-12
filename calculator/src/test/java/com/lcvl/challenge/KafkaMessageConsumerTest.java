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
import org.springframework.test.context.TestPropertySource;
import com.lcvl.challenge.calculator.messaging.CalculatorKafkaMessageConsumer;
import com.lcvl.challenge.calculator.service.CalculationService;
import com.lcvl.challenge.common.dto.CalculationRequest;
import com.lcvl.challenge.common.util.OperationEnum;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = "mdc.correlation.id=Request-ID")
class KafkaMessageConsumerTest {

  @Mock
  private CalculationService calculationService;

  @InjectMocks
  private CalculatorKafkaMessageConsumer kafkaMessageConsumer;

  @BeforeEach
  void setup() {
    String correlationId = "Request-ID";
    kafkaMessageConsumer = new CalculatorKafkaMessageConsumer(calculationService, correlationId);
  }

  @Test
  void testTopicListen() {
    CalculationRequest request = new CalculationRequest("123", OperationEnum.SUM, BigDecimal.ONE,
        BigDecimal.TEN);

    kafkaMessageConsumer.topicListen(request);

    verify(calculationService).calculationDecision(request);
  }
}
