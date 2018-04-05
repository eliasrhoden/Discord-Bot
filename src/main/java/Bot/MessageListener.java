package Bot;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
import java.util.HashMap;

public class MessageListener implements IListener<MessageReceivedEvent> {

    private IDiscordClient client;
    private HashMap<String, BotAction> botFunctions;
    private final String BOT_PREFIX = "/";
    private HashMap<IChannel,ChannelPlayer> channelPlayers;

    public MessageListener(IDiscordClient c){
        client = c;
        botFunctions = createBotFunctions();
        channelPlayers = new HashMap<>();
    }

    /**
     *  Here all bot functions are created and mapped to commands.
     * */

    public HashMap<String, BotAction> createBotFunctions(){
        HashMap<String, BotAction> functions = new HashMap<>();

        BotAction joinVoice = (event) -> {
            IVoiceChannel channel = findChannelWithUser(event.getAuthor());
            channel.join();
        };
        functions.put(BOT_PREFIX + "joinVoice",joinVoice);

        BotAction playJigga = (event) -> playSong("https://www.youtube.com/watch?v=TXgpwKfmsGg",event);
        functions.put(BOT_PREFIX + "jiggajigga",playJigga);

        BotAction playPitbull = (event) -> playSong("https://www.youtube.com/watch?v=Rfz6LMVnBVI",event);
        functions.put(BOT_PREFIX + "pitbull",playPitbull);

        BotAction bruddaTest = (event) -> event.getChannel().sendMessage("DO U KNO DAE WAE?");
        functions.put(BOT_PREFIX + "brudda",bruddaTest);

        BotAction skipToNextSOng= (event) -> nextSong(event);
        functions.put(BOT_PREFIX + "next",skipToNextSOng);

        BotAction playSong = (event) -> {
            String cmd = event.getMessage().getContent();
            String[] parts = cmd.split(" ");
            playSong(parts[1],event);
        };
        functions.put(BOT_PREFIX + "play",playSong);

        BotAction stopPlayer= (event) -> stopPlayer(event);
        functions.put(BOT_PREFIX + "stop",stopPlayer);

        BotAction helpFunction = (event) -> event.getChannel().sendMessage("DO U NEED DAE HELP?");
        functions.put(BOT_PREFIX + "help",helpFunction);

        return functions;
    }

    private void nextSong(MessageReceivedEvent event) {
        IChannel channel = event.getChannel();
        if(channelPlayers.containsKey(channel)){
            channelPlayers.get(channel).next();
        }
    }

    private void stopPlayer(MessageReceivedEvent event) {
        IChannel channel = event.getChannel();
        if(channelPlayers.containsKey(channel)){
            channelPlayers.get(channel).pause();
        }
    }

    private void playSong(String url, MessageReceivedEvent event){
        IChannel channel = event.getChannel();
        IVoiceChannel voiceChannel = findChannelWithUser(event.getAuthor());

        if(!channelPlayers.containsKey(channel)){
            ChannelPlayer player = new ChannelPlayer(event.getGuild(),voiceChannel);
            channelPlayers.put(channel,player);
        }
        channelPlayers.get(channel).playSong(url);
        channelPlayers.get(channel).resume();
    }

    @Override
    public void handle(MessageReceivedEvent event) {
        System.out.println("Message received");
        System.out.println(event.getMessage());

        String msg = event.getMessage().getContent();
        String[] parts = msg.split(" ");
        msg = parts[0];

        if(botFunctions.containsKey(msg)){
            System.out.println("Valid command: " + msg);
            BotAction action = botFunctions.get(msg);
            action.run(event);
            System.out.println("Action completed!");
        }else{
            System.out.println("Invalid command: " + msg);
            event.getChannel().sendMessage("U DO NOT KNO DAE COMANDS...");
        }
    }

    private IVoiceChannel findChannelWithUser(IUser u){
        IVoiceChannel rresult = null;

        for(IVoiceChannel v : client.getVoiceChannels()){
            System.out.println("LOOKING IN CHANNEL: " + v.getName());
            for(IUser user:v.getConnectedUsers()){
                System.out.println("= "+ user.getName());
                if(user.getName() == u.getName()){
                    rresult = v;
                    System.out.println("FOUND CHANNEL!");
                    break;
                }
            }
        }
        return rresult;
    }

}
