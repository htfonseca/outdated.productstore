package com.myprojects.spring.productstore.common.exceptions;

/** Exception to throw if resource cannot be found. */
public class ResourceNotFoundException extends RuntimeException {

  private final String messageKey;

  /**
   * Constructor to initialize exception with a message key (can be used for translation).
   *
   * @param messageKey the message key
   */
  public ResourceNotFoundException(String messageKey) {
    super(messageKey);
    this.messageKey = messageKey;
  }

  /**
   * Constructor to initialize exception with a message key (can be used for translation) and error
   * cause.
   *
   * @param messageKey the message key
   * @param cause the cause
   */
  public ResourceNotFoundException(String messageKey, Throwable cause) {
    super(messageKey, cause);
    this.messageKey = messageKey;
  }

  public String getMessageKey() {
    return messageKey;
  }
}
