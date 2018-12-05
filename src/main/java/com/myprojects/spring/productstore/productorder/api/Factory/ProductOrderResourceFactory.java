package com.myprojects.spring.productstore.productorder.api.Factory;

import com.myprojects.spring.productstore.productorder.api.ProductOrderController;
import com.myprojects.spring.productstore.productorder.api.resource.ParcelResource;
import com.myprojects.spring.productstore.productorder.api.resource.ProductOrderListResource;
import com.myprojects.spring.productstore.productorder.api.resource.ProductOrderResource;
import com.myprojects.spring.productstore.productorder.model.Parcel;
import com.myprojects.spring.productstore.productorder.model.ProductOrder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/** A simple factory that converts product entity to product resources to send to the user. */
@Component
public class ProductOrderResourceFactory {

  public static ProductOrderResource build(ProductOrder productOrder) {

    List<ParcelResource> parcelResources = buildParcels(productOrder.getParcels());

    BigDecimal productOrderTotal =
        parcelResources
            .stream()
            .map(ParcelResource::getTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    ProductOrderResource productOrderResource =
        new ProductOrderResource(
            productOrder.getIdentifier(),
            productOrder.getEmail(),
            productOrder.getOrderDate(),
            productOrderTotal,
            parcelResources);

    productOrderResource.add(
        linkTo(
                methodOn(ProductOrderController.class)
                    .readProductOrder(productOrder.getIdentifier()))
            .withSelfRel());

    return productOrderResource;
  }

  public static ProductOrderListResource build(List<ProductOrder> productOrderList) {

    List<ProductOrderResource> productOrderResources =
        productOrderList
            .stream()
            .map(ProductOrderResourceFactory::build)
            .collect(Collectors.toList());

    ProductOrderListResource productOrderListResource =
        new ProductOrderListResource(productOrderResources);

    productOrderListResource.add(
        linkTo(methodOn(ProductOrderController.class).readProductOrders(null, null)).withSelfRel());

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
