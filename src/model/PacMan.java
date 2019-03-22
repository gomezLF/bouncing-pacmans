package model;

import java.io.Serializable;

public class PacMan implements Serializable {
	
	 private static final double ADVANCE = 5.0;
	 
	 
    public enum Direction {RIGHT, LEFT, UP, DOWN};
   

    private int radius;
    private double posX;
    private double posY;
    private int waitTime;
    private Direction direction;
    private int rebound;
    private boolean stopped;
    private boolean isBouncing;

    public PacMan(int radius, double posX, double posY, int waitTime, Direction direction, int rebound, boolean stopped) {
        this.radius = radius;
        this.posX = posX;
        this.posY = posY;
        this.waitTime = waitTime;
        this.direction = direction;
        this.rebound = rebound;
        this.stopped = stopped;
        this.isBouncing = false;
    }

    public int getRadius() {
        return radius;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getRebound() {
        return rebound;
    }

    public boolean getIsBouncing() {
        return isBouncing;
    }

    public void setIsBouncing(boolean bouncing) {
        isBouncing = bouncing;
    }

    public boolean getStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public void move(double w, double h){
        switch (direction){
            case RIGHT:
                if (posX > w - radius){
                    direction = Direction.LEFT;
                    isBouncing = true;
                   // rebound ++;
                    posX = w - radius;
                }else {
                    posX = posX + ADVANCE;
                }
                break;

            case LEFT:
                if (posX < radius){
                    direction = Direction.RIGHT;
                    isBouncing = true;
                    //rebound ++;
                    posX = radius;
                }else {
                    posX = posX - ADVANCE;
                }
                break;

            case UP:
                if (posY < radius){
                    direction = Direction.DOWN;
                    isBouncing = true;
                    //rebound ++;
                    posY = radius;
                }else {
                    posY = posY - ADVANCE;
                }
                break;

            case DOWN:
                if (posY > h - radius){
                    direction = Direction.UP;
                    isBouncing = true;
                    //rebound ++;
                    posY = h - radius;
                }else {
                    posY = posY + ADVANCE;
                }
        }
        rebounds();
    }
    
    private void rebounds() {
    	if(isBouncing) {
    		rebound ++;
    	}
    }
}
