package com.myprojects.spring.productstore.product.api.resource;

import org.springframework.hateoas.ResourceSupport;

import java.math.BigDecimal;
import java.util.UUID;

/** Simple resource for product with the sending properties and links */
public class ProductResource extends ResourceSupport {

  public static final String LINK_UPDATE = "update";

  private UUID identifier;

  private String name;

  private BigDecimal price;

  public ProductResource(UUID identifier, String name, BigDecimal price) {
    this.identifier = identifier;
    this.name = name;
    this.price = price;
  }

  public UUID getIdentifier() {
    return identifier;
  }

  public String getName() {
    return name;
  }

  public BigDecimal getPrice() {
    return price;
  }
}
