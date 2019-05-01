package sk.lorman.jee7.newservice;

import static org.assertj.core.api.Assertions.assertThat;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.spi.CorsHeaders;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.archive.importer.MavenImporter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

/**
 * Arquillian test class for the CORS filter.
 */
@RunWith(Arquillian.class)
@RunAsClient
public class CorsFilterIT {

  private static final Logger LOG = LoggerFactory.getLogger(CorsFilterIT.class);

  @Deployment
  public static Archive<?> createDeployment() {
    WebArchive archive = ShrinkWrap.create(MavenImporter.class)
        .loadPomFromFile("pom.xml")
        .importBuildOutput()
        .as(WebArchive.class)
        .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
        .addAsResource("import.sql");

    LOG.debug(archive.toString(true));

    return archive;
  }

  @ArquillianResource
  private URL baseURI;

  @Test
  public void getVersion() throws Exception {
    Response response = ClientBuilder.newClient()
        .target(getBaseUri())
        .request(MediaType.TEXT_PLAIN)
        .header(CorsHeaders.ORIGIN, "http://localhost:8080")
        .get(Response.class);

    assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
    assertThat(response.getHeaders()).containsKey(CorsHeaders.ACCESS_CONTROL_ALLOW_ORIGIN);
    assertThat(response.getHeaders()).containsKey(CorsHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS);

    String version = response.readEntity(String.class);
    assertThat(version).isNotNull();
  }

  @Test
  public void getVersionWithMethodOption() throws Exception {
    Response response = ClientBuilder.newClient()
        .target(getBaseUri())
        .request(MediaType.APPLICATION_JSON_TYPE)
        .header(CorsHeaders.ORIGIN, "http://localhost:8080")
        .options(Response.class);

    assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
    assertThat(response.getHeaders()).containsKey(CorsHeaders.ACCESS_CONTROL_ALLOW_ORIGIN);
    assertThat(response.getHeaders()).containsKey(CorsHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS);
    assertThat(response.getHeaders()).containsKey(CorsHeaders.ACCESS_CONTROL_MAX_AGE);
  }

  private URI getBaseUri() throws URISyntaxException {
    return UriBuilder.fromUri(baseURI.toURI()).path("api").path("version").build();
  }
}
