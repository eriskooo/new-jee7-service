package sk.lorman.jee7.newservice.infrastructure.rest.validation;

import static sk.lorman.jee7.newservice.infrastructure.rest.validation.ValidationApplicationError.ASSERT_FALSE;
import static sk.lorman.jee7.newservice.infrastructure.rest.validation.ValidationApplicationError.ASSERT_TRUE;
import static sk.lorman.jee7.newservice.infrastructure.rest.validation.ValidationApplicationError.CUSTOM;
import static sk.lorman.jee7.newservice.infrastructure.rest.validation.ValidationApplicationError.DECIMAL_MAX;
import static sk.lorman.jee7.newservice.infrastructure.rest.validation.ValidationApplicationError.DECIMAL_MIN;
import static sk.lorman.jee7.newservice.infrastructure.rest.validation.ValidationApplicationError.DIGITS;
import static sk.lorman.jee7.newservice.infrastructure.rest.validation.ValidationApplicationError.FUTURE;
import static sk.lorman.jee7.newservice.infrastructure.rest.validation.ValidationApplicationError.MAX;
import static sk.lorman.jee7.newservice.infrastructure.rest.validation.ValidationApplicationError.MIN;
import static sk.lorman.jee7.newservice.infrastructure.rest.validation.ValidationApplicationError.NOT_NULL;
import static sk.lorman.jee7.newservice.infrastructure.rest.validation.ValidationApplicationError.NULL;
import static sk.lorman.jee7.newservice.infrastructure.rest.validation.ValidationApplicationError.PAST;
import static sk.lorman.jee7.newservice.infrastructure.rest.validation.ValidationApplicationError.PATTERN;
import static sk.lorman.jee7.newservice.infrastructure.rest.validation.ValidationApplicationError.SIZE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

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

public class ValidationApplicationErrorTest {

  @Test
  public void getByClazzShouldReturnCustomError() {
    assertThat(ValidationApplicationError.getByClazz(CustomTestCheck.class)).isEqualTo(CUSTOM);
  }

  @Test
  public void getByClazzShouldReturnError() {
    assertThat(ValidationApplicationError.getByClazz(AssertFalse.class)).isEqualTo(ASSERT_FALSE);
    assertThat(ValidationApplicationError.getByClazz(AssertTrue.class)).isEqualTo(ASSERT_TRUE);
    assertThat(ValidationApplicationError.getByClazz(DecimalMax.class)).isEqualTo(DECIMAL_MAX);
    assertThat(ValidationApplicationError.getByClazz(DecimalMin.class)).isEqualTo(DECIMAL_MIN);
    assertThat(ValidationApplicationError.getByClazz(Digits.class)).isEqualTo(DIGITS);
    assertThat(ValidationApplicationError.getByClazz(Future.class)).isEqualTo(FUTURE);
    assertThat(ValidationApplicationError.getByClazz(Max.class)).isEqualTo(MAX);
    assertThat(ValidationApplicationError.getByClazz(Min.class)).isEqualTo(MIN);
    assertThat(ValidationApplicationError.getByClazz(NotNull.class)).isEqualTo(NOT_NULL);
    assertThat(ValidationApplicationError.getByClazz(Null.class)).isEqualTo(NULL);
    assertThat(ValidationApplicationError.getByClazz(Past.class)).isEqualTo(PAST);
    assertThat(ValidationApplicationError.getByClazz(Pattern.class)).isEqualTo(PATTERN);
    assertThat(ValidationApplicationError.getByClazz(Size.class)).isEqualTo(SIZE);
  }
}
