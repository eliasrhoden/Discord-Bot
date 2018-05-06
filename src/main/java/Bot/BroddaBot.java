package Bot;


import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;

import java.util.List;

public class BroddaBot {

    private IDiscordClient client;
    private EventDispatcher eventDispatcher;


    public BroddaBot(String discordToken){
        this.client = createClient(discordToken);

        eventDispatcher = client.getDispatcher();
        eventDispatcher.registerListener(new MessageListener(client));

        System.out.println("BRUDDA IS READY!");
    }

    private IDiscordClient createClient(String token) throws DiscordException {
        IDiscordClient client = null;
        ClientBuilder clientBuilder = new ClientBuilder(); // Creates the ClientBuilder instance
        clientBuilder.withToken(token); // Adds the login info to the builder

        client = clientBuilder.build();
        client.login();

        while(!client.isReady());
        return client;
    }


}
