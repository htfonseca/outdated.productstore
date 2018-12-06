package com.myprojects.spring.productstore.common.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/** Abstract class to be use in all entities with common properties */
@MappedSuperclass
public abstract class AbstractEntity {

  /** The database normal key identifier */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected Long id;

  /** An UUID identifier to be use in the entities */
  @NotNull
  @Column(nullable = false)
  protected UUID identifier;

  public UUID getIdentifier() {
    return identifier;
  }

  public void setIdentifier(UUID identifier) {
    this.identifier = identifier;
  }
}
