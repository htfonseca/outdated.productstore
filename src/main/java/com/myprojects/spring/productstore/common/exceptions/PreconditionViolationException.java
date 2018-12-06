package com.myprojects.spring.productstore.common.exceptions;

/** Exception to throw if precondition is not satisfied. */
public class PreconditionViolationException extends RuntimeException {

  private final String messageKey;

  /**
   * Constructor to initialize exception with a message key (can be used for translation).
   *
   * @param messageKey the message key
   */
  public PreconditionViolationException(String messageKey) {
    this.messageKey = messageKey;
  }

  /**
   * Constructor to initialize exception with a message key (can be used for translation) and error
   * cause.
   *
   * @param messageKey the message key
   * @param cause the cause
   */
  public PreconditionViolationException(String messageKey, Throwable cause) {
    super(cause);
    this.messageKey = messageKey;
  }

  public String getMessageKey() {
    return messageKey;
  }
}
