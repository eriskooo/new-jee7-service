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

import static org.apache.commons.lang3.Validate.notNull;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * JWT principal. Implements {@link Principal} to represent a username, role etc. for JAAS Subject.
 *
 * An instance of {@link Principal} is obtained from call to SecurityContext.getUserPrincipal(). The default returned type is above
 * principal interface which provides just principal.getName().
 */
public class JwtPrincipal implements Principal {

  private final String name;

  private final Set<String> roles;

  public JwtPrincipal(final String name, final String... roles) {
    this.name = notNull(name, "name must not be null");
    this.roles = new LinkedHashSet<>(Arrays.asList(notNull(roles, "roles must not be null")));
  }

  @Override
  public String getName() {
    return name;
  }

  public Set<String> getRoles() {
    return Collections.unmodifiableSet(roles);
  }

  @Override
  public String toString() {
    return "JwtPrincipal{"
           + "name='" + name + '\''
           + ", roles=" + roles
           + '}';
  }
}
