package com.lcvl.challenge;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.lcvl.challenge.calculator.messaging.KafkaMessageProducer;
import com.lcvl.challenge.calculator.service.ArithmeticService;
import com.lcvl.challenge.calculator.service.CalculationService;
import com.lcvl.challenge.common.dto.CalculationRequest;
import com.lcvl.challenge.common.dto.CalculationResponse;
import com.lcvl.challenge.common.util.OperationEnum;

@ExtendWith(MockitoExtension.class)
class CalculationServiceTest {

  @Mock
  private ArithmeticService arithmeticService;

  @Mock
  private KafkaMessageProducer kafkaProducer;

  @InjectMocks
  private CalculationService calculationService;

  @Test
  void testCalculationDecision() {
    CalculationRequest request = new CalculationRequest("123", OperationEnum.SUM, BigDecimal.ONE,
        BigDecimal.TEN);
    when(arithmeticService.addNumbers(any(), any())).thenReturn(BigDecimal.valueOf(11));

    calculationService.calculationDecision(request);

    verify(arithmeticService).addNumbers(BigDecimal.ONE, BigDecimal.TEN);
    verify(kafkaProducer).sendCalculationResponse(any(CalculationResponse.class));
  }


}
