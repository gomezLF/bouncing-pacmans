package threads;

import userInterface.GameWindowController;

public class GUIUpdateControlRunnable extends Thread {

    private GameWindowController gwc;

    public GUIUpdateControlRunnable(GameWindowController gwc) {
        this.gwc = gwc;
    }

    @Override
    public void run(){
        gwc.updateArc();
    }
}
