package sk.lorman.jee7.newservice.infrastructure.rest.jwt;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

import org.hamcrest.Matchers;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.archive.importer.MavenImporter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

/**
 * Arquillian test class for the resource {@link JwtAuthorizationFilter}.
 */
@RunAsClient
@RunWith(Arquillian.class)
public class JwtAuthorizationFilterIT {

  private static final Logger LOG = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

  @Deployment
  public static Archive<?> createDeployment() {
    WebArchive archive = ShrinkWrap.create(MavenImporter.class)
        .loadPomFromFile("pom.xml")
        .importBuildOutput()
        .as(WebArchive.class)
        .addAsResource("log4j2.xml")
        .addAsResource("test-apache-deltaspike.properties", "META-INF/apache-deltaspike.properties")
        .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
        .addAsResource("import.sql")
        .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

    LOG.debug(archive.toString(true));

    return archive;
  }

  @ArquillianResource
  private URL baseURI;

  @Test
  public void requestShouldReturn401ForMissingToken() throws Exception {
    RestAssured.given()
        .filter(new RequestLoggingFilter())
        .filter(new ResponseLoggingFilter())
        .when()
        .get(UriBuilder.fromUri(baseURI.toURI()).path("api").path("version").build())
        .then()
        .statusCode(Response.Status.UNAUTHORIZED.getStatusCode())
        .body(Matchers.is(""));
  }

  @Test
  public void requestShouldReturn404ForEmptyResource() throws Exception {
    RestAssured.given()
        .filter(new RequestLoggingFilter())
        .filter(new ResponseLoggingFilter())
        .when()
        .get(UriBuilder.fromUri(baseURI.toURI()).path("api").build())
        .then()
        .statusCode(Response.Status.NOT_FOUND.getStatusCode())
        .body(Matchers.is(""));
  }
}