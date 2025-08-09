package pl.crystalek.di.injector;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import pl.crystalek.di.ObjectRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class FactoryMethodsInjector implements Injector {
    ObjectRepository objectRepository;
    InjectorHelper injectorHelper;
    CircularDependencyValidator circularDependencyValidator;
    Logger logger;
    List<Method> factoryMethodsToInject;
    @NonFinal boolean isCircularDependency;

    @Override
    public void inject() {
        factoryMethodsToInject.forEach(method -> createFactoryObjects(method, method));
    }

    private void createFactoryObjects(final Method method, final Method startedMethod) {
        final List<Class<?>> parameters = Arrays.asList(method.getParameterTypes());
        checkCircularDependency(parameters, startedMethod);
        if (!isSuccessfulCreateArgumentObjects(startedMethod, parameters)) {
            return;
        }

        final List<?> objectsToInvokeMethod = getObjectsToInvokeMethod(parameters);
        if (parameters.size() != objectsToInvokeMethod.size()) {
            logger.severe("Not found object for method: " + method.getName());
            return;
        }

        createObjectByParameters(method, objectsToInvokeMethod).ifPresent(object -> objectRepository.addObject(method.getReturnType(), object));
    }

    private void checkCircularDependency(final List<Class<?>> parameters, final Method startedMethod) {
        isCircularDependency = parameters.stream()
                .anyMatch(clazz -> circularDependencyValidator.isCircularDependency(startedMethod.getReturnType(), clazz));
    }

    private boolean isSuccessfulCreateArgumentObjects(final Method startedMethod, final List<Class<?>> parameters) {
        createMethodArgumentsObjectsIfMarkedFactory(parameters, startedMethod);
        if (isCircularDependency) {
            return false;
        }

        injectorHelper.createObjectFromClasses(parameters);
        return true;
    }

    private void createMethodArgumentsObjectsIfMarkedFactory(final List<Class<?>> parameters, final Method startedMethod) {
        if (isCircularDependency) {
            return;
        }

        factoryMethodsToInject.stream()
                .filter(method -> parameters.contains(method.getReturnType()))
                .forEach(method -> createFactoryObjects(method, startedMethod));
    }

    private List<?> getObjectsToInvokeMethod(final List<Class<?>> parameters) {
        return parameters.stream()
                .map(objectRepository::getObjectByClassName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Optional<Object> createObjectByParameters(final Method method, final List<?> objectsToInvokeMethod) {
        try {
            method.setAccessible(true);
            final Object object = method.invoke(null, objectsToInvokeMethod.toArray());
            return Optional.ofNullable(object);
        } catch (final IllegalAccessException | InvocationTargetException exception) {
            exception.printStackTrace();
            logger.severe("Error while creating object from method: " + method.getName());
            return Optional.empty();
        }
    }
}
