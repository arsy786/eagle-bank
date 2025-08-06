package dev.arsalaan.eagle_bank.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;

/*
This class handles custom (or existing) exceptions.
It dictates how the exceptions are presented to the REST API client.
Spring Boot handles exceptions by default but this class allows custom handling
to fit our own requirements.

@ControllerAdvice - It allows you to handle exceptions across the whole application,
not just to an individual controller.
 */

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  /*
   * Provides handling for exceptions throughout the app.
   * Created to encapsulate errors with more detail and essentially replace
   * javax.persistence.EntityNotFoundException
   * ApiRequestException ~= EntityNotFoundException
   */
  @ExceptionHandler(value = { ApiRequestException.class })
  public ResponseEntity<Object> handleApiRequestException(ApiRequestException ex) {

    ApiException apiException = new ApiException(
        ex.getMessage(),
        ex.getHttpStatus(),
        ZonedDateTime.now(ZoneId.of("Z")));

    log.error("An exception error has occurred with message: {}", ex.getMessage());

    return new ResponseEntity<>(apiException, ex.getHttpStatus());
  }

  /*
   * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid
   * validation.
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {

    String errorMessages = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .collect(Collectors.joining(", "));

    ApiException apiException = new ApiException(
        errorMessages,
        HttpStatus.BAD_REQUEST,
        ZonedDateTime.now(ZoneId.of("Z")));

    log.error("Validation error has occurred with message: {}", ex.getBindingResult().getFieldErrors());

    return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
  }

  /*
   * Handle generic RuntimeException
   */
  @ExceptionHandler(value = { RuntimeException.class })
  public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {

    ApiException apiException = new ApiException(
        "Internal server error: " + ex.getMessage(),
        HttpStatus.INTERNAL_SERVER_ERROR,
        ZonedDateTime.now(ZoneId.of("Z")));

    log.error("Runtime exception occurred: {}", ex.getMessage(), ex);

    return new ResponseEntity<>(apiException, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /*
   * Handle JWT Expired Exception
   */
  @ExceptionHandler(ExpiredJwtException.class)
  public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex) {

    ApiException apiException = new ApiException(
        "JWT token has expired",
        HttpStatus.UNAUTHORIZED,
        ZonedDateTime.now(ZoneId.of("Z")));

    log.error("Expired JWT token error: {}", ex.getMessage());

    return new ResponseEntity<>(apiException, HttpStatus.UNAUTHORIZED);
  }

  /*
   * Handle JWT Signature Exception
   */
  @ExceptionHandler(SignatureException.class)
  public ResponseEntity<Object> handleSignatureException(SignatureException ex) {

    ApiException apiException = new ApiException(
        "Invalid JWT signature",
        HttpStatus.FORBIDDEN,
        ZonedDateTime.now(ZoneId.of("Z")));
    log.error("Invalid JWT signature error: {}", ex.getMessage());

    return new ResponseEntity<>(apiException, HttpStatus.FORBIDDEN);
  }

  /*
   * Handle JWT Malformed Exception
   */
  @ExceptionHandler(MalformedJwtException.class)
  public ResponseEntity<Object> handleMalformedJwtException(MalformedJwtException ex) {

    ApiException apiException = new ApiException(
        "Malformed JWT token",
        HttpStatus.BAD_REQUEST,
        ZonedDateTime.now(ZoneId.of("Z")));

    log.error("Malformed JWT token error: {}", ex.getMessage());

    return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
  }

  /*
   * Handle JWT Unsupported Exception
   */
  @ExceptionHandler(UnsupportedJwtException.class)
  public ResponseEntity<Object> handleUnsupportedJwtException(UnsupportedJwtException ex) {

    ApiException apiException = new ApiException(
        "Unsupported JWT token",
        HttpStatus.BAD_REQUEST,
        ZonedDateTime.now(ZoneId.of("Z")));
    log.error("Unsupported JWT token error: {}", ex.getMessage());

    return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
  }

  // other exception handlers can be added here
  // (e.g. handleEntityNotFoundException, handleHttpMessageNotReadable,
  // handleUserNotFoundException, handleContentNotAllowedException)

}