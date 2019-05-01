package sk.lorman.jee7.newservice.infrastructure.rest.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import com.auth0.jwt.interfaces.DecodedJWT;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test class for the cdi producer {@link JwtProducer}.
 */
public class JwtProducerTest {

  private JwtProducer producer;

  @Before
  public void setUp() throws Exception {
    producer = new JwtProducer();
  }

  @Test
  public void observeJwt() {
    DecodedJWT jwt = Mockito.mock(DecodedJWT.class);
    Mockito.when(jwt.getToken()).thenReturn(TestJwtTokens.DEFAULT_TOKEN);

    producer.observeJwt(jwt);
    assertThat(producer.getJwt()).isEqualTo(jwt);
  }
}