package customExceptions;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class NoLoadLevelException extends Exception{
	
	public NoLoadLevelException() {
		super("For load a new level, you need to finish the current level");
	}
	
	public void message() {
		Alert alert = new Alert(Alert.AlertType.ERROR, "Caused by: \n" + "load a new level in the current level", ButtonType.CLOSE);
		alert.setHeaderText(super.getMessage());
		alert.show();
	}
	

}
