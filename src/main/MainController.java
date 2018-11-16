package main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainController {
	
	

	public MainController(JFXButton loadFileButton, JFXTreeTableView<?> questionsTableView,
			JFXTreeTableView<?> alternativesTableView, JFXTreeTableView<?> votersTableView) {
		super();
		this.loadFileButton = loadFileButton;
		this.questionsTableView = questionsTableView;
		this.alternativesTableView = alternativesTableView;
		this.votersTableView = votersTableView;
		
		FairBorda fb = new FairBorda();
	}

	@FXML
    private JFXButton loadFileButton;

    @FXML
    private JFXTreeTableView<?> questionsTableView;

    @FXML
    private JFXTreeTableView<?> alternativesTableView;

    @FXML
    private JFXTreeTableView<?> votersTableView;

    @FXML
    void loadFile(ActionEvent event) {
    	System.out.print("Hola");
    }

}
