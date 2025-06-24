package pl.crystalek.di.test.factory;

import pl.crystalek.di.Factory;
import pl.crystalek.di.Injectable;

@Injectable
public interface AfkPunishment {

    @Factory
    static AfkPunishment getAfkPunishment(final Config config) {
        switch (config.afkActionType) {
            case COMMAND:
                return new CommandAfkPunishment();
            case MESSAGE:
                return new MessageAfkPunishment();
            default:
                return null;
        }
    }

    void execute(final String player);
}
