package pl.crystalek.di;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
class InjectServiceImpl implements InjectService {
    @Getter
    ObjectRepositoryImpl objectRepository = new ObjectRepositoryImpl();
    ClassLoader classLoader;
    String packageName;
    Logger logger;

    @Override
    public void injectObjects() {
        final InjectableClassLoader injectableClassLoader = new InjectableClassLoader(classLoader, packageName, logger);
        final Set<Class<?>> classes = injectableClassLoader.loadClasses();

        final InjectableScanner injectableScanner = new InjectableScanner(classes, logger);
        injectableScanner.scan();
        final List<Class<?>> classesToInject = injectableScanner.getClassesToInject();
        final List<Method> factoryMethodsToInject = injectableScanner.getFactoryMethodsToInject();

        final ObjectCreator objectCreator = new ObjectCreator(objectRepository, classesToInject, factoryMethodsToInject, logger);
        objectCreator.createAndInjectObjects();
    }
}
