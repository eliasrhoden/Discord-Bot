import Brodda.BroddaBot;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;

public class Main {

    public static BroddaBot bot;

    public static void main(String[] args) {
        IDiscordClient client = createClient(args[0]);

        bot = new BroddaBot(client);

        getAudio();
        System.out.println("MUY BUENO");


    }

    public static IDiscordClient createClient(String token) throws DiscordException {
        ClientBuilder clientBuilder = new ClientBuilder(); // Creates the ClientBuilder instance
        clientBuilder.withToken(token); // Adds the login info to the builder

        return clientBuilder.build(); // Creates the client instance and logs the client in
    }


    private static AudioInputStream getAudio(){
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
