package pl.crystalek.di.injector;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.crystalek.di.ObjectRepository;

import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Logger;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class FieldsInjector implements Injector {
    ObjectRepository objectRepository;
    Logger logger;
    List<Field> fieldsToInject;

    @Override
    public void inject() {
        fieldsToInject.forEach(this::injectField);
    }

    private void injectField(final Field field) {
        final boolean isEverythingOk = validateClasses(field);
        if (!isEverythingOk) {
            return;
        }

        final Object fieldObject = objectRepository.getObjectByClassName(field.getType());
        final Object classObject = objectRepository.getObjectByClassName(field.getDeclaringClass());

        tryInjectField(field, classObject, fieldObject);
    }

    private boolean validateClasses(final Field field) {
        final Class<?> representingFieldClass = field.getDeclaringClass();
        if (!objectRepository.isObjectExists(representingFieldClass)) {
            logger.severe("Not found representing object class: " + representingFieldClass.getName() + " for field: " + field.getName());
            return false;
        }

        final Class<?> fieldObjectType = field.getType();
        if (!objectRepository.isObjectExists(fieldObjectType)) {
            logger.severe("Not found object for injected field: " + field.getName() + " in class:" + representingFieldClass.getName());
            return false;
        }

        return true;
    }

    private void tryInjectField(final Field field, final Object classObject, final Object fieldObject) {
        field.setAccessible(true);
        try {
            field.set(classObject, fieldObject);
        } catch (final IllegalAccessException exception) {
            exception.printStackTrace();
            logger.severe("Error while injecting field: " + field.getName() + " in class: " + field.getDeclaringClass().getName());
        }
    }
}
