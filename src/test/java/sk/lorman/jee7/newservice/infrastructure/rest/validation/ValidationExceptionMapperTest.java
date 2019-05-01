package sk.lorman.jee7.newservice.infrastructure.rest.validation;

import static org.assertj.core.api.Assertions.assertThat;

import sk.lorman.jee7.newservice.infrastructure.domain.entity.AbstractEntity;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;

/**
 * Test class for the the exception mapper {@link ValidationExceptionMapper} which handles {@link
 * ConstraintViolationException}s.
 */
public class ValidationExceptionMapperTest {

  @BeforeClass
  public static void setUpBeforeClass() {
    Locale.setDefault(Locale.ENGLISH);
  }

  @AfterClass
  public static void tearDown() {
    Locale.setDefault(Locale.getDefault());
  }

  @Test
  public void toResponse() {
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    Validator validator = validatorFactory.getValidator();

    TestEntity entity = new TestEntity();

    Set<ConstraintViolation<TestEntity>> constraintViolations = validator.validate(entity);
    ConstraintViolationException exception = new ConstraintViolationException(constraintViolations);

    ValidationExceptionMapper exceptionMapper = new ValidationExceptionMapper();
    Response response = exceptionMapper.toResponse(exception);

    assertThat(response.getStatusInfo()).isEqualTo(Response.Status.BAD_REQUEST);
    List<ValidationErrorDTO> validationErrors = (List<ValidationErrorDTO>) response.getEntity();
    assertThat(validationErrors).hasSize(1);

    ValidationErrorDTO validationError = validationErrors.get(0);
    assertThat(validationError.getCode()).isEqualTo("BVAL_NOT-NULL");
    assertThat(validationError.getMessage()).isEqualTo("id may not be null");
  }

  private static class TestEntity extends AbstractEntity<Long> {

    @NotNull
    private Long id;

    @Override
    public Long getId() {
      return id;
    }
  }
}
