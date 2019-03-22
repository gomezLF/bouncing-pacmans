package userInterface;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;
import model.Game;
import model.PacMan;
import threads.GUIUpdateControlThread;
import threads.PacManThread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import customExceptions.NoLoadLevelException;

public class GameWindowController {

    private List<Arc> arcs;

    private Game game;

    @FXML
    private Pane gameBoard;

    @FXML
    private Pane gameScorePane;

    @FXML
    private Menu loadGameMenu;

    @FXML
    private Label scoreLabel;

    @FXML
    void initialize() {
        gameBoard.setBackground(new Background(new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        gameScorePane.setBackground(gameBoard.getBackground());
        createMenuConfigurations();

        arcs = new ArrayList<Arc>();
        game = new Game();
        
        loadGame();
    }

    public double getWith(){
        return gameBoard.getWidth();
    }
    public double getHeight(){
        return gameBoard.getHeight();
    }
    public boolean paneIsEmpty() {
    	return gameBoard.getChildren().isEmpty();
    }

    @FXML
    void aboutGameClicked(ActionEvent event) {

    }

    @FXML
    void bestScoresClicked(ActionEvent event) {
    	Stage stage = new Stage();
    	
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ScoreWindow.fxml"));
        Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

        ScoreWindowController swc = loader.getController();
        swc.setGame(game);
        

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Best Scores");
        stage.show();
        
    }

    @FXML
    void exitClicked(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void saveGameClicked(ActionEvent event) {
    	gameWinner(2);
        try {
            game.saveScore(Game.SCORE_SERIALIZATION);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createPacman(){
        ArrayList<PacMan> pacmanList =(ArrayList<PacMan>) game.getPacmanList();
        scoreLabel.setText("0");
        
        if (pacmanList.size() != 0) {
            for (int i = 0; i < pacmanList.size(); i++) {
            	
                Arc arc = new Arc();
                arc.setRadiusX(pacmanList.get(i).getRadius());
                arc.setRadiusY(pacmanList.get(i).getRadius());
                arc.setStartAngle(45);
                arc.setLength(270);
                arc.setStroke(Color.BLACK);
                arc.setStrokeWidth(5);
                arc.setFill(Color.YELLOW);
                arc.setType(ArcType.ROUND);
                arc.setId("" + i);

                arc.setLayoutX(pacmanList.get(i).getPosX());
                arc.setLayoutY(pacmanList.get(i).getPosY());
                
                arc.setOnMouseClicked(new EventHandler<Event>() {

    				@Override
    				public void handle(Event event) {
    					pacmanList.get(Integer.parseInt(arc.getId())).setStopped(true);
    				}
    			});
                
                gameBoard.getChildren().add(arc);
                arcs.add(arc);

                PacManThread pt = new PacManThread(pacmanList.get(i), this);
                pt.setDaemon(true);
                pt.start();
            }
            GUIUpdateControlThread gut = new GUIUpdateControlThread(this);
            gut.setDaemon(true);
            gut.start();
            
		}
    }

    public void updateArc(){
        ArrayList<PacMan> pacmanList =(ArrayList<PacMan>) game.getPacmanList();
        
        for (int i = 0; i < pacmanList.size(); i++) {
        	if (!pacmanList.get(i).getStopped()) {

                switch (pacmanList.get(i).getDirection()){
                    case RIGHT:
                        arcs.get(i).setRotate(0.0);
                        break;
                    case LEFT:
                        arcs.get(i).setRotate(180.0);
                        break;
                    case DOWN:
                        arcs.get(i).setRotate(90.0);
                        break;
                    case UP:
                        arcs.get(i).setRotate(-90);
                        break;
                }
                arcs.get(i).setLayoutX(pacmanList.get(i).getPosX());
                arcs.get(i).setLayoutY(pacmanList.get(i).getPosY());
			}else {
				gameBoard.getChildren().remove(arcs.get(i));
				gameWinner(1);
			}
        }
        game.calculateScore();
        scoreLabel.setText("" + game.getScoreLevel());
    }
    
    

    private void createMenuConfigurations(){
        int counter = 0;
        while (counter < 3){

            MenuItem m = new MenuItem();
            m.setText("Level "+counter);
            m.setId("" + counter);
            m.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        createMenuEvent(m);
                        createPacman();
                    } catch (IOException e) {
                        Alert a = new Alert(Alert.AlertType.ERROR, "Caused by \n" + "the file to load the level has been deleted or moved to another destination", ButtonType.CLOSE);
                        a.setHeaderText("File not Found");
                        a.show();
                    }catch (NoLoadLevelException e) {
						e.message();
					}
                }
            });
            loadGameMenu.getItems().add(m);
            counter ++;
        }
    }

    private void createMenuEvent(MenuItem m) throws IOException, NoLoadLevelException{
    	if(gameBoard.getChildren().isEmpty()) {
        	
            switch (m.getId()){
                case "0":
                    game.loadGame(Game.GAME_CONFIGURATION_0_TXT);
                    break;
                case "1":
                    game.loadGame(Game.GAME_CONFIGURATION_1_TXT);
                    break;
                case "2":
                    game.loadGame(Game.GAME_CONFIGURATION_2_TXT);
                    break;
            }
        }else {
        	throw new NoLoadLevelException();
        }
    }

    private void loadGame(){
        try {
            game.loadScoreSaved(Game.SCORE_SERIALIZATION);
        }catch (IOException e){
            Alert a = new Alert(Alert.AlertType.ERROR, "Caused by \n" + "the file to load the score saved has been altered or moved to another destination", ButtonType.CLOSE);
            a.setHeaderText("File not Found");
            a.show();
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        createPacman();
    }
    
    private void gameWinner(int condition) {
    	
    	if (condition == 1) {
        	if (paneIsEmpty()) {
        		
    			TextInputDialog dialog = new TextInputDialog();
    			dialog.setTitle("You Win!!");
    			dialog.setHeaderText("Enter your name to save your score");
    			dialog.setContentText("Name: ");
    			
    			Optional<String> result = dialog.showAndWait();
    			result.ifPresent(name -> {
    				game.addScore(name, Integer.parseInt(scoreLabel.getText()));
    	        });
    		}
        	
		}else {
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("Save your Score");
			dialog.setHeaderText("Enter your name to save your score \n" + "NOTE: If your score is not low enough, such as to enter the Hall of Fame, it will not be saved.");
			dialog.setContentText("Please enter your name");
			
			Optional<String> result = dialog.showAndWait();
			result.ifPresent(name -> {
				game.addScore(name, Integer.parseInt(scoreLabel.getText()));
	        });
			
		}
    	
    }

}
