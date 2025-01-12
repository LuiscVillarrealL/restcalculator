package com.lcvl.challenge;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.lcvl.challenge.calculator.messaging.CalculatorKafkaMessageProducer;
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
  private CalculatorKafkaMessageProducer kafkaProducer;

  @InjectMocks
  private CalculationService calculationService;

  @Test
  void testCalculationDecision() {
    CalculationRequest request = new CalculationRequest("123", OperationEnum.SUM, BigDecimal.ONE,
        BigDecimal.TEN);

    // Mock behavior
    when(arithmeticService.addNumbers(BigDecimal.ONE, BigDecimal.TEN))
        .thenReturn(BigDecimal.valueOf(11));

    // Call the method
    calculationService.calculationDecision(request);

    // Verify interaction
    verify(arithmeticService).addNumbers(BigDecimal.ONE, BigDecimal.TEN);
    verify(kafkaProducer).sendMessage(any(CalculationResponse.class), eq("123"));
  }
}
