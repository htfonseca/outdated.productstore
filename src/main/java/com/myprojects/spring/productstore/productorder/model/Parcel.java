package com.myprojects.spring.productstore.productorder.model;

import com.myprojects.spring.productstore.product.model.Product;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/** Embeddable class that represents a parcel */
@Embeddable
public class Parcel implements Serializable {

  public static final String MIN_PRICE = "0";

  @NotNull
  @JoinColumn(foreignKey = @ForeignKey(name = "FK_Order_Product"))
  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  private Product product;

  @NotNull
  @Min(1)
  @Column(nullable = false)
  private int amount;

  @NotNull
  @DecimalMin(MIN_PRICE)
  @Column(nullable = false, scale = 2)
  private BigDecimal price;

  /** Constructor for JPA. */
  public Parcel() {
    // empty
  }

  public Parcel(Product product, int amount, BigDecimal price) {
    this.product = product;
    this.amount = amount;
    this.price = price;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }
}
