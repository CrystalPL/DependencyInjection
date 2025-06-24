package pl.crystalek.di;

import lombok.extern.java.Log;

import java.util.Map;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        final Logger logger = Logger.getLogger("test");
        final ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        //Powinno wywalić error - circular dependency
        final InjectService circularDependencyService = InjectService.getInjectService(classLoader, "pl.crystalek.di.test.circular.dependency", logger);
        circularDependencyService.injectObjects();

        System.out.println("---------------------------");
        //Powinno utworzyć obiekty dla zwykłego przykładu
        final InjectService basicDependencyService = InjectService.getInjectService(classLoader, "pl.crystalek.di.test.basic", logger);
        basicDependencyService.injectObjects();

        System.out.println("---------------------------");
        //Powinno utworzyć obiekty dla zaawansowanego przykładu
        final InjectService advanceDependencyService = InjectService.getInjectService(classLoader, "pl.crystalek.di.test.advance", logger);
        advanceDependencyService.injectObjects();

        System.out.println("---------------------------");
        final InjectService factoryDependencyService = InjectService.getInjectService(classLoader, "pl.crystalek.di.test.factory", logger);
        factoryDependencyService.injectObjects();
    }
}
