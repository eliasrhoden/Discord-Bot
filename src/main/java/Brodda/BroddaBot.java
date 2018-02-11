package Brodda;


import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;

public class BroddaBot {

    private IDiscordClient client;
    private EventDispatcher eventDispatcher;

    public BroddaBot(IDiscordClient client){
        this.client = client;
        client.login();
        while(!client.isReady());
        eventDispatcher = client.getDispatcher();

        eventDispatcher.registerListener(new PrivateMessageListener(client));


        System.out.println("Brodda is ready!");

    }




}
