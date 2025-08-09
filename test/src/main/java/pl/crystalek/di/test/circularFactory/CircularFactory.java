package pl.crystalek.di.test.circularFactory;

import pl.crystalek.di.annotations.Factory;

public class CircularFactory {

    @Factory
    private static CircularFactoryA getA(CircularFactoryC circularFactoryB) {
        return new CircularFactoryA();
    }

    @Factory
    private static CircularFactoryB getB(CircularFactoryA circularFactoryB) {
        return new CircularFactoryB();
    }

    @Factory
    private static CircularFactoryC getB(CircularFactoryB circularFactoryB) {
        return new CircularFactoryC();
    }

//    @Factory
//    private static CircularFactoryB getB() {
//        return new CircularFactoryB();
//    }
}
