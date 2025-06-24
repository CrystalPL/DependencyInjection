package pl.crystalek.di;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class ObjectRepositoryImpl implements ObjectRepository {
    Map<Class<?>, Object> injectableObjectsRegistry = new HashMap<>();

    @Override
    public void addObject(final @NonNull Class<?> clazz, final @NonNull Object object) {
        if (!clazz.isAnnotationPresent(Injectable.class)) {
            throw new IllegalArgumentException("Class " + clazz.getName() + " is not annotated with @Injectable");
        }

        injectableObjectsRegistry.put(clazz, object);
    }

    @Override
    public void addObject(final @NonNull Object object) throws IllegalArgumentException {
        final Class<?> clazz = object.getClass();
        if (!clazz.isAnnotationPresent(Injectable.class)) {
            throw new IllegalArgumentException("Class " + clazz.getName() + " is not annotated with @Injectable");
        }

        injectableObjectsRegistry.put(clazz, object);
    }

    @Override
    public <T> T getObjectByClassName(final @NonNull Class<T> clazz) {
        return (T) injectableObjectsRegistry.get(clazz);
    }

    boolean isObjectExists(final Class<?> clazz) {
        return injectableObjectsRegistry.containsKey(clazz);
    }
}
