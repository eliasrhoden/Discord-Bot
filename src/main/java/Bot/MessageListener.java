package Bot;

import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;


public class MessageListener implements IListener<MessageReceivedEvent> {

    private BroddaBot bot;

    public MessageListener(BroddaBot b){
        bot = b;
    }

    @Override
    public void handle(MessageReceivedEvent event) {
        System.out.println("Message received");
        System.out.println(event.getMessage());

        String msg = event.getMessage().getContent();
        String[] parts = msg.split(" ");
        msg = parts[0];
        if(bot.isBotCmd(event.getMessage().getContent())) {
            try {
                bot.excecuteComand(event);
            } catch (Exception e) {
                System.out.println("Invalid command: " + msg);
                event.getChannel().sendMessage("U DO NOT KNO DAE COMANDS...");
            }
        }
    }



}
