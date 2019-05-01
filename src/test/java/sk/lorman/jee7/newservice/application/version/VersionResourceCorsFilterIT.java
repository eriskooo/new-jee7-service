/*
 * Copyright (C) open knowledge GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package sk.lorman.jee7.newservice.application.version;

import sk.lorman.jee7.newservice.application.JaxRsActivator;
import sk.lorman.jee7.newservice.infrastructure.domain.value.AbstractValueObject;
import sk.lorman.jee7.newservice.infrastructure.rest.filter.CustomCorsFilter;
import sk.lorman.jee7.newservice.infrastructure.rest.jwt.JwtAuthorizationFilter;
import sk.lorman.jee7.newservice.infrastructure.rest.jwt.JwtClaims;
import sk.lorman.jee7.newservice.infrastructure.user.AuthenticatedUser;

import org.hamcrest.core.StringContains;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.spi.CorsHeaders;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import io.restassured.RestAssured;

/**
 * Arquillian test class for the resource {@link VersionResource}.
 */
@RunAsClient
@RunWith(Arquillian.class)
public class VersionResourceCorsFilterIT {

  private static final Logger LOG = LoggerFactory.getLogger(VersionResource.class);

  private static String LOCALHOST = "http://localhost:8080";

  @Deployment
  public static Archive<?> createDeployment() {
    PomEquippedResolveStage pomFile = Maven.resolver().loadPomFromFile("pom.xml");

    WebArchive archive = ShrinkWrap.create(WebArchive.class)
        .addAsLibraries(pomFile.resolve("org.apache.commons:commons-lang3").withTransitivity().asFile())
        .addAsLibraries(pomFile.resolve("org.apache.deltaspike.core:deltaspike-core-api").withTransitivity().asFile())
        .addAsLibraries(pomFile.resolve("org.apache.deltaspike.core:deltaspike-core-impl").withTransitivity().asFile())
        .addAsLibraries(pomFile.resolve("com.auth0:java-jwt").withTransitivity().asFile())
        .addClasses(VersionResource.class, JaxRsActivator.class)
        .addClasses(JwtAuthorizationFilter.class, JwtClaims.class)
        .addClasses(AuthenticatedUser.class, AbstractValueObject.class)
        .addClass(CustomCorsFilter.class)
        .addAsResource("log4j2.xml")
        .addAsResource("META-INF/apache-deltaspike.properties")
        .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

    LOG.debug(archive.toString(true));
    return archive;
  }

  @ArquillianResource
  private URL baseURI;

  @Test
  public void getVersion() throws Exception {
    RestAssured.given()
        .contentType(MediaType.TEXT_PLAIN)
        .header(CorsHeaders.ORIGIN, LOCALHOST)
        .when().get(getBaseUri())
        .then()
        .statusCode(Status.OK.getStatusCode())
        .header(CorsHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, LOCALHOST)
        .header(CorsHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true")
        .body(new StringContains("1.0.0-SNAPSHOT build "));
  }

  @Test
  public void getVersionWithMethodOption() throws Exception {
    RestAssured.given()
        .contentType(MediaType.APPLICATION_JSON)
        .header(CorsHeaders.ORIGIN, LOCALHOST)
        .when().get(getBaseUri())
        .then()
        .statusCode(Status.OK.getStatusCode())
        .header(CorsHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, LOCALHOST)
        .header(CorsHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true")
        .body(new StringContains("1.0.0-SNAPSHOT build "));
  }

  private URI getBaseUri() throws URISyntaxException {
    return UriBuilder.fromUri(baseURI.toURI()).path("api").path("version").build();
  }
}
