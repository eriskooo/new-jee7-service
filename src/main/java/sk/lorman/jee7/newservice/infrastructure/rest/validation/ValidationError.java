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
package sk.lorman.jee7.newservice.infrastructure.rest.validation;

import static org.apache.commons.lang3.Validate.notNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.ConstraintViolation;
import javax.validation.ElementKind;
import javax.validation.Path;
import javax.validation.Path.Node;

/**
 * An DTO that represents a validation error.
 */
public class ValidationError {

  private String propertyPath;

  @JsonIgnore
  private Object invalidValue;

  private String message;

  public ValidationError() {
    super();
  }

  public ValidationError(final ConstraintViolation<?> violation) {
    this();
    notNull(violation, "violation must not be null");
    this.propertyPath = getPropertyPath(violation.getPropertyPath());
    this.invalidValue = violation.getInvalidValue();
    this.message = violation.getMessage();
  }

  private String getPropertyPath(final Path propertyPath) {
    StringBuilder sb = new StringBuilder();
    for (Node node : propertyPath) {
      if (ElementKind.PROPERTY.equals(node.getKind())) {
        if (sb.length() > 0) {
          sb.append('.');
        }
        sb.append(node.getName());
      }
    }

    return sb.toString();
  }

  public String getPropertyPath() {
    return propertyPath;
  }

  public Object getInvalidValue() {
    return invalidValue;
  }

  public String getMessage() {
    return message;
  }
}
