package pl.crystalek.di.test.advance;

import lombok.AllArgsConstructor;
import pl.crystalek.di.annotations.Injectable;

@Injectable
@AllArgsConstructor
public class UserValidator {
    UserValidatorData userValidatorData;
}
