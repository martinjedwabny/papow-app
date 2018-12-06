package main.java;

import java.util.HashMap;
import java.util.Map;
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
import main.java.base.Category;
import main.java.base.CategoryFamily;
import main.java.base.Question;
import main.java.base.Vote;
import main.java.base.ordering.Ballot;
import main.java.io.reader.BallotReader;
import main.java.io.reader.CategoryReader;

public class QuestionEditViewController {

    @FXML
    private JFXTextField descriptionTextField;
    @FXML
    private ListView<Alternative> alternativeListView;
    @FXML
    private TableView<VoteTableData> votesTableView;
    @FXML
    private TableColumn<VoteTableData, String> voterColumn;
    @FXML
    private TableColumn<VoteTableData, String> ballotColumn;
    @FXML
    private TableColumn<VoteTableData, String> categoryColumn;
    
    private Question questionCopy;
    private Map<String, Alternative> nametoAlternative;
    private Vector<CategoryFamily> categoriesCopy;
    
    private ObservableList<VoteTableData> votesTableList = FXCollections.observableArrayList();
    
    private HashMap<String, BooleanProperty> selectedAlternativeMap = new HashMap<String, BooleanProperty>();
    

	public Question getEditedQuestion() {
		return this.questionCopy;
	}
	
	public Vector<CategoryFamily> getEditedCategories () {
		return this.categoriesCopy;
	}
	
	public void setupView(Question question, Vector<Alternative> alternatives, Vector<CategoryFamily> categories) {
		this.questionCopy = new Question(question);
		this.nametoAlternative = alternatives.stream().collect(Collectors.toMap(Alternative::getName, a -> a));
		this.categoriesCopy = categories;
		setQuestionDescriptionField(this.questionCopy);
		setAlternativesListView(alternatives, this.questionCopy.getAlternatives());
		setVoteTableView(this.questionCopy.getVotes());
	}

	private void setQuestionDescriptionField(Question question) {
		descriptionTextField.setText(question.getDescription());
		descriptionTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			updateQuestionDescription(newValue);
		});
	}

	private void setAlternativesListView(Vector<Alternative> alternatives, Vector<Alternative> selectedAlternatives) {
		for (Alternative a : alternatives)
			selectedAlternativeMap.put(a.getName(), new SimpleBooleanProperty(false));
		for (Alternative a : selectedAlternatives)
			selectedAlternativeMap.put(a.getName(), new SimpleBooleanProperty(true));
		alternativeListView.setCellFactory(CheckBoxListCell.forListView(item -> {
			BooleanProperty cb = this.selectedAlternativeMap.get(item.getName());
			cb.addListener((obs,wasSelected,nowSelected) -> {
				if (nowSelected)
					addAlternativeToQuestion(item.getName());
				else
					removeAlternativeFromQuestion(item.getName());
			});
			return cb;
		}));
		alternativeListView.setItems(FXCollections.observableArrayList(alternatives));
		alternativeListView.prefHeightProperty().bind(Bindings.size(alternativeListView.getItems()).multiply(28));
	}

	private void setVoteTableView(Set<Vote> votes) {
		updateVoteTableViewItems(votes);
		votesTableView.setItems(votesTableList);
		voterColumn.setCellFactory(column -> EditCell.createStringEditCell());
		voterColumn.setCellValueFactory(cellData -> {
    		return cellData.getValue().getVoter();
    	});
		ballotColumn.setCellFactory(column -> EditCell.createStringEditCell());
		ballotColumn.setCellValueFactory(cellData -> {
    		return cellData.getValue().getRanking();
    	});
		ballotColumn.setOnEditCommit(event -> {
            String ballotString = event.getNewValue();
            Vote vote = ((VoteTableData) event.getTableView().getItems()
                    .get(event.getTablePosition().getRow())).getVote();
            updateVoteFromBallotString(ballotString, vote);
        });
		categoryColumn.setCellFactory(column -> EditCell.createStringEditCell());
		categoryColumn.setCellValueFactory(cellData -> {
    		return cellData.getValue().getCategories();
    	});
		categoryColumn.setOnEditCommit(event -> {
            String categoryString = event.getNewValue();
            Vote vote = ((VoteTableData) event.getTableView().getItems()
                .get(event.getTablePosition().getRow())).getVote();
            updateVoteFromCategoryString(categoryString, vote);
        });
	}

	private void updateVoteTableViewItems(Set<Vote> votes) {
		votesTableList.setAll(votes.stream().map(VoteTableData::new).collect(Collectors.toList()));
	}

	private void updateQuestionDescription(String description) {
		this.questionCopy.setDescription(description);
	}

	private void addAlternativeToQuestion(String alternative) {
		this.questionCopy.addAlternative(this.nametoAlternative.get(alternative));
		updateVoteTableViewItems(this.questionCopy.getVotes());
	}
	
	private void removeAlternativeFromQuestion(String alternative) {
		this.questionCopy.removeAlternative(this.nametoAlternative.get(alternative));
		updateVoteTableViewItems(this.questionCopy.getVotes());
	}

	private void updateVoteFromBallotString(String ballotString, Vote vote) {
		Ballot updatedBallot = BallotReader.fromString(ballotString, this.nametoAlternative);
		if (updatedBallot != null)
			vote.setRanking(updatedBallot);
		updateVoteTableViewItems(this.questionCopy.getVotes());
	}

	private void updateVoteFromCategoryString(String categoryString, Vote vote) {
		Map<CategoryFamily, Category> updatedCategories = CategoryReader.updateAndGetFromString(this.categoriesCopy, categoryString);
		if (updatedCategories != null)
			vote.setCategories(updatedCategories);
		updateVoteTableViewItems(this.questionCopy.getVotes());
	}

}
