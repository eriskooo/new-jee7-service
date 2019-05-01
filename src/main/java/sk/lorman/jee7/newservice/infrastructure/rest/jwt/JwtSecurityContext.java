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
package sk.lorman.jee7.newservice.infrastructure.rest.jwt;

import java.security.Principal;

import javax.ws.rs.core.SecurityContext;

/**
 * JWT security context. Implements {@link SecurityContext} to create a custom context from JWT token.
 */
public class JwtSecurityContext implements SecurityContext {

  private final JwtPrincipal principal;

  public JwtSecurityContext(final String username, final String... roles) {
    this.principal = new JwtPrincipal(username, roles);
  }

  @Override
  public Principal getUserPrincipal() {
    return principal;
  }

  @Override
  public boolean isUserInRole(final String role) {
    return principal.getRoles().contains(role);
  }

  @Override
  public boolean isSecure() {
    return false;
  }

  @Override
  public String getAuthenticationScheme() {
    return "JWT";
  }
}
