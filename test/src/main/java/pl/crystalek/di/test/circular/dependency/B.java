package pl.crystalek.di.test.circular.dependency;

import lombok.AllArgsConstructor;
import pl.crystalek.di.Injectable;

@Injectable
@AllArgsConstructor
public class B {
    A a;
}
