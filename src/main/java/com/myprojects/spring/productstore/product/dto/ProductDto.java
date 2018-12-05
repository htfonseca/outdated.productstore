package com.myprojects.spring.productstore.product.dto;

import com.myprojects.spring.productstore.product.model.Product;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/** Dto that represents a parts of the product to be used in the json parser */
public class ProductDto {

  @NotNull
  @Size(min = 1, max = Product.MAX_NAME_LENGTH)
  private String name;

  @NotNull
  @DecimalMin(Product.MIN_PRICE)
  private BigDecimal price;

  public ProductDto() {}

  public ProductDto(String name, BigDecimal price) {
    this.name = name;
    this.price = price;
  }

  public String getName() {
    return name;
  }

  public BigDecimal getPrice() {
    return price;
  }
}
