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

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.Provider;

/**
 * JAX-RS Filter that writes all REST requests and responses to log.
 */
@Provider
public class LogFilter implements ContainerRequestFilter, ContainerResponseFilter {

  private static final Logger LOG = LoggerFactory.getLogger(LogFilter.class);

  @Context
  private HttpServletRequest request;

  @Context
  private ResourceInfo resourceInfo;

  /*
   * filter request
   */
  @Override
  public void filter(final ContainerRequestContext requestContext) {
    StringBuilder sb = new StringBuilder()
        .append("User: ").append(getPrincipal(requestContext))
        .append(" - Method: ").append(requestContext.getMethod())
        .append(" - URI: ").append(requestContext.getUriInfo().getPath())
        .append(" - Path: ").append(getPath())
        .append(" - Header: ").append(getRequestHeaders(requestContext))
        .append(" - Entity: ").append(getRequestBody(requestContext));

    LOG.info("HTTP REQUEST : {}", sb.toString());
  }

  private String getPrincipal(final ContainerRequestContext requestContext) {
    SecurityContext securityContext = requestContext.getSecurityContext();
    Principal principal = securityContext.getUserPrincipal();
    return principal == null ? "unknown" : principal.toString();
  }

  private String getPath() {
    UriBuilder uriBuilder = UriBuilder
        .fromPath(request.getContextPath())
        .path(request.getServletPath());

    Method resourceMethod = resourceInfo.getResourceMethod();
    if (resourceMethod.getDeclaringClass().isAnnotationPresent(Path.class)) {
      Path classLevelPath = resourceMethod.getDeclaringClass().getAnnotation(Path.class);
      uriBuilder.path(classLevelPath.value());
    }
    if (resourceMethod.isAnnotationPresent(Path.class)) {
      Path methodLevelPath = resourceMethod.getAnnotation(Path.class);
      uriBuilder.path(methodLevelPath.value());
    }

    return uriBuilder.toTemplate();
  }

  private MultivaluedMap<String, String> getRequestHeaders(final ContainerRequestContext requestContext) {
    return requestContext.getHeaders();
  }

  private String getRequestBody(final ContainerRequestContext requestContext) {
    StringBuilder sb = new StringBuilder();
    if (requestContext.hasEntity()) {
      InputStream in = requestContext.getEntityStream();
      ByteArrayOutputStream out = new ByteArrayOutputStream();

      try {
        IOUtils.copy(in, out);

        byte[] requestEntity = out.toByteArray();
        if (requestEntity.length > 0) {
          sb.append(new String(requestEntity, Charset.defaultCharset()));
        }
        requestContext.setEntityStream(new ByteArrayInputStream(requestEntity));
      } catch (IOException e) {
        LOG.error("LogFilter cannot read request entity", e);
      }
    }

    return sb.toString();
  }

  /*
   * filter response
   */
  @Override
  public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext) {
    StringBuilder sb = new StringBuilder()
        .append("StatusCode: ").append(responseContext.getStatusInfo())
        .append(" - Header: ").append(getResponseHeaders(responseContext))
        .append(" - Entity: ").append(getResponseBody(responseContext));

    LOG.info("HTTP RESPONSE : {}", sb.toString());
  }

  private MultivaluedMap<String, Object> getResponseHeaders(final ContainerResponseContext responseContext) {
    return responseContext.getHeaders();
  }

  private String getResponseBody(final ContainerResponseContext responseContext) {
    StringBuilder sb = new StringBuilder();
    if (responseContext.hasEntity()) {
      try {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.writeTo(responseContext.getEntityStream());

        byte[] responseEntity = out.toByteArray();
        if (responseEntity.length > 0) {
          sb.append(new String(responseEntity, Charset.defaultCharset()));
        }
      } catch (IOException e) {
        LOG.error("LogFilter cannot read response entity", e);
      }
    }

    return sb.toString();
  }
}
