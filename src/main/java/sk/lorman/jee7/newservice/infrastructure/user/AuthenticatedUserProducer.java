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
package sk.lorman.jee7.newservice.infrastructure.user;

import sk.lorman.jee7.newservice.infrastructure.qualifier.Authenticated;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;

/**
 * Produces an authenticated user for the application.
 */
@RequestScoped
public class AuthenticatedUserProducer {

  private static final Logger LOG = LoggerFactory.getLogger(AuthenticatedUserProducer.class);

  private AuthenticatedUser user;

  @Produces
  @RequestScoped
  @Authenticated
  public AuthenticatedUser getUser() {
    return user;
  }

  public void observesAuthenticationEvent(@Observes @Authenticated final AuthenticatedUser authenticatedUser) {
    LOG.debug("Observes user event {}", authenticatedUser);
    this.user = authenticatedUser;
  }
}
