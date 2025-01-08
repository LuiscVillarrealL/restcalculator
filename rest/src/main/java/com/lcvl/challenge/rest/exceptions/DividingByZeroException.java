package com.lcvl.challenge.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The Class DividingByZeroException.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Division by zero is not allowed")
public class DividingByZeroException extends Exception {

  private static final long serialVersionUID = 8612055283171484946L;

  /**
   * Instantiates a new dividing by zero exception.
   *
   * @param message the message
   */
  public DividingByZeroException(String message) {
    super(message);
  }

}
