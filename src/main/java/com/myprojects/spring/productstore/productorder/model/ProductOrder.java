package com.myprojects.spring.productstore.productorder.model;

import com.myprojects.spring.productstore.common.model.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/** Entity class that represents an product order */
@Entity
@Table(indexes = @Index(name = "ProductOrder", columnList = "identifier", unique = true))
public class ProductOrder extends AbstractEntity {

  @NotNull
  @Email
  @Column(nullable = false)
  private String email;

  @NotNull
  @Column(nullable = false, name = "order_date")
  private LocalDate orderDate;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
      name = "productorder_parcels",
      foreignKey = @ForeignKey(name = "FK_ProductOrder_Parcel_ProductOrderId"),
      joinColumns = @JoinColumn(name = "parcel_id"))
  private @NotNull @Size(min = 1) List<Parcel> parcels;

  /** Constructor for JPA. */
  public ProductOrder() {
    // empty
  }

  public ProductOrder(UUID identifier, String email, LocalDate orderDate, List<Parcel> parcels) {
    this.identifier = identifier;
    this.email = email;
    this.orderDate = orderDate;
    this.parcels = parcels;
  }

  @Override
  public String toString() {
    return String.format(
        "ProductOrder[id=%d, email='%s', orderDate='%s']", id, email, orderDate.toString());
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public LocalDate getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(LocalDate orderDate) {
    this.orderDate = orderDate;
  }

  public List<Parcel> getParcels() {
    return parcels;
  }

  public void setParcels(List<Parcel> parcels) {
    this.parcels = parcels;
  }
}
