package sk.lorman.jee7.newservice.infrastructure.rest.validation;

import sk.lorman.jee7.newservice.infrastructure.error.ApplicationError;

import java.lang.annotation.Annotation;
import java.util.EnumSet;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public enum ValidationApplicationError implements ApplicationError {

  ASSERT_FALSE(AssertFalse.class, "BVAL_ASSERT-FALSE"),

  ASSERT_TRUE(AssertTrue.class, "BVAL_ASSERT-TRUE"),

  DECIMAL_MAX(DecimalMax.class, "BVAL_DECIMAL-MAX"),

  DECIMAL_MIN(DecimalMin.class, "BVAL_DECIMAL-MIN"),

  DIGITS(Digits.class, "BVAL_DIGITS"),

  FUTURE(Future.class, "BVAL_FUTURE"),

  MAX(Max.class, "BVAL_MAX"),

  MIN(Min.class, "BVAL_MIN"),

  NOT_NULL(NotNull.class, "BVAL_NOT-NULL"),

  NULL(Null.class, "BVAL_NULL"),

  PAST(Past.class, "BVAL_PAST"),

  PATTERN(Pattern.class, "BVAL_PATTERN"),

  SIZE(Size.class, "BVAL_SIZE"),

  CUSTOM(null, "BVAL_CUSTOM");

  private static final Map<Class<? extends Annotation>, ValidationApplicationError> LOOKUP = EnumSet.range(ASSERT_FALSE, SIZE)
      .stream()
      .collect(Collectors.toMap(ValidationApplicationError::getClazz, Function.identity()));

  private Class<? extends Annotation> clazz;

  private String code;

  ValidationApplicationError(final Class<? extends Annotation> clazz, final String code) {
    this.clazz = clazz;
    this.code = code;
  }

  public Class<? extends Annotation> getClazz() {
    return clazz;
  }

  @Override
  public String getCode() {
    return code;
  }

  public static ValidationApplicationError getByClazz(final Class<? extends Annotation> clazz) {
    ValidationApplicationError error = CUSTOM;
    if (LOOKUP.containsKey(clazz)) {
      error = LOOKUP.get(clazz);
    }

    return error;
  }
}
