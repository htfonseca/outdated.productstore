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

/**
 * A example of a factory that will convert the entities to the corresponding resources.
 *
 * <p>This strategy allow the flexibility for constructing the resources with the corresponding
 * needs of the API and adding the needed links to support HATOES.
 */
@Component
public class ProductResourceFactory {

  /**
   * Build a simple {@link ProductResource} from a {@link Product} and add it with the self link to
   * retrieve the resource and the update link to update the corresponding resource.
   *
   * @param product a entity {@link Product}
   * @return a resource {@link ProductResource}
   */
  public static ProductResource build(Product product) {

    ProductResource resource = new ProductResource(product);

    resource.add(
        linkTo(methodOn(ProductController.class).readProduct(product.getIdentifier()))
            .withSelfRel());

    resource.add(
        linkTo(methodOn(ProductController.class).updateProduct(product.getIdentifier(), null))
            .withRel(ProductResource.LINK_UPDATE));

    return resource;
  }

  /**
   * Build a simple {@link ProductListResource} from a list of {@link Product}s and add it with the
   * self link for future calls to the API.
   *
   * @param productList a list of entities {@link Product}
   * @return a resource {@link ProductListResource}
   */
  public static ProductListResource build(List<Product> productList) {

    List<ProductResource> productResourceList =
        productList.stream().map(ProductResourceFactory::build).collect(Collectors.toList());

    ProductListResource productListResource = new ProductListResource(productResourceList);

    productListResource.add(linkTo(methodOn(ProductController.class).readProducts()).withSelfRel());

    return productListResource;
  }

  /**
   * Build a simple {@link ProductPageResource} based on a retrieved {@link Page} of {@link
   * Product}s and add it with the self link for future calls to the API.
   *
   * @param productPage a page of entities {@link Product}
   * @return a resource {@link ProductPageResource}
   */
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
