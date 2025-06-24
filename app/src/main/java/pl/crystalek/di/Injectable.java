package pl.crystalek.di;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation indicating that a class can be managed by the dependency injection (DI) container.
 * Classes marked with this annotation will be automatically initialized and will have their dependencies
 * injected through the constructor.
 * <p>
 * The DI framework will find all classes with this annotation in a specified package
 * and create their instances, resolving constructor dependencies from other managed objects.
 * <p>
 * Example:
 * <pre>
 * {@code
 * @Injectable
 * public class UserService {
 *     private final UserRepository userRepository;
 *
 *     public UserService(UserRepository userRepository) {
 *         this.userRepository = userRepository;
 *     }
 * }
 * }
 * </pre>
 *
 * @see Factory
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Injectable {
}
