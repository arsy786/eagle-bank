package dev.arsalaan.eagle_bank.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/*
 * This class is the Custom exception that is thrown throughout the API.
 * E.g: throw new ApiRequestException (instead of standard IllegalStateException)
 */
@Getter
public class ApiRequestException extends RuntimeException {

  private final HttpStatus httpStatus;

  public ApiRequestException(String message) {
    super(message);
    this.httpStatus = HttpStatus.BAD_REQUEST; // default
  }

  public ApiRequestException(HttpStatus httpStatus, String message) {
    super(message);
    this.httpStatus = httpStatus;
  }
}