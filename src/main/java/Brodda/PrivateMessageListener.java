package Brodda;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.audio.IAudioManager;
import sx.blah.discord.handle.audio.IAudioProcessor;
import sx.blah.discord.handle.audio.IAudioProvider;
import sx.blah.discord.handle.audio.impl.AudioManager;
import sx.blah.discord.handle.audio.impl.DefaultProcessor;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.IVoiceState;
import sx.blah.discord.util.audio.AudioPlayer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class PrivateMessageListener implements IListener<MessageReceivedEvent> {

    private IDiscordClient client;

    public PrivateMessageListener(IDiscordClient c){
        client = c;
    }

    @Override
    public void handle(MessageReceivedEvent messageReceivedEvent) {
        System.out.println("MESSAGE RECIVED");
        IUser user =  messageReceivedEvent.getAuthor();
        System.out.println("User: " + user.getName());

        IVoiceChannel ch = findChannelWithUser(user);


        if(ch != null){
            ch.join();
            /*
            while(!ch.isConnected());
            AudioPlayer audioP = new AudioPlayer(ch.getGuild());
            AudioInputStream track = getAudio();
            while(!audioP.isReady());
            audioP.queue(track);
            audioP.setPaused(false);
            */
            // Get the AudioPlayer object for the guild
            AudioPlayer audioP = AudioPlayer.getAudioPlayerForGuild(ch.getGuild());



            AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
            AudioSourceManagers.registerRemoteSources(playerManager);

            com.sedmelluq.discord.lavaplayer.player.AudioPlayer player = playerManager.createPlayer();

            AudioLoadResultHandler hand = new AudioLoadResultHandler() {
                @Override
                public void trackLoaded(AudioTrack audioTrack) {
                    System.out.println("PALITNGA TRACK");
                    player.playTrack(audioTrack);
                }

                @Override
                public void playlistLoaded(AudioPlaylist audioPlaylist) {
                    System.out.println("FAILDED TO LAOD TRACK");
                }

                @Override
                public void noMatches() {
                    System.out.println("FAILDED TO LAOD TRACK");
                }

                @Override
                public void loadFailed(FriendlyException e) {
                    System.out.println("FAILDED TO LAOD TRACK");
                }
            };

            playerManager.loadItem("C:\\Users\\Elias\\IdeaProjects\\DiscordBot\\src\\main\\resources\\brodda1.wav",hand);


/*
            // Stop the playing track
            audioP.clear();

            // Play the found song
            try {
                audioP.queue(new File("C:\\Users\\Elias\\IdeaProjects\\DiscordBot\\src\\main\\resources\\brodda1.wav"));
            } catch (UnsupportedAudioFileException e) {
                System.out.println("ERROR PALTING SOGN");
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("ERROR PALTING SOGN");
                e.printStackTrace();
            }
*/

        }else{
            System.out.println("CHANNEL NOT FOUND!");
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
                    break;
                }
            }
        }

        return rresult;
    }

    private AudioInputStream getAudio(){
        File soundFile = null;
        AudioInputStream audioStream = null;
        try {
            soundFile = new File("C:\\Users\\Elias\\IdeaProjects\\DiscordBot\\src\\main\\resources\\brodda1.wav");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            audioStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (Exception e){
            e.printStackTrace();
        }
        return audioStream;
    }

}
