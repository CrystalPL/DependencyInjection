package pl.crystalek.di;

import lombok.NonNull;

import java.util.Map;

/**
 * Interface for repository of objects managed by the dependency injection container.
 * Stores all created class instances and provides methods for adding and retrieving them.
 * <p>
 * The repository is used internally by the DI mechanism for resolving dependencies
 * and by applications to retrieve created objects after container initialization.
 */
public interface ObjectRepository {

    /**
     * Adds an object to the repository under a specified class type.
     *
     * @param clazz  The class type under which the object will be available in the repository
     * @param object The object instance to add
     * @throws NullPointerException     if any parameter is null
     * @throws IllegalArgumentException if the class is not marked with {@link Injectable}
     */
    void addObject(@NonNull final Class<?> clazz, @NonNull final Object object) throws IllegalArgumentException;

    /**
     * Adds an object to the repository using its actual class type.
     *
     * @param object The object instance to add
     * @throws NullPointerException     if the object is null
     * @throws IllegalArgumentException if the class is not marked with {@link Injectable}
     */
    void addObject(@NonNull final Object object) throws IllegalArgumentException;

    /**
     * Retrieves an object from the repository based on class type.
     *
     * @param <T>   The type of the returned object
     * @param clazz The class of the object to retrieve
     * @return The object cast to the specified type
     * @throws NullPointerException if the class is null
     */
    <T> T getObjectByClassName(@NonNull final Class<T> clazz);

    /**
     * Returns a map of all managed objects.
     *
     * @return A map where the key is the class type and the value is the object instance
     */
    Map<Class<?>, Object> getInjectableObjectsRegistry();
}
