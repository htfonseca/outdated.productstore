package com.myprojects.spring.productstore.product.api.factory;

import com.myprojects.spring.productstore.product.api.ProductController;
import com.myprojects.spring.productstore.product.api.resource.ProductListResource;
import com.myprojects.spring.productstore.product.api.resource.ProductPageResource;
import com.myprojects.spring.productstore.product.api.resource.ProductResource;
import com.myprojects.spring.productstore.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/** A simple factory that converts product entity to product resources to send to the user. */
@Component
public class ProductResourceFactory {

  public static ProductResource build(Product product) {

    ProductResource resource =
        new ProductResource(product.getIdentifier(), product.getName(), product.getPrice());

    resource.add(
        linkTo(methodOn(ProductController.class).readProduct(product.getIdentifier()))
            .withSelfRel());

    resource.add(
        linkTo(methodOn(ProductController.class).updateProduct(product.getIdentifier(), null))
            .withRel(ProductResource.LINK_UPDATE));

    return resource;
  }

  public static ProductListResource build(List<Product> productList) {

    List<ProductResource> productResourceList =
        productList.stream().map(ProductResourceFactory::build).collect(Collectors.toList());

    ProductListResource productListResource = new ProductListResource(productResourceList);

    productListResource.add(linkTo(methodOn(ProductController.class).readProducts()).withSelfRel());

    return productListResource;
  }

  public static ProductPageResource build(Page<Product> productPage) {

    List<ProductResource> productResourceList =
        productPage
            .getContent()
            .stream()
            .map(ProductResourceFactory::build)
            .collect(Collectors.toList());

    ProductPageResource productPageResource =
        new ProductPageResource(
            productResourceList,
            productPage.getSize(),
            productPage.getNumber(),
            productPage.getTotalPages(),
            productPage.getNumberOfElements());

    productPageResource.add(
        linkTo(methodOn(ProductController.class).readProducts(null)).withSelfRel());

    return productPageResource;
  }
}
