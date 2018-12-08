package com.myprojects.spring.productstore.product.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myprojects.spring.productstore.product.api.resource.ProductResource;
import com.myprojects.spring.productstore.product.dto.ProductDto;
import com.myprojects.spring.productstore.product.model.Product;
import com.myprojects.spring.productstore.product.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.hypermedia.LinksSnippet;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * End to end tests to verify and document (snippet creation) of the {@link ProductController} API
 */
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest
@DisplayName("Test that Product API")
class ProductControllerEnd2EndApiTest {

  /** Documentation snippets that represents the send and receive data. */
  private final RequestFieldsSnippet productDtoRequestSnippet =
      requestFields(
          fieldWithPath("name").description("Product name."),
          fieldWithPath("price").description("Product price."));

  private final ResponseFieldsSnippet productResponseSnippet =
      responseFields(
          fieldWithPath("identifier").description("Product identifier"),
          fieldWithPath("name").description("Product name"),
          fieldWithPath("price").description("Product price"),
          subsectionWithPath("_links").ignored());

  private final ResponseFieldsSnippet productListResponseSnippet =
      responseFields(
          fieldWithPath("content").description("A list of products"),
          fieldWithPath("content[].identifier").description("Product identifier"),
          fieldWithPath("content[].name").description("Product name"),
          fieldWithPath("content[].price").description("Product price"),
          subsectionWithPath("content[]._links").ignored(),
          subsectionWithPath("_links").ignored());

  private final ResponseFieldsSnippet productPageResponseSnippet =
      responseFields(
          fieldWithPath("content").description("A list of products"),
          fieldWithPath("size").description("The size of one page"),
          fieldWithPath("number").description("The number of the page"),
          fieldWithPath("totalPages").description("The total number of pages"),
          fieldWithPath("totalElements").description("The total number of products"),
          fieldWithPath("content[]identifier").description("Product identifier"),
          fieldWithPath("content[].name").description("Product name"),
          fieldWithPath("content[].price").description("Product price"),
          subsectionWithPath("content[]._links").ignored(),
          subsectionWithPath("_links").ignored());

  private final LinksSnippet simpleLinksSnippet =
      links(linkWithRel(Link.REL_SELF).description("Link to resource itself"));

  private final LinksSnippet productLinks =
      links(
          linkWithRel(Link.REL_SELF).description("Link to the product resource itself"),
          linkWithRel(ProductResource.LINK_UPDATE).description("Link to the product update"));

  @Autowired private ProductRepository productRepository;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private MockMvc mockMvc;

  /** Setup to create the products that will be used on the tests. */
  @BeforeEach
  void setUp() {
    List<Product> products = new LinkedList<>();
    products.add(new Product(UUID.randomUUID(), "Product1", new BigDecimal("1.11")));
    products.add(new Product(UUID.randomUUID(), "Product2", new BigDecimal("2.22")));
    products.add(new Product(UUID.randomUUID(), "Product3", new BigDecimal("3.33")));
    products.forEach(productRepository::saveAndFlush);
  }

  /** Cleaning of the persistence layer in the end of the tests. */
  @AfterEach
  void tearDown() {
    productRepository.deleteAll();
  }

  /**
   * Test the creation of a product.
   *
   * @throws Exception is not expected
   */
  @Test
  @DisplayName("can created a product successfully")
  void verifyAndDocumentCreateProductSuccessfully() throws Exception {

    ProductDto productDto = new ProductDto("MyTestProductName", new BigDecimal("4.44"));

    mockMvc
        .perform(
            post("/store/products")
                .locale(Locale.ENGLISH)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(productDto))
                .accept(MediaTypes.HAL_JSON_UTF8_VALUE))
        .andExpect(status().isCreated())
        .andExpect(
            header()
                .string(HttpHeaders.LOCATION, startsWith("http://localhost:8080/store/products/")))
        .andExpect(jsonPath("$.identifier").exists())
        .andExpect(jsonPath("$.name").value(productDto.getName()))
        .andExpect(jsonPath("$.price").value(productDto.getPrice()))
        .andDo(
            document(
                "product/document-create-product",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                productDtoRequestSnippet,
                responseHeaders(
                    headerWithName(HttpHeaders.LOCATION)
                        .description("Location of created product resource.")),
                productResponseSnippet,
                productLinks));
  }

  /**
   * Test the read a product given an identifier.
   *
   * @throws Exception is not expected
   */
  @Test
  @DisplayName("can get a product successfully")
  void verifyAndDocumentReadProductSuccessfully() throws Exception {

    // Get one UUID of the first product
    Product product = productRepository.findAll().get(0);

    mockMvc
        .perform(
            get("/store/products/{identifier}", product.getIdentifier())
                .locale(Locale.ENGLISH)
                .accept(MediaTypes.HAL_JSON_UTF8_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.identifier").value(product.getIdentifier().toString()))
        .andExpect(jsonPath("$.name").value(product.getName()))
        .andExpect(jsonPath("$.price").value(product.getPrice()))
        .andDo(
            document(
                "product/document-get-product",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("identifier").description("Identifier of the Product.")),
                productResponseSnippet,
                productLinks));
  }

  /**
   * Test the read a list of products.
   *
   * @throws Exception is not expected
   */
  @Test
  @DisplayName("can get a list of products successfully")
  void verifyAndDocumentReadProductsSuccessfully() throws Exception {

    // Get the size of all the products
    int productSize = (int) productRepository.count();

    mockMvc
        .perform(
            get("/store/products/").locale(Locale.ENGLISH).accept(MediaTypes.HAL_JSON_UTF8_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").exists())
        .andExpect(jsonPath("$.content.length()").value(productSize))
        .andDo(
            document(
                "product/document-get-list-product",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                productListResponseSnippet,
                simpleLinksSnippet));
  }

  /**
   * Test the read a page of products.
   *
   * @throws Exception is not expected
   */
  @Test
  @DisplayName("can get a page of products successfully")
  void verifyAndDocumentReadProductsSuccessfullyWithPageable() throws Exception {

    // Get the size of all the products
    int productSize = (int) productRepository.count();

    mockMvc
        .perform(
            get("/store/products/page")
                .param("sort", "name,asc")
                .param("size", "20")
                .param("page", "0")
                .locale(Locale.ENGLISH)
                .accept(MediaTypes.HAL_JSON_UTF8_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").exists())
        .andExpect(jsonPath("$.content.length()").value(productSize))
        .andExpect(jsonPath("$.size").value(20))
        .andExpect(jsonPath("$.number").value(0))
        .andExpect(jsonPath("$.totalPages").value((productSize / 20) + 1))
        .andExpect(jsonPath("$.totalElements").value(productSize))
        .andDo(
            document(
                "product/document-get-page-product",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(
                    parameterWithName("sort")
                        .optional()
                        .description(
                            "List of product attributes separated by commas followed by sorting productorder ASC/DESC "
                                + "(Optional)"),
                    parameterWithName("page")
                        .optional()
                        .description("Number of the requested page, defaults to 0 (Optional)"),
                    parameterWithName("size")
                        .optional()
                        .description(
                            "Size of the requested page, defaults to 20, maximum is 100 (Optional)")),
                productPageResponseSnippet,
                simpleLinksSnippet));
  }

  /**
   * Test the update of products given a identifier.
   *
   * @throws Exception is not expected
   */
  @Test
  @DisplayName("can update a product successfully")
  void verifyAndDocumentUpdateProductSuccessfully() throws Exception {

    // Get one UUID of the first product
    Product product = productRepository.findAll().get(0);

    ProductDto productDto = new ProductDto("MyTestProductName", new BigDecimal("4.44"));

    mockMvc
        .perform(
            post("/store/products/{identifier}", product.getIdentifier())
                .locale(Locale.ENGLISH)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(productDto))
                .accept(MediaTypes.HAL_JSON_UTF8_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value(productDto.getName()))
        .andExpect(jsonPath("$.price").value(productDto.getPrice()))
        .andDo(
            document(
                "product/document-update-product",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("identifier").description("Identifier of the Product.")),
                productDtoRequestSnippet,
                productResponseSnippet,
                productLinks));
  }
}
