package com.myprojects.spring.productstore.product.api;

import com.myprojects.spring.productstore.product.api.factory.ProductResourceFactory;
import com.myprojects.spring.productstore.product.boundary.ProductBoundaryService;
import com.myprojects.spring.productstore.product.dto.ProductDto;
import com.myprojects.spring.productstore.product.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

/** Simple REST controller for the product */
@RestController
@RequestMapping(path = "/store/products")
public class ProductController {

  @Autowired ProductBoundaryService productBoundaryService;

  @PostMapping
  public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDto productDto) {

    Product product = productBoundaryService.createProduct(productDto);

    URI location =
        ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/store/products/{identifier}")
            .buildAndExpand(product.getIdentifier())
            .toUri();

    return ResponseEntity.created(location).body(ProductResourceFactory.build(product));
  }

  @GetMapping(path = "/{identifier}")
  public ResponseEntity<?> readProduct(@PathVariable("identifier") UUID identifier) {

    Product product = productBoundaryService.readProduct(identifier);

    return ResponseEntity.ok(ProductResourceFactory.build(product));
  }

  @GetMapping(path = "/page")
  public ResponseEntity<?> readProducts(
      @PageableDefault(
              sort = {"name"},
              direction = Sort.Direction.ASC,
              size = 20)
          Pageable pageable) {

    Page<Product> productPage = productBoundaryService.readProducts(pageable);

    return ResponseEntity.ok(ProductResourceFactory.build(productPage));
  }

  @GetMapping()
  public ResponseEntity<?> readProducts() {

    List<Product> productList = productBoundaryService.readProducts();

    return ResponseEntity.ok(ProductResourceFactory.build(productList));
  }

  @PostMapping(path = "/{identifier}")
  public ResponseEntity<?> updateProduct(
      @PathVariable("identifier") UUID identifier, @Valid @RequestBody ProductDto productDto) {

    Product product = productBoundaryService.updateProduct(identifier, productDto);

    return ResponseEntity.ok(ProductResourceFactory.build(product));
  }
}
