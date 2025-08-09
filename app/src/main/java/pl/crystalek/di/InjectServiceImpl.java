package pl.crystalek.di;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.crystalek.di.injector.Injector;
import pl.crystalek.di.scanner.Scanner;
import pl.crystalek.di.scanner.ScannerRepository;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class InjectServiceImpl implements InjectService {
    @Getter
    ObjectRepository objectRepository = new ObjectRepositoryImpl();
    ScannerRepository scannerRepository = new ScannerRepository();
    ClassLoader classLoader;
    String packageName;
    Logger logger;

    @Override
    public void injectObjects() {
        final Set<Class<?>> classes = loadAllClasses();
        scanClasses(classes);
        inject();
    }

    private Set<Class<?>> loadAllClasses() {
        final InjectableClassLoader injectableClassLoader = new InjectableClassLoader(classLoader, packageName, logger);
        return injectableClassLoader.loadClasses();
    }

    private void scanClasses(final Set<Class<?>> classes) {
        final List<Scanner> scanners = Scanner.getScanners(classes, scannerRepository, logger);
        scanners.forEach(Scanner::scan);
    }

    private void inject() {
        final List<Injector> injectors = Injector.getInjectors(objectRepository, scannerRepository, logger);
        injectors.forEach(Injector::inject);
    }
}
