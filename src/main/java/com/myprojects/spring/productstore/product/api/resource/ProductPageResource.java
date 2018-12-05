package com.myprojects.spring.productstore.product.api.resource;

import org.springframework.hateoas.ResourceSupport;

import java.util.List;

/** Simple page resource for products */
public class ProductPageResource extends ResourceSupport {

  private List<ProductResource> content;

  private int size;

  private int number;

  private int totalPages;

  private int totalElements;

  public ProductPageResource(
      List<ProductResource> content, int size, int number, int totalPages, int totalElements) {
    this.content = content;
    this.size = size;
    this.number = number;
    this.totalPages = totalPages;
    this.totalElements = totalElements;
  }

  public List<ProductResource> getContent() {
    return content;
  }

  public int getSize() {
    return size;
  }

  public int getNumber() {
    return number;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public int getTotalElements() {
    return totalElements;
  }
}
