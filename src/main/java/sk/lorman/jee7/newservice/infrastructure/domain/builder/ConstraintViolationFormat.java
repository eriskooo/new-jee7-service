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
package sk.lorman.jee7.newservice.infrastructure.domain.builder;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;

/**
 * Constraint Violation Format. Provides bean validation messages as concatenated string.
 */
public final class ConstraintViolationFormat {

  private ConstraintViolationFormat() {
    super();
  }

  public static <E> String format(final Set<ConstraintViolation<E>> constraintViolations) {
    notNull(constraintViolations, "constraintViolations must not be null");

    Iterator<ConstraintViolation<E>> iterator = constraintViolations.iterator();

    StringBuilder sb = new StringBuilder();
    while (iterator.hasNext()) {
      ConstraintViolation<E> constraintViolation = iterator.next();
      sb.append(constraintViolation.getRootBeanClass().getSimpleName())
          .append('.')
          .append(constraintViolation.getPropertyPath().toString())
          .append(' ')
          .append(constraintViolation.getMessage());

      if (iterator.hasNext()) {
        sb.append(", ");
      }
    }

    return sb.toString();
  }
}
