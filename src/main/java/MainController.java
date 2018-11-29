package main.java;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import main.java.base.session.Session;
import main.java.io.reader.SessionReader;

public class MainController implements Initializable {
	/**
	 * Constants
	 */
	private final String fileChooserMessage = "Open Resource File";
	private final String jsonFileMessage = "JSON input file";
	private final String sessionFileMessage = "Session file";
	
	/**
	 * Data objects
	 */
	private Session session = new Session();
	
	/**
	 * FXML window objects
	 */
	@FXML private AnchorPane mainPane;
    @FXML private JFXButton loadFileButton;
    @FXML private JFXButton saveFileButton;
	@FXML private InputTabViewController inputTabViewController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//@TODO get this out, just for testing
    	loadSession("src/main/json/sample.json");
	}

    @FXML
    private void loadFile(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle(fileChooserMessage);
    	fileChooser.getExtensionFilters().addAll(
    			new ExtensionFilter(jsonFileMessage, "*.json"),
    			new ExtensionFilter(sessionFileMessage, "*.ses"));
    	File chosenFile = fileChooser.showOpenDialog((Stage) mainPane.getScene().getWindow());
    	if (chosenFile != null) {
    		loadSession(chosenFile.getAbsolutePath());
        } else {
        	loadSessionError();
        }
    }
    
    private void loadSession(String fileInputPath) {
    	try {
			Session newSession = SessionReader.read(fileInputPath);
			session.setInput(newSession.getInput());
			session.getInput().getQuestions().add(session.getInput().getQuestions().get(0));
			session.setCommand(newSession.getCommand());
			session.setResult(newSession.getResult());
	    	inputTabViewController.setSession(session);
		} catch (Exception e) {
			loadSessionError();
		}
    }

	private void loadSessionError() {
		// TODO Auto-generated method stub
    	
    }
	
	@FXML void saveFile(ActionEvent event) {

    }

}