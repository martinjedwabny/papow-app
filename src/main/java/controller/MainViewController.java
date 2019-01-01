package main.java.controller;

import java.io.File;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.ResourceBundle;

import com.github.martinjedwabny.main.java.base.criterion.Criterion;
import com.github.martinjedwabny.main.java.base.criterion.CriterionOr;
import com.github.martinjedwabny.main.java.base.session.Session;
import com.github.martinjedwabny.main.java.base.session.SessionCommand;
import com.github.martinjedwabny.main.java.base.session.SessionRunner;
import com.github.martinjedwabny.main.java.io.reader.SessionReader;
import com.github.martinjedwabny.main.java.io.writer.SessionWriter;
import com.jfoenix.controls.JFXButton;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class MainViewController implements Initializable {
	/**
	 * Constants
	 */
	private static final String FILE_CHOOSER_LOAD_MESSAGE = "Open Resource File";
	private static final String FILE_CHOOSER_SAVE_MESSAGE = "Save Resource File";
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
	@FXML private TabPane tabPane;
	@FXML private Tab resultTab;
	@FXML private Tab commandTab;
    @FXML private JFXButton loadFileButton;
    @FXML private JFXButton saveFileButton;
	@FXML private InputTabViewController inputTabViewController;
	@FXML private CommandTabViewController commandTabViewController;
	@FXML private ResultTabViewController resultTabViewController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//@TODO get this out, just for testing
//		loadSession("src/main/ses/sample-1.ses");
    	loadSessionSuccess(new Session());
    	setTabPaneChange();
	}

    private void setTabPaneChange() {
		this.tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
	        @Override
	        public void changed(ObservableValue<? extends Tab> obs, Tab oldTab, Tab newTab) {
	            if (newTab.equals(resultTab))
	            	updateSessionResult();
	            if (newTab.equals(commandTab))
	            	updateSessionCommand();
	        }
	    });
	}

	private void updateSessionResult() {
		this.session.setResult(SessionRunner.generateResults(
				this.session.getInput(), 
				this.session.getCommand().getRules(), 
				this.session.getCommand().getCriteria()));
		this.resultTabViewController.updateResults();
	}

	private void updateSessionCommand() {
		this.commandTabViewController.setCriterionComboBoxes(this.session.getInput().getFamilies());
		this.commandTabViewController.setCriterionTreeView(this.session.getCommand().getCriteria());
	}

	@FXML
    private void loadFile(ActionEvent event) {
		showLoadWarning();
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle(FILE_CHOOSER_LOAD_MESSAGE);
    	fileChooser.getExtensionFilters().addAll(
    			new ExtensionFilter(SESSION_FILE_MESSAGE, "*.ses"),
    			new ExtensionFilter(JSON_FILE_MESSAGE, "*.json"));
    	File chosenFile = fileChooser.showOpenDialog((Stage) mainPane.getScene().getWindow());
    	if (chosenFile != null) {
    		loadSession(chosenFile.getAbsolutePath());
        } else {
        	loadSessionError();
        }
    }

	private void showLoadWarning() {
		Alert alert = new Alert(AlertType.WARNING, "Be sure to save your changes before loading.", ButtonType.OK);
		alert.getDialogPane().getStylesheets().setAll(this.mainPane.getStylesheets());
		alert.showAndWait();
	}
    
    private void loadSession(String fileInputPath) {
    	try {
			Session newSession = SessionReader.read(fileInputPath);
			loadSessionSuccess(newSession);
		} catch (Exception e) {
			e.printStackTrace();
			loadSessionError();
		}
    }

	private void loadSessionSuccess(Session newSession) {
		session.setInput(newSession.getInput());
		session.setCommand(newSession.getCommand());
		session.setResult(newSession.getResult());
		setCriterionToDefault(newSession.getCommand());
		inputTabViewController.setSession(session);
		commandTabViewController.setSession(session);
		resultTabViewController.setSession(session);
		updateSessionCommand();
		updateSessionResult();
	}

	private void setCriterionToDefault(SessionCommand command) {
		if (command.getCriteria() == null || command.getCriteria().isEmpty()) {
			command.setCriteria(new LinkedHashSet<Criterion>());
			command.getCriteria().add(new CriterionOr());
		}
	}

	private void loadSessionError() {
		// TODO Auto-generated method stub
    	
    }
	
	@FXML void saveFile(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle(FILE_CHOOSER_SAVE_MESSAGE);
    	fileChooser.getExtensionFilters().add(
    			new ExtensionFilter(SESSION_FILE_MESSAGE, "*.ses"));
    	File chosenFile = fileChooser.showSaveDialog((Stage) mainPane.getScene().getWindow());
    	if (chosenFile != null) {
            try {
                SessionWriter.write(this.session, chosenFile.getAbsolutePath());
            } catch (Exception ex) {
            	saveSessionError();
            }
        }
    }

	private void saveSessionError() {
		// TODO Auto-generated method stub
		
	}

}