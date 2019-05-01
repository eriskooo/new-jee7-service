package sk.lorman.jee7.newservice.infrastructure.permissions;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Accepts a list of permissions, as best practise from {@link Permissions}.
 */
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface PermissionRequired {

  int[] value();
}
