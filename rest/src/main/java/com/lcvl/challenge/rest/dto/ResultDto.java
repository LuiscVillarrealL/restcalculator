package com.lcvl.challenge.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The Record ResultDto.
 *
 * @param results the results
 */
@Schema(description = "Represents a result of the operation request")
public record ResultDto(
    
    @Schema(name = "result", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    String result) {

}
