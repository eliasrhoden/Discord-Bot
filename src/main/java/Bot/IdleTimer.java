package Bot;

/**
 * Used to disconnect from voice channels after being idle for a given time
 * */

public class IdleTimer implements Runnable {

    private static final int TIME_OUT_MIN = 2;
    private ChannelPlayer playerToClose;

    public IdleTimer(ChannelPlayer player){
        playerToClose = player;
    }

    @Override
    public void run() {
        do {
            for (int i = 0; i < TIME_OUT_MIN; i++)
                sleepMinute();
        }while(playerToClose.playerBusy());
        playerToClose.shutdown();
    }

    private void sleepMinute(){
        long oneMinuteInMillis = 60 * 1000;
        try {
            Thread.sleep(oneMinuteInMillis);
        } catch (InterruptedException e) {
        }
    }
}
