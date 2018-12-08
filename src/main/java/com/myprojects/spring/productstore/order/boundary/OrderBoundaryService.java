package com.myprojects.spring.productstore.order.boundary;

import com.myprojects.spring.productstore.common.exceptions.PreconditionViolationException;
import com.myprojects.spring.productstore.common.exceptions.ResourceNotFoundException;
import com.myprojects.spring.productstore.order.model.Order;
import com.myprojects.spring.productstore.order.model.Parcel;
import com.myprojects.spring.productstore.order.repository.OrderRepository;
import com.myprojects.spring.productstore.product.model.Product;
import com.myprojects.spring.productstore.product.repository.ProductRepository;
import com.myprojects.spring.productstore.order.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/** Logic class that deal with all the CRUD operations of the product orders */
@Service
public class OrderBoundaryService {

  @Autowired
  OrderRepository orderRepository;

  @Autowired ProductRepository productRepository;

  @Transactional
  public Order createOrder(OrderDto orderDto) {

    /**
     * Runs by the list of parcelDtos, fetch the corresponding product and create a new entity
     * parcel. Could be improve by using batch fetch with all the identifiers of the products.
     */
    List<Parcel> parcels =
        orderDto
            .getParcels()
            .stream()
            .map(
                parcelDto -> {
                  UUID productIdentifier = parcelDto.getProductIdentifier();
                  Product product = productRepository.findByIdentifier(productIdentifier);
                  if (product == null) {
                    throw new ResourceNotFoundException(
                        "The product with the identifier \""
                            + productIdentifier
                            + "\" wasn't found.");
                  }

                  return new Parcel(product, parcelDto.getAmount(), product.getPrice());
                })
            .collect(Collectors.toList());

    Order order = new Order(UUID.randomUUID(), orderDto.getEmail(), LocalDate.now(), parcels);

    return orderRepository.save(order);
  }

  @Transactional(readOnly = true)
  public Order getOrder(UUID identifier) {

    Order order = orderRepository.findByIdentifier(identifier);
    if (order == null) {
      throw new ResourceNotFoundException(
          "The product order with the identifier \"" + identifier + "\" ] wasn't found.");
    }

    return order;
  }

  @Transactional(readOnly = true)
  public List<Order> getOrders(LocalDate start, LocalDate end) {

    if (end.isBefore(start)) {
      throw new PreconditionViolationException("The start date can't be before the end date");
    }

    return orderRepository.findByOrderDateBetween(start, end);
  }
}
