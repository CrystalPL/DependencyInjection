package pl.crystalek.di.scanner;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
public abstract class Scanner {
    Set<Class<?>> allLoadedClasses;
    ScannerRepository scannerRepository;

    public static List<Scanner> getScanners(final Set<Class<?>> allLoadedClasses, final ScannerRepository scannerRepository, final Logger logger) {
        return Arrays.asList(
                new FactoryScanner(allLoadedClasses, scannerRepository),
                new InjectableClassesScanner(allLoadedClasses, scannerRepository, logger),
                new InjectableFieldsScanner(allLoadedClasses, scannerRepository)
        );
    }

    public abstract void scan();
}
