package pl.crystalek.di.injector;

import pl.crystalek.di.ObjectRepository;
import pl.crystalek.di.scanner.ScannerRepository;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public interface Injector {

    static List<Injector> getInjectors(final ObjectRepository objectRepository, final ScannerRepository scannerRepository, final Logger logger) {
        final List<Class<?>> classesToInject = scannerRepository.getClassesToInject();
        final CircularDependencyValidator circularDependencyValidator = new CircularDependencyValidator(logger);
        final ObjectCreator objectCreator = new ObjectCreator(objectRepository, classesToInject, logger, circularDependencyValidator);
        final InjectorHelper injectorHelper = new InjectorHelper(objectRepository, objectCreator);

        return Arrays.asList(
                new FactoryMethodsInjector(objectRepository, injectorHelper, circularDependencyValidator, logger, scannerRepository.getFactoryMethodsToInject()),
                new ObjectInjector(classesToInject, injectorHelper),
                new FieldsInjector(objectRepository, logger, scannerRepository.getFieldsToInject())
        );
    }

    void inject();
}
