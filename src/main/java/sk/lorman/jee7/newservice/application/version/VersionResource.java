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

import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/*
 * A resource that provides access to version information
 */
@Path("version")
@PermitAll
@OpenAPIDefinition(tags = {@Tag(name = "Version", description = "resource to version information")})
public class VersionResource {

  private static final Logger LOG = LoggerFactory.getLogger(VersionResource.class);

  @Inject
  @ConfigProperty(name = "build.version")
  private String version;

  @Inject
  @ConfigProperty(name = "build.timestamp")
  private String timestamp;

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  @Operation(tags = {"Version"}, summary = "Get version information")
  @ApiResponse(responseCode = "200", description = "Successful retrieval of version information")
  public Response getVersion() {
    LOG.info("Get version information");
    return Response.ok(version + " build " + timestamp).build();
  }
}
