package Bot;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

public interface BotAction {
    void run(MessageReceivedEvent event);
}
