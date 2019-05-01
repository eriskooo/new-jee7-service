package sk.lorman.jee7.newservice.infrastructure.rest.jwt;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static org.assertj.core.api.Assertions.assertThat;

import com.auth0.jwt.interfaces.DecodedJWT;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.core.MultivaluedHashMap;

/**
 * Test class for the JAX-RS client filter {@link ClientSideJwtAuthorizationFilter}.
 */
public class ClientSideJwtAuthorizationFilterTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private DecodedJWT decodedJWT;

  private ClientSideJwtAuthorizationFilter filter;

  @Before
  public void setUp() throws Exception {
    decodedJWT = Mockito.mock(DecodedJWT.class);
    Mockito.when(decodedJWT.getToken()).thenReturn(TestJwtTokens.DEFAULT_TOKEN);

    filter = new ClientSideJwtAuthorizationFilter(decodedJWT);
  }

  @Test
  public void filter() {
    ClientRequestContext requestContext = Mockito.mock(ClientRequestContext.class);

    MultivaluedHashMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
    Mockito.when(requestContext.getHeaders()).thenReturn(headers);

    filter.filter(requestContext);

    assertThat(headers).containsKey(AUTHORIZATION);
    assertThat(headers.getFirst(AUTHORIZATION)).isEqualTo("Bearer " + TestJwtTokens.DEFAULT_TOKEN);
  }

  @Test
  public void instantiationShouldFailForMissingParam() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("jwt must not be null");
    new ClientSideJwtAuthorizationFilter(null);
  }
}
