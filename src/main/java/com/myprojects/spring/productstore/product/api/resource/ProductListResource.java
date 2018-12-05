package com.myprojects.spring.productstore.product.api.resource;

import org.springframework.hateoas.ResourceSupport;

import java.util.List;

/** Simple list resource for products */
public class ProductListResource extends ResourceSupport {

  private List<ProductResource> content;

  public ProductListResource(List<ProductResource> content) {
    this.content = content;
  }

  public List<ProductResource> getContent() {
    return content;
  }
}
