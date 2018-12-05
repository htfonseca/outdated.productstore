package com.myprojects.spring.productstore.productorder.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/** Dto that represents a parts of the an productorder (parcel) to be used in the json parser */
public class ParcelDto {

  @NotNull private UUID productIdentifier;

  @NotNull
  @Min(1)
  private int amount;

  public ParcelDto() {}

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
