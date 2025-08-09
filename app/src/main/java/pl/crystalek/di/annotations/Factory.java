package pl.crystalek.di.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation indicating that a static method is an object factory
 * which should be used by the dependency injection mechanism
 * to create object instances.
 * <p>
 * A method annotated with this annotation must be static and return an object
 * that will be added to the DI container. The method parameters will be automatically
 * injected from the DI container.
 * <p>
 * Example:
 * <pre>
 * {@code
 * public class ConfigFactory {
 *     @Factory
 *     public static ConfigInterface createConfig(Repository repository) {
 *         return new ConfigImplementation(repository.getSettings());
 *     }
 * }
 * }
 * </pre>
 *
 * @see Injectable
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Factory {

}
