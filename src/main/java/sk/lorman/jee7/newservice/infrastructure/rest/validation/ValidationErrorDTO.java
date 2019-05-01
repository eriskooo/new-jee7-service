package sk.lorman.jee7.newservice.infrastructure.rest.validation;

import static org.apache.commons.lang3.Validate.notNull;

import sk.lorman.jee7.newservice.infrastructure.error.ApplicationError;
import sk.lorman.jee7.newservice.infrastructure.error.ApplicationErrorDTO;

import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;

import javax.validation.ConstraintViolation;
import javax.validation.ElementKind;
import javax.validation.Path;

/**
 * A DTO that represents a validation error.
 */
public class ValidationErrorDTO extends ApplicationErrorDTO {

  public ValidationErrorDTO(final ConstraintViolation constraintViolation) {
    super(extractApplicationError(notNull(constraintViolation, "must not be null")),
          extractMessage(notNull(constraintViolation, "must not be null")));
  }

  private static ApplicationError extractApplicationError(final ConstraintViolation constraintViolation) {
    Annotation annotation = constraintViolation.getConstraintDescriptor().getAnnotation();
    return ValidationApplicationError.getByClazz(annotation.annotationType());
  }

  private static String extractMessage(final ConstraintViolation constraintViolation) {
    String propertyPath = getPropertyPath(constraintViolation);

    StringBuilder sb = new StringBuilder();
    if (StringUtils.isNotBlank(propertyPath) && constraintViolation.getMessageTemplate().startsWith("{javax.validation.constraints")) {
      sb.append(propertyPath).append(' ');
    }

    sb.append(constraintViolation.getMessage());
    return sb.toString();
  }

  private static String getPropertyPath(final ConstraintViolation constraintViolation) {
    StringBuilder sb = new StringBuilder();
    for (Path.Node node : constraintViolation.getPropertyPath()) {
      if (ElementKind.PROPERTY.equals(node.getKind())) {
        if (sb.length() > 0) {
          sb.append('.');
        }
        sb.append(node.getName());
      }
    }

    return sb.toString();
  }
}
