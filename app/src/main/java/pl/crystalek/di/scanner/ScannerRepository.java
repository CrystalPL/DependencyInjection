package pl.crystalek.di.scanner;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ScannerRepository {
    List<Class<?>> classesToInject;
    List<Method> factoryMethodsToInject;
    List<Field> fieldsToInject;

    public ScannerRepository() {
        this.classesToInject = new ArrayList<>();
        this.factoryMethodsToInject = new ArrayList<>();
        this.fieldsToInject = new ArrayList<>();
    }
}
