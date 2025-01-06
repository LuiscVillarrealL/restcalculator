package com.lcvl.challenge.common.dto;

import java.math.BigDecimal;
import com.lcvl.challenge.common.util.OperationEnum;
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
  
  //TODO pas to commons
  
  private String requestId;
  private OperationEnum operation;
  private BigDecimal num1;
  private BigDecimal num2;

}
