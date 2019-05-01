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
package sk.lorman.jee7.newservice.infrastructure.rest.exception;

import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Response;

/**
 * A runtime exception indicating an {@link javax.ws.rs.core.Response.Status#BAD_GATEWAY} bad gateway error}.
 */
public class BadGatewayException extends ServerErrorException {

  public BadGatewayException() {
    super(Response.Status.BAD_GATEWAY);
  }
}
