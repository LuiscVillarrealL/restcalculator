package com.lcvl.challenge.common.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Class CalculationResponse.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalculationResponse {
  
  private String requestId;
  private BigDecimal result;
  private String error;

}
