package com.lcvl.challenge;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.lcvl.challenge.calculator.dto.CalculationRequest;
import com.lcvl.challenge.calculator.dto.CalculationResponse;
import com.lcvl.challenge.calculator.messaging.KafkaMessageProducer;
import com.lcvl.challenge.calculator.service.ArithmeticService;
import com.lcvl.challenge.calculator.service.CalculationService;
import challenge.common.util.OperationEnum;

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

  @Test
  void testCalculationDecisionHandlesError() {
    CalculationRequest request = new CalculationRequest("123", OperationEnum.DIVIDE, BigDecimal.ONE,
        BigDecimal.ZERO);

    // Mock exception
    when(arithmeticService.divideNumbers(any(), any()))
        .thenThrow(new ArithmeticException("Division by zero"));

    // Call the service
    calculationService.calculationDecision(request);

    // Verify response was sent with error
    ArgumentCaptor<CalculationResponse> responseCaptor = ArgumentCaptor
        .forClass(CalculationResponse.class);
    verify(kafkaProducer).sendCalculationResponse(responseCaptor.capture());

    CalculationResponse capturedResponse = responseCaptor.getValue();
    assertEquals("123", capturedResponse.getRequestId());
    assertNull(capturedResponse.getResult());
    assertEquals("Division by zero", capturedResponse.getError());
  }
}
