package pl.crystalek.di;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
class InjectableScanner {
    final Set<Class<?>> classes;
    final Logger logger;
    @Getter
    List<Class<?>> classesToInject;
    @Getter
    List<Method> factoryMethodsToInject;

    public void scan() {
        loadFactoryMethods();
        loadClassesToInject();
    }

    private void loadClassesToInject() {
        classesToInject = classes.stream()
                .filter(clazz -> clazz.isAnnotationPresent(Injectable.class))
                .filter(clazz -> !clazz.isInterface())
                .filter(this::isOneConstructorInClass)
                .collect(Collectors.toList());
    }

    private void loadFactoryMethods() {
        factoryMethodsToInject =  classes.stream()
                .map(this::getMethodWithFactoryAnnotation)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private boolean isOneConstructorInClass(final Class<?> clazz) {
        if (clazz.getConstructors().length != 1) {
            logger.severe("The number of constructors in the injectable class must be equal to 1");
            return false;
        }

        return true;
    }

    private List<Method> getMethodWithFactoryAnnotation(final Class<?> clazz) {
        return Arrays.stream(clazz.getMethods())
                .filter(method -> method.isAnnotationPresent(Factory.class))
                .filter(method -> !method.getReturnType().equals(void.class))
                .filter(method -> method.getReturnType().isInterface())
                .filter(method -> Modifier.isStatic(method.getModifiers()))
                .collect(Collectors.toList());
    }
}
