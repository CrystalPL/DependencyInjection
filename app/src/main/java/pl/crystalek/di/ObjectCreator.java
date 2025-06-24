package pl.crystalek.di;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class ObjectCreator {
    ObjectRepositoryImpl objectRepository;
    List<Class<?>> classesToInject;
    List<Method> factoryMethodsToInject;
    Logger logger;

    public void createAndInjectObjects() {
        factoryMethodsToInject.forEach(this::createFactoryObjects);
        createObjectFromClasses(classesToInject);
    }

    private void createObjectFromClasses(final List<Class<?>> classes) {
        classes.stream()
                .filter(clazz -> !objectRepository.isObjectExists(clazz))
                .forEach(clazz -> createObject(clazz, clazz));
    }

    private void createFactoryObjects(final Method method) {
        final List<Class<?>> parameters = Arrays.asList(method.getParameterTypes());
        createObjectFromClasses(parameters);
        final List<?> objectsToInvokeMethod = parameters.stream()
                .map(objectRepository::getObjectByClassName)
                .collect(Collectors.toList());

        if (parameters.size() != objectsToInvokeMethod.size()) {
            logger.severe("Not found object for method: " + method.getName());
            return;
        }

        createObjectByParameters(method, objectsToInvokeMethod).ifPresent(object -> objectRepository.addObject(method.getReturnType(), object));
    }

    private Optional<Object> createObjectByParameters(final Method method, final List<?> objectsToInvokeMethod) {
        try {
            final Object object = method.invoke(null, objectsToInvokeMethod.toArray());
            return Optional.of(object);
        } catch (final IllegalAccessException | InvocationTargetException exception) {
            logger.severe("Error while creating object from method: " + method.getName());
            return Optional.empty();
        }
    }

    private void createObject(final Class<?> injectedClass, final Class<?> startedClass) {
        final Constructor<?> constructor = injectedClass.getConstructors()[0];
        final List<Object> parameters = new ArrayList<>();

        for (final Class<?> requiredObjectClass : constructor.getParameterTypes()) {
            if (isCircularDependency(startedClass, requiredObjectClass)) {
                return;
            }

            if (!tryCreateObjectIfAbsent(requiredObjectClass, startedClass)) {
                return;
            }

            Optional.ofNullable(objectRepository.getObjectByClassName(requiredObjectClass)).ifPresent(parameters::add);
        }

        if (parameters.size() == constructor.getParameterTypes().length) {
            createObjectByParameters(constructor, parameters).ifPresent(objectRepository::addObject);
        }
    }

    private boolean isCircularDependency(final Class<?> startedClass, final Class<?> requiredObjectClass) {
        if (requiredObjectClass.equals(startedClass)) {
            logger.severe("Circular dependency: " + startedClass);
            return true;
        }

        return false;
    }

    private boolean tryCreateObjectIfAbsent(final Class<?> requiredObjectClass, final Class<?> startedClass) {
        if (objectRepository.isObjectExists(requiredObjectClass)) {
            return true;
        }

        final Optional<Class<?>> injectOptional = classesToInject.stream()
                .filter(it -> it.equals(requiredObjectClass))
                .findFirst();
        if (!injectOptional.isPresent()) {
            logger.severe("Not found object to inject: " + requiredObjectClass.getName());
            return false;
        }

        createObject(injectOptional.get(), startedClass);
        return true;
    }

    private Optional<Object> createObjectByParameters(final Constructor<?> constructor, final List<Object> objectsToCreateInstance) {
        try {
            final Object object = constructor.newInstance(objectsToCreateInstance.toArray());
            return Optional.of(object);
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            logger.severe("Error while creating object for class: " + constructor.getClass().getName());
            return Optional.empty();
        }
    }
}
