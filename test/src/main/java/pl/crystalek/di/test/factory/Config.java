package pl.crystalek.di.test.factory;

import lombok.Getter;
import pl.crystalek.di.Injectable;

@Getter
@Injectable
public class Config {
    AfkActionType afkActionType = AfkActionType.COMMAND;
}
