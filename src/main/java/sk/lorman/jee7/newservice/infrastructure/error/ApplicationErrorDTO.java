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
package sk.lorman.jee7.newservice.infrastructure.error;

import static org.apache.commons.lang3.Validate.notNull;

import sk.lorman.jee7.newservice.infrastructure.domain.value.AbstractValueObject;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A DTO that represents an application error.
 */
public class ApplicationErrorDTO extends AbstractValueObject {

  @Schema(required = true)
  private String code;

  @Schema(required = true)
  private String message;

  public ApplicationErrorDTO() {
    super();
  }

  public ApplicationErrorDTO(final ApplicationError error, final String message) {
    this();
    this.code = notNull(error, "errorCode must not be null").getCode();
    this.message = notNull(message, "message must not be null");
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  @Override
  protected Object[] values() {
    return new Object[]{code, message};
  }

  @Override
  public String toString() {
    return "ApplicationErrorDTO{"
           + "code='" + code + '\''
           + ", message='" + message + '\''
           + '}';
  }
}
