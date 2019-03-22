package threads;

import javafx.scene.shape.Arc;
import model.PacMan;
import userInterface.GameWindowController;

import java.util.ArrayList;

public class PacManThread extends Thread{

    private PacMan pacMan;
    private GameWindowController gwc;
    private boolean moving;

    public PacManThread(PacMan pacMan, GameWindowController gwc) {
        this.pacMan = pacMan;
        this.gwc = gwc;
        moving = pacMan.getStopped();
    }

    @Override
    public void run(){
        while (!moving){
            pacMan.move(gwc.getWith(), gwc.getHeight());
            try {
                sleep(pacMan.getWaitTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
