package pl.crystalek.di.injector;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.crystalek.di.ObjectRepository;
import pl.crystalek.di.annotations.Injectable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class ObjectCreator {
    ObjectRepository objectRepository;
    List<Class<?>> classesToInject;
    Logger logger;
    CircularDependencyValidator circularDependencyValidator;

    void createObject(final Class<?> injectedClass, final Class<?> startedClass) {
        final Constructor<?> constructor = injectedClass.getDeclaredConstructors()[0];
        final List<Object> parameters = new ArrayList<>();

        for (final Class<?> requiredObjectClass : constructor.getParameterTypes()) {
            if (!requiredObjectClass.isAnnotationPresent(Injectable.class)) {
                continue;
            }

            if (circularDependencyValidator.isCircularDependency(startedClass, requiredObjectClass)) {
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

    private boolean tryCreateObjectIfAbsent(final Class<?> requiredObjectClass, final Class<?> startedClass) {
        if (objectRepository.isObjectExists(requiredObjectClass)) {
            return true;
        }

        final Optional<Class<?>> injectOptional = getClassToInject(requiredObjectClass);
        if (!injectOptional.isPresent()) {
            logger.severe("Not found object to inject: " + requiredObjectClass.getName());
            return false;
        }

        createObject(injectOptional.get(), startedClass);
        return true;
    }

    private Optional<Class<?>> getClassToInject(final Class<?> requiredObjectClass) {
        return classesToInject.stream()
                .filter(it -> it.equals(requiredObjectClass))
                .findFirst();
    }

    private Optional<Object> createObjectByParameters(final Constructor<?> constructor, final List<Object> objectsToCreateInstance) {
        try {
            constructor.setAccessible(true);
            final Object object = constructor.newInstance(objectsToCreateInstance.toArray());
            return Optional.of(object);
        } catch (final InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            exception.printStackTrace();
            logger.severe("Error while creating object for class: " + constructor.getDeclaringClass().getName());
            return Optional.empty();
        }
    }
}
