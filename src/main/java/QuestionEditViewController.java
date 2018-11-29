package main.java;

import java.util.Vector;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.StringConverter;
import main.java.base.Alternative;
import main.java.base.Question;

public class QuestionEditViewController {

    @FXML
    private JFXTextField descriptionTextField;

    @FXML
    private JFXComboBox<Alternative> alternativeComboBox;

    @FXML
    private TableView<?> votesTableView;

    @FXML
    private TableColumn<?, ?> voterColumn;

    @FXML
    private TableColumn<?, ?> ballotColumn;
    
    private Question question;
    Vector<Alternative> alternatives;

	public QuestionEditViewController(Question question, Vector<Alternative> alternatives) {
		super();
		this.question = question;
		this.alternatives = alternatives;
	}

	@FXML
	public void initialize(){
		alternativeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
			
		});
		alternativeComboBox.setConverter(new StringConverter<Alternative>() {
			
			@Override
			public String toString(Alternative object) {
				return object.getName();
			}
			
			@Override
			public Alternative fromString(String string) {
				return new Alternative(string);
			}
		});
		alternativeComboBox.getItems().addAll(alternatives);
	}

	public Question getQuestion() {
		return question;
	}

}
