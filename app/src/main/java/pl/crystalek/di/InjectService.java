package pl.crystalek.di;

import pl.crystalek.di.annotations.Factory;
import pl.crystalek.di.annotations.Injectable;

import java.util.logging.Logger;

/**
 * Main interface for the dependency injection service.
 * Responsible for managing the scanning, creation, and injection of objects
 * in the application.
 * <p>
 * Usage example:
 * <pre>
 * {@code
 * Logger logger = Logger.getLogger(Main.class.getName());
 * InjectService injectService = InjectService.getInjectService(
 *     Main.class.getClassLoader(),
 *     "com.example.app",
 *     logger
 * );
 * injectService.injectObjects();
 *
 * // Get an injected object
 * UserService userService = injectService.getObjectRepository().getObjectByClassName(UserService.class);
 * }
 * </pre>
 */
public interface InjectService {

    /**
     * Returns the repository of objects managed by the DI container.
     *
     * @return An instance of {@link ObjectRepository} containing all created objects
     */
    ObjectRepository getObjectRepository();

    /**
     * Initiates the process of scanning classes with {@link Injectable} annotation and methods with {@link Factory} annotation,
     * then creates object instances and injects dependencies.
     */
    void injectObjects();

    /**
     * Creating an instance of the dependency injection service.
     *
     * @param classLoader ClassLoader used for scanning classes
     * @param packageName Name of the package to be searched for classes
     * @param logger Logger used for logging information and errors
     * @return instance of {@link InjectService}
     */
    static InjectService getInjectService(final ClassLoader classLoader, final String packageName, final Logger logger) {
        return new InjectServiceImpl(classLoader, packageName, logger);
    }
}
