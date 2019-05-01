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
package sk.lorman.jee7.newservice.application;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * JAX-RS Activator
 */
@ApplicationPath("api")
@OpenAPIDefinition(
  info = @Info(version = "1.0.0", title = "New service API", description = "Provides access to the new service API operations"),
  security = {@SecurityRequirement(name = "bearerAuth")},
  servers = {
    @Server(description = "Localhost (http)", url = "http://localhost:8080/new-jee7-service/api"),
    @Server(description = "Secure Localhost (https)", url = "https://localhost:8443/new-jee7-service/api")
  })
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
public class JaxRsActivator extends Application {
}
