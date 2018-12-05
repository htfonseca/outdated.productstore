package com.myprojects.spring.productstore.productorder.repository;

import com.myprojects.spring.productstore.productorder.model.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/** Simple product order repository class */
@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {

  ProductOrder findByIdentifier(UUID identifier);

  List<ProductOrder> findByOrderDateBetween(LocalDate start, LocalDate end);
}
