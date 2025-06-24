package pl.crystalek.di;

import com.google.common.reflect.ClassPath;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
class InjectableClassLoader {
    ClassLoader classLoader;
    String packageName;
    Logger logger;

    public Set<Class<?>> loadClasses() {
        final ClassPath classPath;
        try {
            classPath = ClassPath.from(classLoader);
        } catch (final IOException exception) {
            logger.severe("Cannot load all classes from ClassLoader.");
            return new HashSet<>();
        }

        return classPath.getAllClasses().stream()
                .filter(it -> it.getPackageName().startsWith(packageName))
                .map(ClassPath.ClassInfo::getName)
                .map(this::getClassByName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    private Optional<Class<?>> getClassByName(final String className) {
        try {
            final Class<?> clazz = Class.forName(className, false, classLoader);
            return Optional.of(clazz);
        } catch (final Exception | Error exception) {
            logger.severe("The required class does not exist: " + className);
            return Optional.empty();
        }
    }
}
