package sk.lorman.jee7.newservice.infrastructure.rest.filter;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import java.util.Collections;

/**
 * Test class for the JAX-RS filter {@link CustomCorsFilter}.
 */
public class CustomCorsFilterTest {

  @Test
  public void instantiationShouldSucceed() {
    CustomCorsFilter filter = new CustomCorsFilter();
    assertThat(filter.getAllowedOrigins()).isEqualTo(Collections.singleton("*"));
    assertThat(filter.isAllowCredentials()).isTrue();
    assertThat(filter.getAllowedHeaders()).isEqualTo("Accept,Authorization,Content-Type,Origin,X-REQUEST-ID,");
    assertThat(filter.getAllowedMethods()).isEqualTo("GET,POST,PUT,DELETE,OPTIONS,HEAD,");
    assertThat(filter.getCorsMaxAge()).isEqualTo(42 * 60 * 60);
  }
}
