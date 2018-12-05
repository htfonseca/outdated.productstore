package com.myprojects.spring.productstore.productorder.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myprojects.spring.productstore.product.model.Product;
import com.myprojects.spring.productstore.product.repository.ProductRepository;
import com.myprojects.spring.productstore.productorder.dto.ParcelDto;
import com.myprojects.spring.productstore.productorder.dto.ProductOrderDto;
import com.myprojects.spring.productstore.productorder.model.Parcel;
import com.myprojects.spring.productstore.productorder.model.ProductOrder;
import com.myprojects.spring.productstore.productorder.repository.ProductOrderRepository;
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
import java.time.LocalDate;
import java.util.*;

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

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@SpringBootTest
@DisplayName("Test that Product Order API")
class ProductOrderControllerEnd2EndApiTest {

  private final RequestFieldsSnippet productOrderDtoRequestSnippet =
      requestFields(
          fieldWithPath("email").description("Email of the product order"),
          fieldWithPath("parcels").description("List of parcel of the product order"),
          fieldWithPath("parcels[].productIdentifier")
              .description("Product identifier of one parcel"),
          fieldWithPath("parcels[].amount").description("Product amount of one parcel"));

  private final ResponseFieldsSnippet productOrderResponseSnippet =
      responseFields(
          fieldWithPath("identifier").description("Product order identifier"),
          fieldWithPath("email").description("Email of the product order"),
          fieldWithPath("date").description("Date of the product order"),
          fieldWithPath("total").description("Total cost of the product order"),
          fieldWithPath("parcels").description("List of parcel of the product order"),
          fieldWithPath("parcels[].product").description("Product name of one parcel"),
          fieldWithPath("parcels[].price").description("Product price of one parcel"),
          fieldWithPath("parcels[].amount").description("Product amount of one parcel"),
          fieldWithPath("parcels[].total").description("Product total value of one parcel"),
          subsectionWithPath("_links").ignored());

  private final ResponseFieldsSnippet productListResponseSnippet =
      responseFields(
          fieldWithPath("content").description("A list of products orders"),
          fieldWithPath("content[].identifier").description("Product identifier"),
          fieldWithPath("content[].email").description("Email of the product order"),
          fieldWithPath("content[].date").description("Date of the product order"),
          fieldWithPath("content[].total").description("Total cost of the product order"),
          fieldWithPath("content[].parcels").description("List of parcel of the product order"),
          fieldWithPath("content[].parcels[].product").description("Product name of one parcel"),
          fieldWithPath("content[].parcels[].price").description("Product price of one parcel"),
          fieldWithPath("content[].parcels[].amount").description("Product amount of one parcel"),
          fieldWithPath("content[].parcels[].total")
              .description("Product total value of one parcel"),
          subsectionWithPath("content[]._links").ignored(),
          subsectionWithPath("_links").ignored());

  private final LinksSnippet simpleLinksSnippet =
      links(linkWithRel(Link.REL_SELF).description("Link to resource itself"));

  @Autowired private ProductRepository productRepository;

  @Autowired private ProductOrderRepository productOrderRepository;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    List<Product> products = new LinkedList<>();
    products.add(new Product(UUID.randomUUID(), "Product1", new BigDecimal("1.11")));
    products.add(new Product(UUID.randomUUID(), "Product2", new BigDecimal("2.22")));
    products.add(new Product(UUID.randomUUID(), "Product3", new BigDecimal("3.33")));
    products.forEach(productRepository::saveAndFlush);

    List<Parcel> parcels = new LinkedList<>();
    parcels.add(new Parcel(products.get(0), 1, products.get(0).getPrice()));
    parcels.add(new Parcel(products.get(1), 2, products.get(1).getPrice()));
    parcels.add(new Parcel(products.get(2), 3, products.get(2).getPrice()));

    List<ProductOrder> productOrders = new LinkedList<>();
    productOrders.add(
        new ProductOrder(
            UUID.randomUUID(),
            "user1@gmail.com",
            LocalDate.now(),
            Arrays.asList(parcels.get(0), parcels.get(1))));
    productOrders.add(
        new ProductOrder(
            UUID.randomUUID(),
            "user2@gmail.com",
            LocalDate.now(),
            Arrays.asList(parcels.get(0), parcels.get(2))));
    productOrders.add(
        new ProductOrder(
            UUID.randomUUID(),
            "user3@gmail.com",
            LocalDate.now(),
            Arrays.asList(parcels.get(1), parcels.get(2))));
    productOrders.forEach(productOrderRepository::saveAndFlush);
  }

  @AfterEach
  void tearDown() {
    productOrderRepository.deleteAll();
    productRepository.deleteAll();
  }

  @Test
  @DisplayName("can created a product order successfully")
  void verifyAndDocumentCreateProductOrderSuccessfully() throws Exception {

    Product product = productRepository.findAll().get(0);
    ParcelDto parcel = new ParcelDto(product.getIdentifier(), 4);
    ProductOrderDto productOrderDto =
        new ProductOrderDto("newUser@gmail.com", Collections.singletonList(parcel));

    mockMvc
        .perform(
            post("/store/productorders")
                .locale(Locale.ENGLISH)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(objectMapper.writeValueAsString(productOrderDto))
                .accept(MediaTypes.HAL_JSON_UTF8_VALUE))
        .andExpect(status().isCreated())
        .andExpect(
            header()
                .string(
                    HttpHeaders.LOCATION, startsWith("http://localhost:8080/store/productorders")))
        .andExpect(jsonPath("$.identifier").exists())
        .andExpect(jsonPath("$.email").value(productOrderDto.getEmail()))
        .andExpect(jsonPath("$.date").exists())
        .andExpect(jsonPath("$.total").value(4.44))
        .andExpect(jsonPath("$.parcels").exists())
        .andExpect(jsonPath("$.parcels.length()").value(1))
        .andExpect(jsonPath("$.parcels[%d].product", 0).value(product.getName()))
        .andExpect(jsonPath("$.parcels[%d].price", 0).value(1.11))
        .andExpect(jsonPath("$.parcels[%d].amount", 0).value(4))
        .andExpect(jsonPath("$.parcels[%d].total", 0).value(4.44))
        .andDo(
            document(
                "productorder/document-create-productorder",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                productOrderDtoRequestSnippet,
                responseHeaders(
                    headerWithName(HttpHeaders.LOCATION)
                        .description("Location of created product order resource.")),
                productOrderResponseSnippet,
                simpleLinksSnippet));
  }

  @Test
  @DisplayName("can get a product order successfully")
  void verifyAndDocumentReadProductSuccessfully() throws Exception {

    ProductOrder productOrder = productOrderRepository.findAll().get(0);

    mockMvc
        .perform(
            get("/store/productorders/{identifier}", productOrder.getIdentifier())
                .locale(Locale.ENGLISH)
                .accept(MediaTypes.HAL_JSON_UTF8_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.identifier").value(productOrder.getIdentifier().toString()))
        .andExpect(jsonPath("$.email").value(productOrder.getEmail()))
        .andExpect(jsonPath("$.date").value(productOrder.getOrderDate().toString()))
        .andExpect(jsonPath("$.total").exists())
        .andExpect(jsonPath("$.parcels").exists())
        .andExpect(jsonPath("$.parcels.length()").value(productOrder.getParcels().size()))
        .andExpect(
            jsonPath("$.parcels[%d].product", 0)
                .value(productOrder.getParcels().get(0).getProduct().getName()))
        .andExpect(
            jsonPath("$.parcels[%d].price", 0).value(productOrder.getParcels().get(0).getPrice()))
        .andExpect(
            jsonPath("$.parcels[%d].amount", 0).value(productOrder.getParcels().get(0).getAmount()))
        .andExpect(jsonPath("$.parcels[%d].total", 0).exists())
        .andExpect(
            jsonPath("$.parcels[%d].product", 1)
                .value(productOrder.getParcels().get(1).getProduct().getName()))
        .andExpect(
            jsonPath("$.parcels[%d].price", 1).value(productOrder.getParcels().get(1).getPrice()))
        .andExpect(
            jsonPath("$.parcels[%d].amount", 1).value(productOrder.getParcels().get(1).getAmount()))
        .andExpect(jsonPath("$.parcels[%d].total", 1).exists())
        .andDo(
            document(
                "productorder/document-get-productorder",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                    parameterWithName("identifier")
                        .description("Identifier of the product order.")),
                productOrderResponseSnippet,
                simpleLinksSnippet));
  }

  @Test
  @DisplayName("can get product orders from start date to end date successfully")
  void verifyAndDocumentReadProductsSuccessfully() throws Exception {

    int productOrderSize = (int) productOrderRepository.count();

    mockMvc
        .perform(
            get("/store/productorders/")
                .param("start", LocalDate.now().minusDays(1).toString())
                .param("end", LocalDate.now().plusDays(1).toString())
                .locale(Locale.ENGLISH)
                .accept(MediaTypes.HAL_JSON_UTF8_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").exists())
        .andExpect(jsonPath("$.content.length()").value(productOrderSize))
        .andDo(
            document(
                "productorder/document-get-list-productorder",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestParameters(
                    parameterWithName("start")
                        .description("Start date to fetch the product orders."),
                    parameterWithName("end").description("End date to fetch the product orders.")),
                productListResponseSnippet,
                simpleLinksSnippet));
  }
}
