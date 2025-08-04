package dev.arsalaan.eagle_bank.exception;

import org.springframework.http.HttpStatus;
import lombok.Data;
import java.time.ZonedDateTime;

/*
 * Class for representing API errors.
 * This holds relevant information about errors that occur during invalid REST calls.
 */
@Data
public class ApiException {

  private final String message;
  private final HttpStatus httpStatus;
  private final ZonedDateTime timestamp;

}
