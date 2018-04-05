package Bot;

import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechAlternative;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.Transcript;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;

import javax.sound.sampled.*;
import java.io.InputStream;

/**
*
* For future functionality, Speech2Text
*
* */


public class Watson {

    private String userName;
    private String password;

    public Watson(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    public void runTest(InputStream au){
        SpeechToText service = new SpeechToText();
        service.setUsernameAndPassword(userName, password);

        // Signed PCM AudioFormat with 16kHz, 16 bit sample size, mono
        int sampleRate = 16000;
        AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Line not supported");
            System.exit(0);
        }

        AudioInputStream audio = new AudioInputStream(au,format,10);

        RecognizeOptions options = new RecognizeOptions.Builder()
                //.continuous(true)
                .interimResults(true)
                //.inactivityTimeout(5) // use this to stop listening when the speaker pauses, i.e. for 5s
                .contentType(HttpMediaType.AUDIO_RAW + "; rate=" + sampleRate)
                .build();

        service.recognizeUsingWebSocket(audio, options, new BaseRecognizeCallback() {
            @Override
            public void onTranscription(SpeechResults speechResults) {

                for(Transcript t:speechResults.getResults()){
                    if(t.isFinal()) {
                        for (SpeechAlternative spa : t.getAlternatives()) {
                            System.out.println(spa.getTranscript());
                        }
                    }
                }

            }
        });

        System.out.println("Listening to your voice for the next 30s...");
        try {
            Thread.sleep(30 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // closing the WebSockets underlying InputStream will close the WebSocket itself.


        System.out.println("Fin.");
    }

}
