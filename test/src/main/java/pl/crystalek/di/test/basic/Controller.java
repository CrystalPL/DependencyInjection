package pl.crystalek.di.test.basic;

import lombok.AllArgsConstructor;
import pl.crystalek.di.annotations.Injectable;

@Injectable
@AllArgsConstructor
public class Controller {
    Service service;
}
