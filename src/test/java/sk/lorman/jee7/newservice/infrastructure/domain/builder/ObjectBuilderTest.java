/*
 * Copyright (C) open knowledge GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package sk.lorman.jee7.newservice.infrastructure.domain.builder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import sk.lorman.jee7.newservice.infrastructure.domain.builder.ObjectBuilderTest.OuterClass.UnspecificSubclass;
import sk.lorman.jee7.newservice.infrastructure.domain.value.AbstractSimpleValueObject;
import sk.lorman.jee7.newservice.infrastructure.domain.value.AbstractValueObject;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.util.Collections;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;

public class ObjectBuilderTest {

  private static final TestFirstSimpleValueObject FIRST_SIMPLE_VO = new TestFirstSimpleValueObject("first");
  private static final TestSecondSimpleValueObject SECOND_SIMPLE_VO = new TestSecondSimpleValueObject("second");

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void stringSubclass() {
    Class<Object> clazz = ObjectBuilder.fromGenericType(StringSubclass.class, GenericSuperclass.class, 0).getType();
    assertThat(clazz).isEqualTo(String.class);
  }

  @Test
  public void integerSubclass() {
    Class<Object> clazz = ObjectBuilder.fromGenericType(IntegerSubclass.class, GenericSuperclass.class, 0).getType();
    assertThat(clazz).isEqualTo(Integer.class);
  }

  @Test(expected = IllegalStateException.class)
  public void rawSuperclassFails() {
    ObjectBuilder.fromGenericType(RawSubclass.class, GenericSuperclass.class, 0);
  }

  @Test(expected = IllegalStateException.class)
  public void unresolvableSuperclassFails() {
    ObjectBuilder.fromGenericType(UnspecificSubclass.class, GenericSuperclass.class, 0);
  }

  @Test
  public void multipleGenericParameters() {
    assertThat(ObjectBuilder.fromGenericType(MultiparameterSubclass.class, MultiparameterSuperclass.class, 0).getType()).isEqualTo(String.class);
    assertThat(ObjectBuilder.fromGenericType(MultiparameterSubclass.class, MultiparameterSuperclass.class, 1).getType()).isEqualTo(Integer.class);
    assertThat(ObjectBuilder.fromGenericType(MultiparameterSwitchingSubclass.class, MultiparameterSuperclass.class, 0).getType()).isEqualTo(Integer.class);
    assertThat(ObjectBuilder.fromGenericType(MultiparameterSwitchingSubclass.class, MultiparameterSuperclass.class, 1).getType()).isEqualTo(String.class);
  }

  @Test
  public void buildWithoutConstructor() {
    assertThat(ObjectBuilder.forType(ClassWithoutConstructor.class).build()).isInstanceOf(ClassWithoutConstructor.class);
  }

  @Test
  public void buildWithPrivateConstructor() {
    assertThat(ObjectBuilder.forType(ClassWithPrivateConstructor.class).build()).isInstanceOf(ClassWithPrivateConstructor.class);
  }

  @Test
  public void buildWithAbstractClass() {
    thrown.expect(IllegalStateException.class);
    thrown.expectCause(instanceOf(InstantiationException.class));
    ObjectBuilder.forType(GenericSuperclass.class).build();
  }

  @Test
  public void buildWithExceptionThrowingConstructor() {
    thrown.expect(IllegalStateException.class);
    thrown.expectCause(instanceOf(IOException.class));
    ObjectBuilder.forType(ClassWithExceptionThrowingConstructor.class).build();
  }

  @Test
  public void buildWithRuntimeExceptionThrowingConstructor() {
    thrown.expect(IllegalArgumentException.class);
    ObjectBuilder.forType(ClassWithRuntimeExceptionThrowingConstructor.class).build();
  }

  @Test
  public void buildWithParameter() {
    TestSecondSimpleValueObject value = ObjectBuilder.forType(TestSecondSimpleValueObject.class)
        .withParameter("second")
        .build();

    assertThat(value).isEqualTo(SECOND_SIMPLE_VO);
  }

  @Test
  public void buildWithParameterConversion() {
    TestMultiValueObject value = ObjectBuilder.forType(TestMultiValueObject.class)
        .withParameter("first")
        .andParameter("second")
        .andParameter(true)
        .build();

    assertThat(value).isEqualTo(new TestMultiValueObject(FIRST_SIMPLE_VO, SECOND_SIMPLE_VO));
  }

  @Test
  public void buildWithSpecificConstructor() {
    TestMultiValueObject value = ObjectBuilder.forType(TestMultiValueObject.class)
        .withParameter("first")
        .andParameter("second")
        .build();

    assertThat(value).isEqualTo(new TestMultiValueObject(FIRST_SIMPLE_VO, SECOND_SIMPLE_VO));
  }

  @Test
  public void buildWithNullParameter() {
    TestMultiValueObject value = ObjectBuilder.forType(TestMultiValueObject.class)
        .withParameter(null)
        .andParameter("second")
        .build();

    assertThat(value).isEqualTo(new TestMultiValueObject(null, SECOND_SIMPLE_VO));
  }

  @Test(expected = IllegalStateException.class)
  public void buildWithInconvertibleConstructor() {
    TestMultiValueObject value = ObjectBuilder.forType(TestMultiValueObject.class)
        .withParameter("first")
        .andParameter("second")
        .andParameter("true")
        .andParameter("now")
        .build();
  }

  @Test(expected = IllegalStateException.class)
  public void buildWithNoMatchingConstructor() {
    ObjectBuilder.forType(TestMultiValueObject.class)
        .withParameter("first")
        .andParameter("second")
        .andParameter("now")
        .andParameter("false")
        .build();
  }

  @Test(expected = IllegalStateException.class)
  public void buildWithAmbiguousConstructors() {
    ObjectBuilder.forType(ClassWithAmbiguousConstructors.class)
        .withParameter("first")
        .andParameter("second")
        .build();
  }

  @Test
  public void buildWithValidator() throws ReflectiveOperationException {
    Validator validator = mock(Validator.class);
    ExecutableValidator executableValidator = mock(ExecutableValidator.class);
    when(validator.forExecutables()).thenReturn(executableValidator);

    TestSecondSimpleValueObject value = ObjectBuilder.forType(TestSecondSimpleValueObject.class)
        .withParameter("second")
        .validatedBy(validator)
        .build();

    assertThat(value).isEqualTo(SECOND_SIMPLE_VO);

    Constructor<TestSecondSimpleValueObject> constructor = TestSecondSimpleValueObject.class.getDeclaredConstructor(String.class);
    verify(executableValidator).validateConstructorParameters(eq(constructor), aryEq(new Object[]{"second"}));
  }

  @Test(expected = ConstraintViolationException.class)
  public void buildWithValidationError() throws ReflectiveOperationException {
    Validator validator = mock(Validator.class);
    ExecutableValidator executableValidator = mock(ExecutableValidator.class);
    when(validator.forExecutables()).thenReturn(executableValidator);
    Constructor<TestSecondSimpleValueObject> constructor = TestSecondSimpleValueObject.class.getDeclaredConstructor(String.class);
    when(executableValidator.validateConstructorParameters(eq(constructor), aryEq(new Object[]{"second"})))
        .thenReturn(Collections.singleton(mock(ConstraintViolation.class)));

    ObjectBuilder.forType(TestSecondSimpleValueObject.class)
        .withParameter("second")
        .validatedBy(validator)
        .build();
  }

  public abstract static class GenericSuperclass<A> {

  }

  public static class GenericNumberSubclass<N extends Number> extends GenericSuperclass<N> {

  }

  public static class IntegerSubclass extends GenericNumberSubclass<Integer> {

  }

  public static class StringSubclass extends GenericSuperclass<String> {

  }

  public static class RawSubclass extends GenericSuperclass {

  }

  public static class OuterClass<X> {

    public class UnspecificSubclass extends GenericSuperclass<X> {

    }

  }

  public static class MultiparameterSuperclass<A, B> {

  }

  public static class MultiparameterInbetweenClass<A, B> extends MultiparameterSuperclass<B, A> {

  }

  public static class MultiparameterSubclass extends MultiparameterSuperclass<String, Integer> {

  }

  public static class MultiparameterSwitchingSubclass extends MultiparameterInbetweenClass<String, Integer> {

  }

  public static class ClassWithoutConstructor {

  }

  public static class ClassWithPrivateConstructor {

    protected ClassWithPrivateConstructor() {
    }
  }

  public static class ClassWithExceptionThrowingConstructor {

    public ClassWithExceptionThrowingConstructor() throws IOException {
      throw new IOException();
    }

  }

  public static class ClassWithRuntimeExceptionThrowingConstructor {

    public ClassWithRuntimeExceptionThrowingConstructor() {
      throw new IllegalArgumentException();
    }

  }

  public static class ClassWithAmbiguousConstructors {

    public ClassWithAmbiguousConstructors(final String first, final Object second) {
    }

    public ClassWithAmbiguousConstructors(final Object first, final String second) {
    }

  }

  private static class TestFirstSimpleValueObject extends AbstractSimpleValueObject<String> {

    public TestFirstSimpleValueObject(final String value) {
      super(value);
    }
  }

  private static class TestSecondSimpleValueObject extends AbstractSimpleValueObject<String> {

    public TestSecondSimpleValueObject(final String value) {
      super(value);
    }

  }

  private static class TestMultiValueObject extends AbstractValueObject {

    private TestFirstSimpleValueObject first;

    private TestSecondSimpleValueObject second;

    protected TestMultiValueObject() {
      // for JPA
    }

    public TestMultiValueObject(final TestFirstSimpleValueObject first, final TestSecondSimpleValueObject second) {
      this.first = first;
      this.second = second;
    }

    public TestMultiValueObject(final Object first, final TestSecondSimpleValueObject second) {
      this(first, second, true);
    }

    public TestMultiValueObject(final TestFirstSimpleValueObject first, final Object second) {
      this(first, new TestSecondSimpleValueObject(second.toString()), false);
    }

    public TestMultiValueObject(final Object first, final TestSecondSimpleValueObject second, final boolean convert) {
      this(convert ? new TestFirstSimpleValueObject(first.toString()) : (TestFirstSimpleValueObject) first, second);
    }

    public TestMultiValueObject(final Object first, final TestSecondSimpleValueObject second, final boolean convert, final LocalDate date) {
      this(convert ? new TestFirstSimpleValueObject(first.toString()) : (TestFirstSimpleValueObject) first, second);
    }

    @Override
    protected Object[] values() {
      return new Object[]{first, second};
    }
  }
}
