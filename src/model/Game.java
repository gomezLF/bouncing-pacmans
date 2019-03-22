package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Game {

    public static final String SCORE_SERIALIZATION = "data/Score_Serialization.txt";
    public static final String GAME_CONFIGURATION_0_TXT = "data/Game_Configuration_0.txt";
    public static final String GAME_CONFIGURATION_1_TXT = "data/Game_Configuration_1.txt";
    public static final String GAME_CONFIGURATION_2_TXT = "data/Game_Configuration_2.txt";

    private List<PacMan> pacmanList;
    private Score[] scores;
    
    private int scoreLevel;

    public Game() {
    	scoreLevel = 0;
        pacmanList = new ArrayList<PacMan>();
        scores = new Score[10];
    }

    public List<PacMan> getPacmanList(){
        return pacmanList;
    }
    
    public Score[] getScores() {
    	return scores;
    }
    
	public int getScoreLevel() {
		return scoreLevel;
	}

    private void addPacman(int radius, double posX, double posY, int waitTime, String initialDirection, int rebounds, boolean stopped){
        PacMan.Direction direction = null;

        switch (initialDirection){
            case "RIGHT":
                direction = PacMan.Direction.RIGHT;
                break;
            case "LEFT":
                direction = PacMan.Direction.LEFT;
                break;
            case "UP":
                direction = PacMan.Direction.UP;
                break;
            case "DOWN":
                direction = PacMan.Direction.DOWN;
                break;
        }

        PacMan p = new PacMan(radius, posX, posY, waitTime, direction, rebounds, stopped);
        pacmanList.add(p);
    }
    
    public void addScore(String name, int score) {
    	Score s = new Score(score, name);
    	
    	for (int i = 0; i < scores.length; i++) {
			if (scores[9] == null) {
				if (scores[i] == null) {
					scores[i] = s;
				}
			}else if(scores[9] != null && s.getScore() < scores[9].getScore()){
				scores[9] = s;
			}
		}
    	organizeScores();
    }

    public void loadGame(String path) throws IOException {
    	pacmanList.clear();
    	this.scoreLevel = 0;
    	
        File file = new File(path);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String line = br.readLine();
        while (line != null){
            String[] words = line.split("\t");

            if (words[0].charAt(0) != '#'){
                int radius = Integer.parseInt(words[0]);
                double posX = Double.parseDouble(words[1]);
                double posY = Double.parseDouble(words[2]);
                int waitTime = Integer.parseInt(words[3]);
                String directin = words[4];
                int rebounds = Integer.parseInt(words[5]);
                boolean stopped = Boolean.parseBoolean(words[6]);

                addPacman(radius, posX, posY, waitTime, directin, rebounds, stopped);
            }
            line = br.readLine();
        }
        br.close();
        fr.close();
    }

    public void saveScore(String path)throws IOException{
        File file  = new File(path);
        
        if (file.exists()) {
        	file.delete();
		}
        
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeObject(scores);
        oos.close();
        fos.close();
    }

    public void loadScoreSaved(String path) throws IOException, ClassNotFoundException{
        File file = new File(path);

        if (file.exists()){
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            scores = (Score[]) ois.readObject();
            ois.close();
            fis.close();
        }
    }
    
    private void organizeScores() {
    	for (int i = 0; i < scores.length - 1; i++) {
			for (int j = 0; j < scores.length - i - 1; j++) {
				if (scores[j] != null && scores[j+1] != null) {
					if (scores[j + 1].getScore() < scores[j].getScore()) {
						Score aux = scores[j+1];
						scores[j+1] = scores[j];
						scores[j] = aux;
					}
				}
			}
		}
    }
    
    public void calculateScore() {
    	int score = scoreLevel;
    	int currentScore = 0;
    	
    	for (int i = 0; i < pacmanList.size(); i++) {
			if(pacmanList.get(i).getIsBouncing() && !pacmanList.get(i).getStopped()) {
				currentScore ++;
				pacmanList.get(i).setIsBouncing(false);
			}
		}
    	if(!(score + currentScore == scoreLevel)) {
    		scoreLevel += currentScore;
    	}
    	
    }
    
	public ObservableList<String> ArrayListToString() {
		organizeScores();
		
		ObservableList<String> data = FXCollections.observableArrayList("");
		for(int i=0;i<scores.length;i++) {
			if (scores[i] != null) {
				data.add("Score: " + scores[i].getScore() + "\t" + "Player name: " + scores[i].getName());
			}
		}
		
		return data;
	}

}
