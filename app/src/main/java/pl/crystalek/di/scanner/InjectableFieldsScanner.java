package pl.crystalek.di.scanner;

import pl.crystalek.di.annotations.Injectable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class InjectableFieldsScanner extends Scanner {
    InjectableFieldsScanner(final Set<Class<?>> allLoadedClasses, final ScannerRepository scannerRepository) {
        super(allLoadedClasses, scannerRepository);
    }

    @Override
    public void scan() {
        final List<Field> fields = allLoadedClasses.stream()
                .map(Class::getDeclaredFields)
                .flatMap(Arrays::stream)
                .filter(field -> field.isAnnotationPresent(Injectable.class))
                .filter(field -> !Modifier.isFinal(field.getModifiers()))
                .collect(Collectors.toList());

        scannerRepository.getFieldsToInject().addAll(fields);
    }
}
