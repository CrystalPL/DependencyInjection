package pl.crystalek.di.test.factory;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.crystalek.di.annotations.Injectable;

@Getter
@Injectable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Config {
    AfkActionType afkActionType = AfkActionType.COMMAND;
}
