package com.myprojects.spring.productstore.product.model;

import com.myprojects.spring.productstore.common.model.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Simple entity of a instance of a {@link Product}. It contains all the corresponding annotation
 * for the hibernate to generate the database tables.
 *
 * <p>Also creates a index from the identifier {@link UUID} that will be used to retrieve the
 * products. This allow a detachment of the id key value of the database and simplify the data
 * migrations.
 */
@Entity
@Table(indexes = @Index(name = "UK_Product_Identifier", columnList = "identifier", unique = true))
public class Product extends AbstractEntity {

  public static final int MAX_NAME_LENGTH = 100;

  public static final String MIN_PRICE = "0";

  @NotNull
  @Size(min = 1, max = MAX_NAME_LENGTH)
  @Column(nullable = false, length = MAX_NAME_LENGTH)
  private String name;

  @NotNull
  @DecimalMin(MIN_PRICE)
  @Column(nullable = false, scale = 2)
  private BigDecimal price;

  /** Constructor for JPA. */
  public Product() {
    // empty
  }

  public Product(UUID identifier, String name, BigDecimal price) {
    this.identifier = identifier;
    this.name = name;
    this.price = price;
  }

  @Override
  public String toString() {
    return String.format("Product[id=%d, name='%s', price='%s']", id, name, price);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }
}
