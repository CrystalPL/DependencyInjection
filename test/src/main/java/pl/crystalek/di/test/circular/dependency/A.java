package pl.crystalek.di.test.circular.dependency;

import lombok.AllArgsConstructor;
import pl.crystalek.di.annotations.Injectable;

@Injectable
@AllArgsConstructor
public class A {
    C c;
}
