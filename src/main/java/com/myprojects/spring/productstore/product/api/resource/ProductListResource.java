package com.myprojects.spring.productstore.product.api.resource;

import com.myprojects.spring.productstore.product.model.Product;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

/**
 * Simple representation of a list of {@link Product} resources to support the parse for json and
 * the corresponding string HATOES links (if needed).
 */
public class ProductListResource extends ResourceSupport {

  private List<ProductResource> content;

  public ProductListResource(List<ProductResource> content) {
    this.content = content;
  }

  public List<ProductResource> getContent() {
    return content;
  }
}
