package com.myprojects.spring.productstore.common.api;

import com.myprojects.spring.productstore.common.exceptions.PreconditionViolationException;
import com.myprojects.spring.productstore.common.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/** Simple advice to catch and deal with throw exceptions. */
@ControllerAdvice
public class ErrorController {

  /**
   * Exception handler for the {@link ResourceNotFoundException}.
   *
   * @param exception The resource exception object
   * @return a {@link ResponseEntity} to be send with the http code not found
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<?> handleMethodArgumentNotValid(ResourceNotFoundException exception) {

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }

  /**
   * Exception handler for the {@link PreconditionViolationException}.
   *
   * @param exception The resource exception object
   * @return a {@link ResponseEntity} to be send with the http code bad request
   */
  @ExceptionHandler(PreconditionViolationException.class)
  public ResponseEntity<?> handlePreconditionValidationException(
      PreconditionViolationException exception) {

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }
}
