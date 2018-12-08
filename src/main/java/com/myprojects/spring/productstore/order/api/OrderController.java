package com.myprojects.spring.productstore.order.api;

import com.myprojects.spring.productstore.order.api.Factory.OrderResourceFactory;
import com.myprojects.spring.productstore.order.boundary.OrderBoundaryService;
import com.myprojects.spring.productstore.order.dto.OrderDto;
import com.myprojects.spring.productstore.order.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/** Simple REST controller for the orders */
@RestController
@RequestMapping(path = "/store/orders")
public class OrderController {

  @Autowired OrderBoundaryService orderBoundaryService;

  @Autowired OrderResourceFactory orderResourceFactory;

  @PostMapping
  public ResponseEntity<?> createOrder(@Valid @RequestBody OrderDto orderDto) {

    Order order = orderBoundaryService.createOrder(orderDto);

    URI location =
        ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/store/orders/{identifier}")
            .buildAndExpand(order.getIdentifier())
            .toUri();

    return ResponseEntity.created(location).body(OrderResourceFactory.build(order));
  }

  @GetMapping(path = "/{identifier}")
  public ResponseEntity<?> readOrder(@PathVariable("identifier") UUID identifier) {

    Order order = orderBoundaryService.getOrder(identifier);

    return ResponseEntity.ok(OrderResourceFactory.build(order));
  }

  @GetMapping
  public ResponseEntity<?> readOrders(
      @RequestParam(value = "start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
      @RequestParam(value = "end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

    List<Order> orders = orderBoundaryService.getOrders(start, end);

    return ResponseEntity.ok(OrderResourceFactory.build(orders));
  }
}
