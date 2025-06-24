package pl.crystalek.di.test.factory;

public class CommandAfkPunishment implements AfkPunishment {

    @Override
    public void execute(final String player) {
        System.out.println("command-afk-punishment");
    }
}
