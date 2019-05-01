package sk.lorman.jee7.newservice;

import static org.assertj.core.api.Assertions.assertThat;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.archive.importer.MavenImporter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

/**
 * Arquillian test class for the Swagger generation.
 */
@RunWith(Arquillian.class)
@RunAsClient
public class SwaggerIT {

  private static final Logger LOG = LoggerFactory.getLogger(SwaggerIT.class);

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
  public void getSwaggerJson() throws Exception {
    Response response = ClientBuilder.newClient()
        .target(UriBuilder.fromUri(baseURI.toURI()).path("openapi").path("openapi.json").build())
        .request(MediaType.APPLICATION_JSON_TYPE)
        .get(Response.class);

    String json = response.readEntity(String.class);
    assertThat(json).isNotNull();
  }

  @Test
  public void getSwaggerYaml() throws Exception {
    Response response = ClientBuilder.newClient()
        .target(UriBuilder.fromUri(baseURI.toURI()).path("openapi").path("openapi.yaml").build())
        .request(MediaType.TEXT_PLAIN)
        .get(Response.class);

    String yaml = response.readEntity(String.class);
    assertThat(yaml).isNotNull();
  }
}
