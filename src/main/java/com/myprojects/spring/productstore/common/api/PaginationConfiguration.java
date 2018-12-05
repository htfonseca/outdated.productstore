package com.myprojects.spring.productstore.common.api;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.HateoasAwareSpringDataWebConfiguration;

/** Custom Configuration of the Pageable default setting. */
@Configuration
@EnableConfigurationProperties
public class PaginationConfiguration extends HateoasAwareSpringDataWebConfiguration {

  @Value("${smarsite.pagination.maxPageSize: 100}")
  private int maxPageSize;

  /**
   * @param context must not be {@literal null}.
   * @param conversionService must not be {@literal null}.
   */
  public PaginationConfiguration(
      ApplicationContext context, ObjectFactory<ConversionService> conversionService) {
    super(context, conversionService);
  }

  /**
   * Custom bean overwriting some defaults for the pagination request parameters.
   *
   * @return the customized {@link PageableHandlerMethodArgumentResolver}
   */
  @Override
  @Bean
  public HateoasPageableHandlerMethodArgumentResolver pageableResolver() {
    HateoasPageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver =
        new HateoasPageableHandlerMethodArgumentResolver(sortResolver());

    pageableHandlerMethodArgumentResolver.setMaxPageSize(maxPageSize);

    return pageableHandlerMethodArgumentResolver;
  }
}
