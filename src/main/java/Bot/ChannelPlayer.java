package Bot;

import Bot.lavaplayer.AudioProvider;
import Bot.lavaplayer.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import sx.blah.discord.handle.audio.IAudioManager;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IVoiceChannel;

public class ChannelPlayer {

    /**
     * Play audio in the given channel in the given guild
     *
     * */

    private AudioPlayerManager playerManager;
    private AudioPlayer player;
    private TrackScheduler trackScheduler;
    private IVoiceChannel channel;
    private Thread idleTime;
    private final int PLAYER_FRAME_BUFFER = 30;
    private boolean playerKilled;

    public ChannelPlayer(IGuild guild, IVoiceChannel channel){

        channel.join();
        this.channel = channel;

        IAudioManager audioManager = guild.getAudioManager();

        playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);

        player = playerManager.createPlayer();
        trackScheduler = new TrackScheduler(player);

        AudioProvider provider = new AudioProvider(player);
        audioManager.setAudioProvider(provider);

        player.addListener(trackScheduler);
        player.setFrameBufferDuration(PLAYER_FRAME_BUFFER);
        playerKilled = false;
    }

    public IVoiceChannel getChannel() {
        return channel;
    }

    public void playSong(String youtubeUrl){
        playerManager.loadItem(youtubeUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                System.out.println("Track loaded:");
                System.out.println(track.getInfo().toString());
                trackScheduler.queue(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                for (AudioTrack track : playlist.getTracks()) {
                    System.out.println("Playlist loaded.");
                    trackScheduler.queue(track);
                }
            }

            @Override
            public void noMatches() {
                System.out.println("No matches!");
                // Notify the user that we've got nothing
            }

            @Override
            public void loadFailed(FriendlyException throwable) {
                System.out.println("Load failed!");
                // Notify the user that everything exploded
            }
        });

        idleTime = new Thread(new IdleTimer(this));
        idleTime.start();
    }

    public void pause(){
        if(!player.isPaused())
            player.setPaused(true);
    }

    public void resume(){
        if(player.isPaused())
            player.setPaused(false);
    }
    public void next(){
        trackScheduler.nextTrack();
    }

    public boolean playerBusy(){
        AudioTrack track = player.getPlayingTrack();
        return track != null && !player.isPaused();
    }

    public void shutdown(){
        channel.leave();
        playerManager.shutdown();
        playerKilled = true;
    }

    public boolean playerKilled(){
        return playerKilled;
    }

}
