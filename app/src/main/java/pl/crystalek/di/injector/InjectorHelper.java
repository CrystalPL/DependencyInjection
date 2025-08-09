package pl.crystalek.di.injector;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.crystalek.di.ObjectRepository;
import pl.crystalek.di.annotations.Injectable;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class InjectorHelper {
    ObjectRepository objectRepository;
    ObjectCreator objectCreator;

    void createObjectFromClasses(final List<Class<?>> classes) {
        classes.stream()
                .filter(clazz -> !objectRepository.isObjectExists(clazz))
                .filter(clazz -> clazz.isAnnotationPresent(Injectable.class))
                .forEach(clazz -> objectCreator.createObject(clazz, clazz));
    }
}
