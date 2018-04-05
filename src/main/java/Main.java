import Bot.BroddaBot;

public class Main {

    public static void main(String[] args) {
        String discordToken = args[0];

        BroddaBot bot = new BroddaBot(discordToken);
    }
}
