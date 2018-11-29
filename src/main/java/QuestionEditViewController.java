package main.java;

import java.util.Vector;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
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
    private JFXComboBox<StringProperty> alternativeComboBox;

    @FXML
    private TableView<?> votesTableView;

    @FXML
    private TableColumn<?, ?> voterColumn;

    @FXML
    private TableColumn<?, ?> ballotColumn;
    
    private Question question;
    private Vector<Alternative> alternatives;

	public Question getEditedQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		descriptionTextField.setText(question.getDescription());
	}

	public void setAlternatives(Vector<Alternative> alternatives) {
		this.alternatives = alternatives;
		alternativeComboBox.getItems().clear();
		for (Alternative a : alternatives)
			alternativeComboBox.getItems().add(new StringPropertyBase() {
				@Override
				public String getName() {
					return a.getName();
				}
				
				@Override
				public Object getBean() {
					// TODO Auto-generated method stub
					return null;
				}
			});
	}

}
