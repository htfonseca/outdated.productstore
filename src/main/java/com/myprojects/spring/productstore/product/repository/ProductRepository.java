package com.myprojects.spring.productstore.product.repository;

import com.myprojects.spring.productstore.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Very simple {@link JpaRepository} with only a method to find a {@link Product} by a {@link UUID}.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  Product findByIdentifier(UUID identifier);
}
