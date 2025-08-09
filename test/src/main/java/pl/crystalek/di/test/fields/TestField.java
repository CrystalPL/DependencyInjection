package pl.crystalek.di.test.fields;

import lombok.Getter;
import pl.crystalek.di.annotations.Injectable;

public class TestField {
    @Injectable
    @Getter
    private TestingField testingField;
}
