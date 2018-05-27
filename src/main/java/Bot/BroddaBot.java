package Bot;



import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.DiscordException;

import java.util.HashMap;
import java.util.List;


public class BroddaBot {

    private IDiscordClient client;
    private EventDispatcher eventDispatcher;
    private HashMap<String, BotAction> botFunctions;
    private final char BOT_PREFIX = '/';
    private HashMap<IVoiceChannel,ChannelPlayer> channelPlayers;
    private JoinWatchK themeSongPlayer;

    public BroddaBot(String discordToken){
        this.client = createClient(discordToken);

        eventDispatcher = client.getDispatcher();
        eventDispatcher.registerListener(new MessageListener(this));

        botFunctions = createBotFunctions();
        channelPlayers = new HashMap<>();

        themeSongPlayer = new JoinWatchK(this);
        themeSongPlayer.start();

        System.out.println("BRUDDA IS READY!");
    }

    /**
     *  Here all bot functions are created and mapped to commands.
     * */

    // eye of the tiger http://www.math.chalmers.se/~palbin/Eye_of_the_Tiger.mp3

    public HashMap<String, BotAction> createBotFunctions(){
        HashMap<String, BotAction> functions = new HashMap<>();

        BotAction joinVoice = (event) -> {
            IVoiceChannel channel = findChannelWithUser(event.getAuthor());
            channel.join();
        };
        functions.put( "joinVoice",joinVoice);

        BotAction playJigga = (event) -> playSong("https://www.youtube.com/watch?v=TXgpwKfmsGg",findChannelWithUser(event.getAuthor()));
        functions.put( "jiggajigga",playJigga);

        BotAction playPitbull = (event) -> playSong("https://www.youtube.com/watch?v=Rfz6LMVnBVI",findChannelWithUser(event.getAuthor()));
        functions.put( "pitbull",playPitbull);

        BotAction bruddaTest = (event) -> event.getChannel().sendMessage("DO U KNO DAE WAE?");
        functions.put( "brudda",bruddaTest);

        BotAction skipToNextSOng= (event) -> nextSong(event);
        functions.put( "next",skipToNextSOng);

        BotAction playSong = (event) -> {
            String cmd = event.getMessage().getContent();
            String[] parts = cmd.split(" ");
            playSong(parts[1],findChannelWithUser(event.getAuthor()));
        };
        functions.put( "play",playSong);

        BotAction stopPlayer= (event) -> stopPlayer(event);
        functions.put( "stop",stopPlayer);

        BotAction helpFunction = (event) -> event.getChannel().sendMessage("DO U NEED DAE HELP?");
        functions.put("help",helpFunction);

        return functions;
    }

    public void excecuteComand(MessageReceivedEvent event) throws Exception{
        String cmd = event.getMessage().getContent().substring(1);
        cmd = cmd.split(" ")[0];
        if(botFunctions.containsKey(cmd)){
            BotAction action = botFunctions.get(cmd);
            action.run(event);
        }else{
            throw new Exception("Invalid cmd");
        }
    }

    public boolean isBotCmd(String msg){
        char firstChar = msg.charAt(0);
        return firstChar == BOT_PREFIX;
    }

    public boolean busyPlayingIn(IVoiceChannel channel){
        if(channelPlayers.containsKey(channel)){
            ChannelPlayer pl = channelPlayers.get(channel);
            return pl.playerBusy();
        }else
            return false;
    }

    public List<IVoiceChannel> getAllConnectedVoiceChannels(){
        return client.getVoiceChannels();
    }

    public ChannelPlayer getVoiceChannelPlayer(IVoiceChannel ch){
        return channelPlayers.get(ch);
    }

    private void nextSong(MessageReceivedEvent event) {
        IVoiceChannel channel = findChannelWithUser(event.getAuthor());
        if(channelPlayers.containsKey(channel)){
            channelPlayers.get(channel).next();
        }
    }

    private void stopPlayer(MessageReceivedEvent event) {
        IVoiceChannel channel = findChannelWithUser(event.getAuthor());
        if(channelPlayers.containsKey(channel)){
            channelPlayers.get(channel).pause();
        }
    }

    public void playSong(String url, IVoiceChannel channel){

        if(!channelPlayers.containsKey(channel)){
            ChannelPlayer player = new ChannelPlayer(channel.getGuild(),channel);
            channelPlayers.put(channel,player);
        }
        ChannelPlayer player = channelPlayers.get(channel);

        if(player.playerKilled()){
            channelPlayers.remove(channel);
            player = new ChannelPlayer(channel.getGuild(),channel);
            channelPlayers.put(channel,player);
        }

        channelPlayers.get(channel).playSong(url);
        channelPlayers.get(channel).resume();
    }

    public IVoiceChannel findChannelWithUser(IUser u){
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
