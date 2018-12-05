package com.myprojects.spring.productstore.productorder.api.resource;

import org.springframework.hateoas.ResourceSupport;

import java.util.List;

/** Simple list resource for products orders */
public class ProductOrderListResource extends ResourceSupport {

  private List<ProductOrderResource> content;

  public ProductOrderListResource(List<ProductOrderResource> content) {
    this.content = content;
  }

  public List<ProductOrderResource> getContent() {
    return content;
  }
}
