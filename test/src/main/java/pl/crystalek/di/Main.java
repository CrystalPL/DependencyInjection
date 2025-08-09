package pl.crystalek.di;

import pl.crystalek.di.test.extensionObject.ExtensionC;
import pl.crystalek.di.test.fields.TestField;

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

        System.out.println("---------------------------");
        final InjectService fieldDependencyService = InjectService.getInjectService(classLoader, "pl.crystalek.di.test.fields", logger);
        final TestField testField = new TestField();
        fieldDependencyService.getObjectRepository().addObject(testField);
        fieldDependencyService.injectObjects();
        System.out.println(testField.getTestingField());
        System.out.println(fieldDependencyService.getObjectRepository().getInjectableObjectsRegistry());

        System.out.println("---------------------------");
        final InjectService circularFactoryTest = InjectService.getInjectService(classLoader, "pl.crystalek.di.test.circularFactory", logger);
        circularFactoryTest.injectObjects();
        System.out.println(circularFactoryTest.getObjectRepository().getInjectableObjectsRegistry());

        System.out.println("---------------------------");
        final InjectService extensionObjectTest = InjectService.getInjectService(classLoader, "pl.crystalek.di.test.extensionObject", logger);
        extensionObjectTest.injectObjects();
        ExtensionC c = extensionObjectTest.getObjectRepository().getObjectByClassName(ExtensionC.class);
        System.out.println(c.toString());
    }

}
