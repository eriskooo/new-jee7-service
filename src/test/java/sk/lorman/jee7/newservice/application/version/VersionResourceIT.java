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

import org.hamcrest.core.StringContains;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
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

import java.net.URL;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import io.restassured.RestAssured;

/**
 * Arquillian test class for the resource {@link VersionResource}.
 */
@RunAsClient
@RunWith(Arquillian.class)
public class VersionResourceIT {

  private static final Logger LOG = LoggerFactory.getLogger(VersionResource.class);

  @Deployment
  public static Archive<?> createDeployment() {
    PomEquippedResolveStage pomFile = Maven.resolver().loadPomFromFile("pom.xml");

    WebArchive archive = ShrinkWrap.create(WebArchive.class)
        .addAsLibraries(pomFile.resolve("org.apache.deltaspike.core:deltaspike-core-api").withTransitivity().asFile())
        .addAsLibraries(pomFile.resolve("org.apache.deltaspike.core:deltaspike-core-impl").withTransitivity().asFile())
        .addClasses(VersionResource.class, JaxRsActivator.class)
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
        .when()
        .get(UriBuilder.fromUri(baseURI.toURI()).path("api").path("version").build())
        .then()
        .body(new StringContains("1.0.0-SNAPSHOT build "));
  }
}