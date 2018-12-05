package com.myprojects.spring.productstore.common.api;

import com.myprojects.spring.productstore.common.exceptions.PreconditionViolationException;
import com.myprojects.spring.productstore.common.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/** Simple advice to catch and deal with a resource not found exception */
@ControllerAdvice
public class ErrorController {

  /**
   * Exception handler for the ResourceNotFoundException.class
   *
   * @param ex The resource exception
   * @return a response to be send to the request
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<?> handleMethodArgumentNotValid(ResourceNotFoundException ex) {

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  /**
   * Exception handler for the PreconditionViolationException.class
   *
   * @param ex The resource exception
   * @return a response to be send to the request
   */
  @ExceptionHandler(PreconditionViolationException.class)
  public ResponseEntity<?> handlePreconditionValidationException(
      PreconditionViolationException ex) {

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }
}
