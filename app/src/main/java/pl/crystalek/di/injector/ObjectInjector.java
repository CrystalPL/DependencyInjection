package pl.crystalek.di.injector;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class ObjectInjector implements Injector {
    List<Class<?>> classesToInject;
    InjectorHelper injectorHelper;

    @Override
    public void inject() {
        injectorHelper.createObjectFromClasses(classesToInject);
    }
}
