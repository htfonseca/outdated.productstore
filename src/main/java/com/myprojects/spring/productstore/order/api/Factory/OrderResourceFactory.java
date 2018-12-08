package com.myprojects.spring.productstore.order.api.Factory;

import com.myprojects.spring.productstore.order.api.OrderController;
import com.myprojects.spring.productstore.order.api.resource.OrderResource;
import com.myprojects.spring.productstore.order.api.resource.ParcelResource;
import com.myprojects.spring.productstore.order.api.resource.OrderListResource;
import com.myprojects.spring.productstore.order.model.Order;
import com.myprojects.spring.productstore.order.model.Parcel;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/** A simple factory that converts product entity to product resources to send to the user. */
@Component
public class OrderResourceFactory {

  public static OrderResource build(Order productOrder) {

    List<ParcelResource> parcelResources = buildParcels(productOrder.getParcels());

    BigDecimal productOrderTotal =
        parcelResources
            .stream()
            .map(ParcelResource::getTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    OrderResource productOrderResource =
        new OrderResource(
            productOrder.getIdentifier(),
            productOrder.getEmail(),
            productOrder.getOrderDate(),
            productOrderTotal,
            parcelResources);

    productOrderResource.add(
        ControllerLinkBuilder.linkTo(methodOn(OrderController.class).readOrder(productOrder.getIdentifier()))
            .withSelfRel());

    return productOrderResource;
  }

  public static OrderListResource build(List<Order> productOrderList) {

    List<OrderResource> productOrderResources =
        productOrderList.stream().map(OrderResourceFactory::build).collect(Collectors.toList());

    OrderListResource productOrderListResource = new OrderListResource(productOrderResources);

    productOrderListResource.add(
        linkTo(methodOn(OrderController.class).readOrders(null, null)).withSelfRel());

    return productOrderListResource;
  }

  /** Simple method to calculate the parcerl resources with the correct total amount */
  private static List<ParcelResource> buildParcels(List<Parcel> parcels) {

    List<ParcelResource> parcelResources = new LinkedList<>();

    parcels.forEach(
        parcel -> {
          ParcelResource parcelResource =
              new ParcelResource(
                  parcel.getProduct().getName(),
                  parcel.getPrice(),
                  parcel.getAmount(),
                  parcel.getPrice().multiply(new BigDecimal(parcel.getAmount())));

          parcelResources.add(parcelResource);
        });

    return parcelResources;
  }
}
