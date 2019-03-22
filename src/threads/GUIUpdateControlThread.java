package threads;

import javafx.application.Platform;
import model.Game;
import userInterface.GameWindowController;

public class GUIUpdateControlThread extends Thread {

    private final static long UPDATE_SLEEP_TIME = 5;
    private GameWindowController gwc;

    public GUIUpdateControlThread(GameWindowController gwc) {
        this.gwc = gwc;
    }

    @Override
    public void run(){
        while (!gwc.paneIsEmpty()){
            GUIUpdateControlRunnable guiUpdateControlRunnable = new GUIUpdateControlRunnable(gwc);
            Platform.runLater(guiUpdateControlRunnable);

            try {
                sleep(UPDATE_SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
