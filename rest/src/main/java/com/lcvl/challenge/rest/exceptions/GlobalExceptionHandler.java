package com.lcvl.challenge.rest.exceptions;

import java.math.BigDecimal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.lcvl.challenge.rest.dto.ResultDto;
import jakarta.validation.ConstraintViolationException;

/**
 * The Class GlobalExceptionHandler.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handle type mismatch.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ResultDto> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    String message;

    // Check if the target type is BigDecimal
    if (ex.getRequiredType() != null && ex.getRequiredType().equals(BigDecimal.class)) {
      message = "Invalid number format for parameter";
    } else {
      // Default handling for other mismatched arguments
      message = "Invalid number format for parameter";
    }

    ResultDto resultDto = new ResultDto(message);
    return ResponseEntity.badRequest().body(resultDto);
  }

  /**
   * Handle dividing by zero exception.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(DividingByZeroException.class)
  public ResponseEntity<ResultDto> handleDividingByZeroException(DividingByZeroException ex) {
    ResultDto errorResponse = new ResultDto("Cannot divide by zero");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  /**
   * Handle generic exception.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResultDto> handleGenericException(Exception ex) {
    ResultDto errorResponse = new ResultDto("An error occurred: " + ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  /**
   * Handle missing param.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ResultDto> handleMissingParam(MissingServletRequestParameterException ex) {

    ResultDto errorResponse = new ResultDto(
        "Missing required parameter '" + ex.getParameterName() + "'");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

  }

  /**
   * Handle constraint violation.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ResultDto> handleConstraintViolation(ConstraintViolationException ex) {

    ResultDto errorResponse = new ResultDto("Validation error: " + ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

  }
}
