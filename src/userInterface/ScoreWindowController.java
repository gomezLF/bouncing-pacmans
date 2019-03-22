package userInterface;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import model.Game;

public class ScoreWindowController {
	
	private Game game;
	
    @FXML
    private ListView<String> ScoresListV;
    
    @FXML
    void initialize() {
    	showScores();
    }
    
    public void setGame(Game game) {
    	this.game = game;
    }
    
    public void showScores() {
    	ScoresListV.setItems(game.ArrayListToString());
    }
	
}
