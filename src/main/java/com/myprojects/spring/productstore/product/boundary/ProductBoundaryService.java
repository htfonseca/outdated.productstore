package com.myprojects.spring.productstore.product.boundary;

import com.myprojects.spring.productstore.common.exceptions.ResourceNotFoundException;
import com.myprojects.spring.productstore.product.dto.ProductDto;
import com.myprojects.spring.productstore.product.model.Product;
import com.myprojects.spring.productstore.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/** Logic class that deal with all the CRUD operations of the product */
@Service
public class ProductBoundaryService {

  /** Needed repositories for dealing with access to the persistence layers. */
  @Autowired ProductRepository productRepository;

  /**
   * Create operation that receives a {@link ProductDto}, creates a {@link Product} and calls the
   * save method in the repository.
   */
  @Transactional
  public Product createProduct(ProductDto productDto) {
    Product product = new Product(UUID.randomUUID(), productDto.getName(), productDto.getPrice());
    return productRepository.save(product);
  }

  /**
   * Read operation that receives a {@link UUID} identifier and try to retrieve the corresponding
   * {@link Product} from the repository. If no product can be found it will throw a {@link
   * ResourceNotFoundException} to the user can know that there is no product for the given
   * identifier.
   *
   * <p>This operations is annotated with the 'readOnly' at true, so that the hibernate knows that
   * no record will be change.
   */
  @Transactional(readOnly = true)
  public Product readProduct(UUID identifier) {

    Product product = productRepository.findByIdentifier(identifier);
    if (product == null) {
      throw new ResourceNotFoundException(
          "The product with the identifier \"" + identifier + "\" ] wasn't found.");
    }

    return product;
  }

  /**
   * Read operation that try to retrieve all the {@link Product} from the repository.
   *
   * <p>This operations is annotated with the 'readOnly' at true, so that the hibernate knows that
   * no record will be change.
   */
  @Transactional(readOnly = true)
  public List<Product> readProducts() {
    return productRepository.findAll();
  }

  /**
   * Read operation that receives a {@link Pageable} and try to retrieve a page of {@link Product}s
   * from the repository.
   *
   * <p>This operations is annotated with the 'readOnly' at true, so that the hibernate knows that
   * no record will be change.
   */
  @Transactional(readOnly = true)
  public Page<Product> readProducts(Pageable pageable) {
    return productRepository.findAll(pageable);
  }

  /**
   * Update operation that receives a {@link UUID} identifier and a {@link ProductDto}, try to
   * retrieve the corresponding {@link Product} from the repository, update it with the
   * corresponding properties and call the save method. If no product can be found it will throw a
   * {@link ResourceNotFoundException} to the user can know that there is no product for the given
   * identifier.
   */
  @Transactional
  public Product updateProduct(UUID identifier, ProductDto productDto) {

    Product product = productRepository.findByIdentifier(identifier);
    if (product == null) {
      throw new ResourceNotFoundException(
          "The product with the id \"" + identifier + "\" ] wasn't found.");
    }

    product.setName(productDto.getName());
    product.setPrice(productDto.getPrice());

    return productRepository.save(product);
  }
}
