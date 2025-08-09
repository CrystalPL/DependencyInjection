package pl.crystalek.di.injector;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.logging.Logger;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class CircularDependencyValidator {
    Logger logger;

    boolean isCircularDependency(final Class<?> startedClass, final Class<?> requiredObjectClass) {
        if (requiredObjectClass.equals(startedClass)) {
            logger.severe("Circular dependency: " + startedClass);
            return true;
        }

        return false;
    }
}
