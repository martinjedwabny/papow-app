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
import main.java.base.criterion.CriterionOr;
import main.java.base.criterion.CriterionTrue;
import main.java.base.session.Session;
import main.java.base.session.SessionCommand;
import main.java.io.reader.SessionReader;

public class MainViewController implements Initializable {
	/**
	 * Constants
	 */
	private static final String FILE_CHOOSER_MESSAGE = "Open Resource File";
	private static final String JSON_FILE_MESSAGE = "JSON input file";
	private static final String SESSION_FILE_MESSAGE = "Session file";
	
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
	@FXML private CommandTabViewController commandTabViewController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//@TODO get this out, just for testing
    	loadSession("src/main/json/sample.json");
	}

    @FXML
    private void loadFile(ActionEvent event) {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle(FILE_CHOOSER_MESSAGE);
    	fileChooser.getExtensionFilters().addAll(
    			new ExtensionFilter(JSON_FILE_MESSAGE, "*.json"),
    			new ExtensionFilter(SESSION_FILE_MESSAGE, "*.ses"));
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
			session.setCommand(newSession.getCommand());
			session.setResult(newSession.getResult());
			setCriterionToDefault(newSession.getCommand());
	    	inputTabViewController.setSession(session);
	    	commandTabViewController.setSession(session);
		} catch (Exception e) {
			loadSessionError();
		}
    }

	private void setCriterionToDefault(SessionCommand command) {
		if (command.getCriterion() == null || command.getCriterion() instanceof CriterionTrue)
			command.setCriterion(new CriterionOr());
	}

	private void loadSessionError() {
		// TODO Auto-generated method stub
    	
    }
	
	@FXML void saveFile(ActionEvent event) {

    }

}