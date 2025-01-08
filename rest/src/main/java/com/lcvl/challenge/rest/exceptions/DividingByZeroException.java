package com.lcvl.challenge.rest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The Class NotANumberException.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Not a valid number")
public class DividingByZeroException extends NumberFormatException {

  private static final long serialVersionUID = 8612055283171484946L;

  /**
   * Instantiates a new not A number exception.
   *
   * @param message the message
   */
  public DividingByZeroException(String message) {
    super(message);
  }

}
