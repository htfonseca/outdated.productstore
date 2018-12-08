package com.myprojects.spring.productstore.order.repository;

import com.myprojects.spring.productstore.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/** Order repository class */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  Order findByIdentifier(UUID identifier);

  List<Order> findByOrderDateBetween(LocalDate start, LocalDate end);
}
