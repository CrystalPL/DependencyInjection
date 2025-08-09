package pl.crystalek.di.scanner;

import pl.crystalek.di.annotations.Factory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class FactoryScanner extends Scanner {
    FactoryScanner(final Set<Class<?>> allLoadedClasses, final ScannerRepository scannerRepository) {
        super(allLoadedClasses, scannerRepository);
    }

    @Override
    public void scan() {
        final List<Method> methods = allLoadedClasses.stream()
                .map(this::getMethodWithFactoryAnnotation)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        scannerRepository.getFactoryMethodsToInject().addAll(methods);
    }

    private List<Method> getMethodWithFactoryAnnotation(final Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Factory.class))
                .filter(method -> !method.getReturnType().equals(void.class))
                .filter(method -> Modifier.isStatic(method.getModifiers()))
                .collect(Collectors.toList());
    }
}
