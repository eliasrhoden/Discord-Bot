package Bot;

import sx.blah.discord.handle.audio.AudioEncodingType;
import sx.blah.discord.handle.audio.IAudioReceiver;
import sx.blah.discord.handle.audio.impl.AudioManager;
import sx.blah.discord.handle.obj.IUser;

import javax.sound.sampled.AudioInputStream;
import java.io.IOException;

import java.io.InputStream;
import java.util.ArrayDeque;

public class AudioReciv extends InputStream implements IAudioReceiver {

    ArrayDeque<Byte> buffer;

    public AudioReciv(){
        super();
        buffer = new ArrayDeque<Byte>();
    }

    @Override
    public void receive(byte[] bytes, IUser iUser, char c, int i) {
        System.out.println("Recived bytes");
        for(byte b:bytes)
            buffer.add(b);
    }

    @Override
    public AudioEncodingType getAudioEncodingType() {
        return AudioEncodingType.PCM;
    }

    @Override
    public int read() throws IOException {
        byte b = buffer.isEmpty() ? 0 : buffer.pollFirst();
        System.out.println("Polled byte:");
        return (int) b;
    }
}
