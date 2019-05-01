package sk.lorman.jee7.newservice.infrastructure.domain.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A base class for nested builders.
 */
@SuppressWarnings("PMD.AbstractNaming")
public abstract class NestedBuilder<T, E> extends DefaultBuilder<E> {

  private static final Logger LOG = LoggerFactory.getLogger(NestedBuilder.class);

  protected T parent;

  /**
   * To get the parent builder
   *
   * @return T the instance of the parent builder
   */
  public T done() {
    Class<?> parentClass = parent.getClass();
    try {
      E build = this.build();
      String methodname = "with" + build.getClass().getSimpleName();
      Method method = parentClass.getDeclaredMethod(methodname, build.getClass());
      method.invoke(parent, build);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      LOG.error(e.getMessage(), e);
    }
    return parent;
  }

  /**
   * Adds parent builder to nested builder.
   *
   * @param t the instance of the parent builder
   * @return P the instance of the parent builder.
   */
  public <P extends NestedBuilder<T, E>> P withParentBuilder(final T t) {
    this.parent = t;
    return (P) this;
  }
}
