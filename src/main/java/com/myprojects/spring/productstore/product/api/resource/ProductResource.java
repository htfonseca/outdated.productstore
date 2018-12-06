package com.myprojects.spring.productstore.product.api.resource;

import com.myprojects.spring.productstore.product.model.Product;
import org.springframework.hateoas.ResourceSupport;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Simple representation of a {@link Product} resource to support the parse for json and the
 * corresponding string HATOES links (if needed).
 *
 * <p>I can be created with a normal constructor or with support of the entity for fast creation.
 * The second options forces this class to have knowlage of the entity that it will represent.
 */
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

  public ProductResource(Product product) {
    this.identifier = product.getIdentifier();
    this.name = product.getName();
    this.price = product.getPrice();
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
