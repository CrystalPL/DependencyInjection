package pl.crystalek.di.test.factory;

public class MessageAfkPunishment implements AfkPunishment{
    @Override
    public void execute(final String player) {
        System.out.println("message-afk-punishment");
    }
}
