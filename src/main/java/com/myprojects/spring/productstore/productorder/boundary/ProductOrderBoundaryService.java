package com.myprojects.spring.productstore.productorder.boundary;

import com.myprojects.spring.productstore.common.exceptions.ResourceNotFoundException;
import com.myprojects.spring.productstore.product.model.Product;
import com.myprojects.spring.productstore.product.repository.ProductRepository;
import com.myprojects.spring.productstore.productorder.dto.ProductOrderDto;
import com.myprojects.spring.productstore.productorder.model.Parcel;
import com.myprojects.spring.productstore.productorder.model.ProductOrder;
import com.myprojects.spring.productstore.productorder.repository.ProductOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/** Logic class that deal with all the CRUD operations of the product orders */
@Service
public class ProductOrderBoundaryService {

  @Autowired ProductOrderRepository productOrderRepository;

  @Autowired ProductRepository productRepository;

  @Transactional
  public ProductOrder createProductOrder(ProductOrderDto productOrderDto) {

    List<Parcel> parcelSet = new LinkedList<>();

    productOrderDto
        .getParcels()
        .stream()
        .forEach(
            parcelDto -> {
              UUID productIdentifier = parcelDto.getProductIdentifier();
              Product product = productRepository.findByIdentifier(productIdentifier);
              if (product == null) {
                throw new ResourceNotFoundException(
                    "The product with the identifier \""
                        + productIdentifier
                        + "\" ] wasn't found.");
              }

              parcelSet.add(new Parcel(product, parcelDto.getAmount(), product.getPrice()));
            });

    ProductOrder productOrder =
        new ProductOrder(UUID.randomUUID(), productOrderDto.getEmail(), LocalDate.now(), parcelSet);

    return productOrderRepository.saveAndFlush(productOrder);
  }

  @Transactional(readOnly = true)
  public ProductOrder getProductOrder(UUID identifier) {

    ProductOrder productOrder = productOrderRepository.findByIdentifier(identifier);
    if (productOrder == null) {
      throw new ResourceNotFoundException(
          "The product order with the identifier \"" + identifier + "\" ] wasn't found.");
    }

    return productOrder;
  }

  @Transactional(readOnly = true)
  public List<ProductOrder> getProductOrders(LocalDate start, LocalDate end) {

    if (end.isBefore(start)) {
      throw new ResourceNotFoundException("The start date can't be before the end date");
    }

    return productOrderRepository.findByOrderDateBetween(start, end);
  }
}
