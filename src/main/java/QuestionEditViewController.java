package main.java;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXTextField;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxListCell;
import main.java.base.Alternative;
import main.java.base.Question;
import main.java.base.Vote;

public class QuestionEditViewController {

    @FXML
    private JFXTextField descriptionTextField;
    @FXML
    private ListView<String> alternativeListView;
    @FXML
    private TableView<VoteTableData> votesTableView;
    @FXML
    private TableColumn<VoteTableData, String> voterColumn;
    @FXML
    private TableColumn<VoteTableData, String> ballotColumn;
    @FXML
    private TableColumn<VoteTableData, String> categoryColumn;
    
    private ObservableList<VoteTableData> votesTableList = FXCollections.observableArrayList();
    private HashMap<String, BooleanProperty> selectedAlternativeMap = new HashMap<String, BooleanProperty>();

	public Question getEditedQuestion() {
		return null;
	}
	
	public void setupView(Question question, Vector<Alternative> alternatives) {
		setQuestion(question);
		setAlternatives(alternatives, question.getAlternatives());
		setVotes(question.getVotes());
	}

	private void setQuestion(Question question) {
		descriptionTextField.setText(question.getDescription());
	}

	private void setAlternatives(Vector<Alternative> alternatives, Vector<Alternative> selectedAlternatives) {
		for (Alternative a : alternatives)
			selectedAlternativeMap.put(a.getName(), new SimpleBooleanProperty(false));
		for (Alternative a : selectedAlternatives)
			selectedAlternativeMap.put(a.getName(), new SimpleBooleanProperty(true));
		alternativeListView.setCellFactory(CheckBoxListCell.forListView(item -> this.selectedAlternativeMap.get(item)));
		alternativeListView.setItems(FXCollections.observableArrayList(alternatives.stream().map(Alternative::getName).collect(Collectors.toList())));
		alternativeListView.prefHeightProperty().bind(Bindings.size(alternativeListView.getItems()).multiply(28));
	}
	
	private void setVotes(Set<Vote> votes) {
		votesTableList.setAll(votes.stream().map(VoteTableData::new).collect(Collectors.toList()));
		votesTableView.setItems(votesTableList);
		voterColumn.setCellFactory(column -> EditCell.createStringEditCell());
		voterColumn.setCellValueFactory(cellData -> {
    		return cellData.getValue().getVoter();
    	});
		ballotColumn.setCellFactory(column -> EditCell.createStringEditCell());
		ballotColumn.setCellValueFactory(cellData -> {
    		return cellData.getValue().getRanking();
    	});
		categoryColumn.setCellFactory(column -> EditCell.createStringEditCell());
		categoryColumn.setCellValueFactory(cellData -> {
    		return cellData.getValue().getCategories();
    	});
	}

}
