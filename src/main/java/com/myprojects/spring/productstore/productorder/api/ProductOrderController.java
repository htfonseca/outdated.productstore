package com.myprojects.spring.productstore.productorder.api;

import com.myprojects.spring.productstore.productorder.api.Factory.ProductOrderResourceFactory;
import com.myprojects.spring.productstore.productorder.boundary.ProductOrderBoundaryService;
import com.myprojects.spring.productstore.productorder.dto.ProductOrderDto;
import com.myprojects.spring.productstore.productorder.model.ProductOrder;
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

/** Simple REST controller for the product orders */
@RestController
@RequestMapping(path = "/store/productorders")
public class ProductOrderController {

  @Autowired ProductOrderBoundaryService productOrderBoundaryService;

  @Autowired ProductOrderResourceFactory productOrderResourceFactory;

  @PostMapping
  public ResponseEntity<?> createProductOrder(@Valid @RequestBody ProductOrderDto productOrderDto) {

    ProductOrder productOrder = productOrderBoundaryService.createProductOrder(productOrderDto);

    URI location =
        ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/store/productorders/{identifier}")
            .buildAndExpand(productOrder.getIdentifier())
            .toUri();

    return ResponseEntity.created(location).body(ProductOrderResourceFactory.build(productOrder));
  }

  @GetMapping(path = "/{identifier}")
  public ResponseEntity<?> readProductOrder(@PathVariable("identifier") UUID identifier) {

    ProductOrder productOrder = productOrderBoundaryService.getProductOrder(identifier);

    return ResponseEntity.ok(ProductOrderResourceFactory.build(productOrder));
  }

  @GetMapping
  public ResponseEntity<?> readProductOrders(
      @RequestParam(value = "start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
      @RequestParam(value = "end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

    List<ProductOrder> productOrderList = productOrderBoundaryService.getProductOrders(start, end);

    return ResponseEntity.ok(ProductOrderResourceFactory.build(productOrderList));
  }
}
