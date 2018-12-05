package com.myprojects.spring.productstore.product.boundary;

import com.myprojects.spring.productstore.common.exceptions.ResourceNotFoundException;
import com.myprojects.spring.productstore.product.dto.ProductDto;
import com.myprojects.spring.productstore.product.model.Product;
import com.myprojects.spring.productstore.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/** Logic class that deal with all the CRUD operations of the product */
@Service
public class ProductBoundaryService {

  @Autowired ProductRepository productRepository;

  @Transactional
  public Product createProduct(ProductDto productDto) {
    Product product = new Product(UUID.randomUUID(), productDto.getName(), productDto.getPrice());
    return productRepository.save(product);
  }

  @Transactional(readOnly = true)
  public Product readProduct(UUID identifier) {

    Product product = productRepository.findByIdentifier(identifier);
    if (product == null) {
      throw new ResourceNotFoundException(
          "The product with the identifier \"" + identifier + "\" ] wasn't found.");
    }

    return product;
  }

  @Transactional(readOnly = true)
  public Page<Product> readProducts(Pageable pageable) {
    return productRepository.findAll(pageable);
  }

  @Transactional(readOnly = true)
  public List<Product> readProducts() {
    return productRepository.findAll();
  }

  @Transactional
  public Product updateProduct(UUID identifier, ProductDto productDto) {

    Product product = productRepository.findByIdentifier(identifier);
    if (product == null) {
      throw new ResourceNotFoundException(
          "The product with the id \"" + identifier + "\" ] wasn't found.");
    }

    product.setName(productDto.getName());
    product.setPrice(productDto.getPrice());

    return productRepository.save(product);
  }
}
