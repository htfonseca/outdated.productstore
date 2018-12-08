package com.myprojects.spring.productstore.order.api.resource;

import org.springframework.hateoas.ResourceSupport;

import java.util.List;

/** Simple list resource for orders */
public class OrderListResource extends ResourceSupport {

  private List<OrderResource> content;

  public OrderListResource(List<OrderResource> content) {
    this.content = content;
  }

  public List<OrderResource> getContent() {
    return content;
  }
}
