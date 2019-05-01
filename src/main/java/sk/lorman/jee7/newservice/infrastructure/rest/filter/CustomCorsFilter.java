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
package sk.lorman.jee7.newservice.infrastructure.rest.filter;

import static sk.lorman.jee7.newservice.infrastructure.web.CustomHttpHeader.X_REQUEST_ID;
import static javax.ws.rs.HttpMethod.DELETE;
import static javax.ws.rs.HttpMethod.GET;
import static javax.ws.rs.HttpMethod.HEAD;
import static javax.ws.rs.HttpMethod.OPTIONS;
import static javax.ws.rs.HttpMethod.POST;
import static javax.ws.rs.HttpMethod.PUT;
import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static org.jboss.resteasy.spi.CorsHeaders.ORIGIN;

import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

/**
 * JAX-RS Filter that handles CORS requests both preflight and simple CORS requests.
 */
@Provider
@PreMatching
public class CustomCorsFilter extends org.jboss.resteasy.plugins.interceptors.CorsFilter {

  private static final String[] ALLOWED_HEADERS = new String[]{ACCEPT, AUTHORIZATION, CONTENT_TYPE, ORIGIN, X_REQUEST_ID};
  private static final String[] ALLOWED_METHODS = new String[]{GET, POST, PUT, DELETE, OPTIONS, HEAD};

  private static final int MAX_AGE = 42 * 60 * 60;

  public CustomCorsFilter() {
    super();
    this.getAllowedOrigins().add("*");
    this.setAllowCredentials(true);
    this.setAllowedHeaders(createList(ALLOWED_HEADERS));
    this.setAllowedMethods(createList(ALLOWED_METHODS));
    this.setCorsMaxAge(MAX_AGE);
  }

  private String createList(final String... values) {
    StringBuilder sb = new StringBuilder();
    for (String value : values) {
      sb.append(value);
      sb.append(',');
    }

    return sb.toString();
  }
}
