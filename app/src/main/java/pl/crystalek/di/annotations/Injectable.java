package pl.crystalek.di.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation indicating that a class or a field can be managed by the dependency injection (DI) container.
 * <p>
 * Usage:
 * <ul>
 *     <li><b>On classes</b> – marks the class as a managed component. The DI container will automatically
 *     create its instance and resolve constructor dependencies from other managed objects.</li>
 *     <li><b>On fields</b> – marks a field for dependency injection. The DI container will inject
 *     an already managed instance of the required type into this field.
 *     <b>The target object must already exist in the DI container</b> – it must be created
 *     either automatically (by marking the class with {@code @Injectable}) or manually registered.</li>
 * </ul>
 *
 * <p><b>Example – Class-level injection</b>:
 * <pre>{@code
 * @Injectable
 * public class UserService {
 *     private final UserRepository userRepository;
 *
 *     public UserService(UserRepository userRepository) {
 *         this.userRepository = userRepository;
 *     }
 * }
 * }</pre>
 *
 * <p><b>Example – Field-level injection</b>:
 * <pre>{@code
 * public class UserController {
 *
 *     // Injects an existing managed UserService instance
 *     @Injectable
 *     private UserService userService;
 *
 *     public void getUser() {
 *         userService.findUser(...);
 *     }
 * }
 * }</pre>
 *
 * @see Factory
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Injectable {
}
