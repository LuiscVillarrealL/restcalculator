package com.lcvl.challenge;

import static org.mockito.Mockito.verify;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;
import com.lcvl.challenge.calculator.messaging.KafkaMessageConsumer;
import com.lcvl.challenge.calculator.service.CalculationService;
import com.lcvl.challenge.common.dto.CalculationRequest;
import com.lcvl.challenge.common.util.OperationEnum;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application.properties")
class KafkaMessageConsumerTest {

  @Mock
  private CalculationService calculationService;

  @InjectMocks
  private KafkaMessageConsumer kafkaMessageConsumer;

  @Test
  void testTopicListen() {
    CalculationRequest request = new CalculationRequest("123", OperationEnum.SUM, BigDecimal.ONE,
        BigDecimal.TEN);

    kafkaMessageConsumer.topicListen(request);

    verify(calculationService).calculationDecision(request);
  }
}
