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
        injectableObjectsRegistry.put(clazz, object);
    }

    @Override
    public void addObject(final @NonNull Object object) {
        addObject(object.getClass(), object);
    }

    @Override
    public <T> T getObjectByClassName(final @NonNull Class<T> clazz) {
        Object object = injectableObjectsRegistry.get(clazz);
        if (object == null) {
            object = findObjectInRootClass(clazz);
        }

        return (T) object;
    }

    private Object findObjectInRootClass(final Class<?> clazz) {
        return injectableObjectsRegistry.entrySet().stream()
                .filter(entry -> clazz.isAssignableFrom(entry.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean isObjectExists(final Class<?> clazz) {
        return injectableObjectsRegistry.containsKey(clazz);
    }
}
