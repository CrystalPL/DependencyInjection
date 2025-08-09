package pl.crystalek.di.scanner;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import pl.crystalek.di.annotations.Injectable;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class InjectableClassesScanner extends Scanner {
    Logger logger;

    InjectableClassesScanner(final Set<Class<?>> allLoadedClasses, final ScannerRepository scannerRepository, final Logger logger) {
        super(allLoadedClasses, scannerRepository);

        this.logger = logger;
    }

    @Override
    public void scan() {
        final List<Class<?>> classes = allLoadedClasses.stream()
                .filter(clazz -> clazz.isAnnotationPresent(Injectable.class))
                .filter(clazz -> !clazz.isInterface())
                .filter(this::isOneConstructorInClass)
                .collect(Collectors.toList());

        scannerRepository.getClassesToInject().addAll(classes);
    }

    private boolean isOneConstructorInClass(final Class<?> clazz) {
        if (clazz.getDeclaredConstructors().length != 1) {
            logger.severe("The number of constructors in the injectable class must be equal to 1");
            return false;
        }

        return true;
    }

}
