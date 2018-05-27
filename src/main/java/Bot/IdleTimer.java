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
            sleepMinutes(TIME_OUT_MIN);
        }while(playerToClose.playerBusy());
        playerToClose.shutdown();
    }

    private void sleepMinutes(int minutes){
        long sleepTimeMins = minutes * 60 * 1000;
        try {
            Thread.sleep(sleepTimeMins);
        } catch (InterruptedException e) {
        }
    }
}
