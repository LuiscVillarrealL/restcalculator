package com.lcvl.challenge.calculator.dto;

import java.math.BigDecimal;
import com.lcvl.challenge.calculator.util.OperationEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Class CalculationRequest.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalculationRequest {
  
  private String requestId;
  private OperationEnum operation;
  private BigDecimal num1;
  private BigDecimal num2;

}
