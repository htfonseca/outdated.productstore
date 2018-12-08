package com.myprojects.spring.productstore.order.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/** Dto that represents a parts of the an order (parcel) to be used in the json parser */
public class ParcelDto {

  @NotNull private UUID productIdentifier;

  @NotNull
  @Min(1)
  private int amount;

  /** Constructor for JPA. */
  public ParcelDto() {
    // empty
  }

  public ParcelDto(UUID productIdentifier, int amount) {
    this.productIdentifier = productIdentifier;
    this.amount = amount;
  }

  public UUID getProductIdentifier() {
    return productIdentifier;
  }

  public int getAmount() {
    return amount;
  }
}
