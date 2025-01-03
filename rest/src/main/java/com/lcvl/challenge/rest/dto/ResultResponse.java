package com.lcvl.challenge.rest.dto;

import java.math.BigDecimal;

/**
 * The Record ResultResponse.
 *
 * @param requestId the request id
 * @param result the result
 */
public record ResultResponse(
    BigDecimal result) {

}
