package com.myprojects.spring.productstore.product.repository;

import com.myprojects.spring.productstore.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/** Simple product repository class */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  Product findByIdentifier(UUID identifier);
}
