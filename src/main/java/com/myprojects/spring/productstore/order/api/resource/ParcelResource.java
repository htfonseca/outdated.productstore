package com.myprojects.spring.productstore.order.api.resource;

import org.springframework.hateoas.ResourceSupport;

import java.math.BigDecimal;

/** Simple resource for order parcel with the sending properties and links */
public class ParcelResource extends ResourceSupport {

  private String product;

  private BigDecimal price;

  private int amount;

  private BigDecimal total;

  public ParcelResource(String product, BigDecimal price, int amount, BigDecimal total) {
    this.product = product;
    this.price = price;
    this.amount = amount;
    this.total = total;
  }

  public String getProduct() {
    return product;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public int getAmount() {
    return amount;
  }

  public BigDecimal getTotal() {
    return total;
  }
}
