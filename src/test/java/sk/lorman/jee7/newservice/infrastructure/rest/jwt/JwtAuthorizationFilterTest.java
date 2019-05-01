package sk.lorman.jee7.newservice.infrastructure.rest.jwt;

import static sk.lorman.jee7.newservice.infrastructure.rest.jwt.TestJwtTokens.DEFAULT_TOKEN;
import static java.lang.Boolean.FALSE;
import static javax.ws.rs.HttpMethod.GET;
import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static org.assertj.core.api.Assertions.assertThat;

import sk.lorman.jee7.newservice.infrastructure.user.AuthenticatedUser;

import com.auth0.jwt.interfaces.DecodedJWT;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URI;

import javax.enterprise.event.Event;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

/**
 * Test class for the JAX-RS filter {@link JwtAuthorizationFilter}.
 */
@RunWith(MockitoJUnitRunner.class)
public class JwtAuthorizationFilterTest {

  private static final String INVALID_TOKEN = "eyJhbGciOiJIUzI1NiJ9.ey.Xpk63zUfXiI5f_bdGjqrhx03aGyBn9ETgXbkAgLalPk";

  private static final String URL = "http://localhost:8080/new-service/api/baskets";

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Mock
  private ContainerRequestContext requestContext;

  @Mock
  private UriInfo uriInfo;

  @Mock
  private Event<AuthenticatedUser> userEvent;

  @Mock
  private Event<DecodedJWT> jwtEvent;

  @InjectMocks
  private JwtAuthorizationFilter filter = new JwtAuthorizationFilter("secret", FALSE, FALSE);

  @Test
  public void filterShouldReturnVoidForMethodOptions() throws Exception {
    Mockito.doReturn(HttpMethod.OPTIONS).when(requestContext).getMethod();

    filter.filter(requestContext);

    ArgumentCaptor<Response> captor = ArgumentCaptor.forClass(Response.class);
    Mockito.verify(requestContext).abortWith(captor.capture());
    Mockito.verifyZeroInteractions(jwtEvent, userEvent);

    Response response = captor.getValue();
    assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
  }

  @Test
  public void filterShouldReturnVoidForValidToken() throws Exception {
    Mockito.doReturn(GET).when(requestContext).getMethod();
    Mockito.when(requestContext.getHeaderString(AUTHORIZATION)).thenReturn("Bearer " + DEFAULT_TOKEN);

    UriInfo uriInfo = Mockito.mock(UriInfo.class);
    Mockito.doReturn(new URI(URL)).when(uriInfo).getAbsolutePath();

    Mockito.doReturn(uriInfo).when(requestContext).getUriInfo();

    filter.filter(requestContext);

    ArgumentCaptor<SecurityContext> securityContextCaptor = ArgumentCaptor.forClass(SecurityContext.class);
    Mockito.verify(requestContext).setSecurityContext(securityContextCaptor.capture());

    SecurityContext securityContext = securityContextCaptor.getValue();
    assertThat(securityContext).isNotNull();
    assertThat(securityContext).isInstanceOf(JwtSecurityContext.class);

    ArgumentCaptor<DecodedJWT> decodedJwtCaptor = ArgumentCaptor.forClass(DecodedJWT.class);
    Mockito.verify(jwtEvent).fire(decodedJwtCaptor.capture());

    DecodedJWT decodedJWT = decodedJwtCaptor.getValue();
    assertThat(decodedJWT.getToken()).isEqualTo(DEFAULT_TOKEN);

    ArgumentCaptor<AuthenticatedUser> authenticatedUserCaptor = ArgumentCaptor.forClass(AuthenticatedUser.class);
    Mockito.verify(userEvent).fire(authenticatedUserCaptor.capture());

    AuthenticatedUser authenticatedUser = authenticatedUserCaptor.getValue();
    assertThat(authenticatedUser).isEqualTo(new AuthenticatedUser("1", "Saskia", "Bodein"));
  }

  @Test
  public void filterShouldReturnVoidForWhitelistedSwaggerJson() throws Exception {
    Mockito.doReturn(GET).when(requestContext).getMethod();

    UriInfo uriInfo = Mockito.mock(UriInfo.class);
    Mockito.doReturn(new URI("http://localhost:8080/new-service/api/swagger.json")).when(uriInfo).getAbsolutePath();

    Mockito.doReturn(uriInfo).when(requestContext).getUriInfo();

    filter.filter(requestContext);

    Mockito.verifyZeroInteractions(jwtEvent, userEvent);
  }

  @Test
  public void filterShouldReturnVoidForWhitelistedUrlVersionResource() throws Exception {
    Mockito.doReturn(GET).when(requestContext).getMethod();

    UriInfo uriInfo = Mockito.mock(UriInfo.class);
    Mockito.doReturn(new URI("http://localhost:8080/new-service/api/version")).when(uriInfo).getAbsolutePath();

    Mockito.doReturn(uriInfo).when(requestContext).getUriInfo();

    filter.filter(requestContext);

    Mockito.verifyZeroInteractions(jwtEvent, userEvent);
  }

  @Test
  public void filterShouldThrowNotAuthorizedExceptionForInvalidAuthorizationHeader() throws Exception {
    thrown.expect(NotAuthorizedException.class);
    thrown.expectMessage("Authorization header is invalid");

    Mockito.doReturn(GET).when(requestContext).getMethod();
    Mockito.when(requestContext.getHeaderString(AUTHORIZATION)).thenReturn("INVALID");

    UriInfo uriInfo = Mockito.mock(UriInfo.class);
    Mockito.doReturn(new URI(URL)).when(uriInfo).getAbsolutePath();

    Mockito.doReturn(uriInfo).when(requestContext).getUriInfo();

    filter.filter(requestContext);

    Mockito.verifyZeroInteractions(jwtEvent, userEvent);
  }

  @Test
  public void filterShouldThrowNotAuthorizedExceptionForInvalidToken() throws Exception {
    thrown.expect(NotAuthorizedException.class);
    thrown.expectMessage("JWT token verification failed");

    Mockito.doReturn(GET).when(requestContext).getMethod();
    Mockito.when(requestContext.getHeaderString(AUTHORIZATION)).thenReturn("Bearer " + INVALID_TOKEN);

    UriInfo uriInfo = Mockito.mock(UriInfo.class);
    Mockito.doReturn(new URI(URL)).when(uriInfo).getAbsolutePath();

    Mockito.doReturn(uriInfo).when(requestContext).getUriInfo();

    filter.filter(requestContext);

    Mockito.verifyZeroInteractions(jwtEvent, userEvent);
  }

  @Test
  public void filterShouldThrowNotAuthorizedExceptionForMissingAuthorizationHeader() throws Exception {
    thrown.expect(NotAuthorizedException.class);
    thrown.expectMessage("Authorization header is missing");

    Mockito.doReturn(GET).when(requestContext).getMethod();

    UriInfo uriInfo = Mockito.mock(UriInfo.class);
    Mockito.doReturn(new URI(URL)).when(uriInfo).getAbsolutePath();

    Mockito.doReturn(uriInfo).when(requestContext).getUriInfo();

    filter.filter(requestContext);

    Mockito.verifyZeroInteractions(jwtEvent, userEvent);
  }
}