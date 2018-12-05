/**
 * 
 */
package main.java;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

import com.jfoenix.controls.JFXDialog;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import main.java.base.Alternative;
import main.java.base.CategoryFamily;
import main.java.base.Question;
import main.java.base.Voter;
import main.java.base.session.Session;

/**
 * @author martin
 *
 */
public class InputTabViewController implements Initializable {

	/**
	 * Data objects
	 */
	private Session session = new Session();

	/**
	 * TableView list objects
	 */
	private ObservableList<QuestionTableData> questions = FXCollections.observableArrayList();
	private ObservableList<AlternativeTableData> alternatives = FXCollections.observableArrayList();
	private ObservableList<VoterTableData> voters = FXCollections.observableArrayList();

	/**
	 * UI objects
	 */
    @FXML private StackPane mainPane;
    
    @FXML private TableView<QuestionTableData> questionsTableView;
    @FXML private TableColumn<QuestionTableData, String> questionDescriptionColumn;
    @FXML private TableColumn<QuestionTableData, String> questionAlternativesColumn;
    @FXML private TableColumn<QuestionTableData, String> questionVotesColumn;

    @FXML private TableView<AlternativeTableData> alternativesTableView;
    @FXML private TableColumn<AlternativeTableData, String> alternativeNameColumn;

    @FXML private TableView<VoterTableData> votersTableView;
    @FXML private TableColumn<VoterTableData, String> voterNameColumn;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
    	questionsTableView.setItems(questions);
    	alternativesTableView.setItems(alternatives);
    	votersTableView.setItems(voters);
    	setupQuestionTableView();
    	setupAlternativeTableView();
    	setupVotersTableView();
	}

	/**
	 * 
	 * 
	 * TableView Setup
	 * 
	 * 
	 */
	private void setupQuestionTableView() {
		questionsTableView.setRowFactory( tv -> {
			TableRow<QuestionTableData> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				showQuestionEditDialog(row.getItem().getQuestion());
			});
			return row ;
		});
		questionDescriptionColumn.setCellFactory(column -> EditCell.createStringEditCell());
    	questionDescriptionColumn.setCellValueFactory(cellData -> {
    		return cellData.getValue().getDescription();
    	});
    	questionAlternativesColumn.setCellValueFactory(cellData -> {
    		return cellData.getValue().getAlternatives();
    	});
    	questionVotesColumn.setCellValueFactory(cellData -> {
    		return cellData.getValue().getVotes();
    	});
	}

	private void setupAlternativeTableView() {
		alternativeNameColumn.setCellFactory(column -> EditCell.createStringEditCell());
		alternativeNameColumn.setCellValueFactory(cellData -> {
    		return cellData.getValue().getName();
    	});
		alternativeNameColumn.setOnEditCommit(event -> {
            String name = event.getNewValue();
            ((AlternativeTableData) event.getTableView().getItems()
                .get(event.getTablePosition().getRow())).setName(name);
        });
	}
	
	private void setupVotersTableView() {
		voterNameColumn.setCellFactory(column -> EditCell.createStringEditCell());
		voterNameColumn.setCellValueFactory(cellData -> {
    		return cellData.getValue().getName();
    	});
		voterNameColumn.setOnEditCommit(event -> {
            String name = event.getNewValue();
            ((VoterTableData) event.getTableView().getItems()
                .get(event.getTablePosition().getRow())).setName(name);
        });
	}

	/**
	 * 
	 * 
	 * Question edit handler (opens popup dialog)
	 * 
	 * 
	 */
	private void showQuestionEditDialog(Question question) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/fxml/QuestionEditView.fxml"));
			StackPane content = loader.load();
			QuestionEditViewController controller = (QuestionEditViewController) loader.getController();
			controller.setupView(question, this.session.getInput().getAlternatives(), this.session.getInput().getFamilies());
			DialogBuilder.showConfirmCancelDialog(content, this.mainPane, new EventHandler<ActionEvent>() {
			    @Override public void handle(ActionEvent e) {
			    	Question editedQuestion = controller.getEditedQuestion();
			    	Vector<CategoryFamily> editedCategories = controller.getEditedCategories();
			    	question.setDescription(editedQuestion.getDescription());
			    	question.setVotes(editedQuestion.getVotes());
			    	question.setAlternatives(editedQuestion.getAlternatives());
			    	session.getInput().setFamilies(editedCategories);
			    	setQuestionsFromSession(session);
			    }
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 
	 * Session setup (feed data to TableViews)
	 * 
	 * 
	 */
	public void setSession(Session session) {
		this.session = session;
		setQuestionsFromSession(session);
		setAlternativesFromSession(session);
		setVotersFromSession(session);
	}

	private void setVotersFromSession(Session session) {
		voters.clear();
		session.getInput().getVoters().forEach(voter -> {
			this.voters.add(new VoterTableData(voter));
		});
	}

	private void setAlternativesFromSession(Session session) {
		alternatives.clear();
		session.getInput().getAlternatives().forEach(alternative -> {
			this.alternatives.add(new AlternativeTableData(alternative));
		});
	}

	private void setQuestionsFromSession(Session session) {
		questions.clear();
		session.getInput().getQuestions().forEach(question -> {
			this.questions.add(new QuestionTableData(question));
		});
	}

	/**
	 * 
	 * 
	 * TableView Add/Delete Button handlers
	 * 
	 * 
	 */
    @FXML
    void addAlternative(MouseEvent event) {
    	Alternative a = new Alternative("Name");
    	AlternativeTableData item = new AlternativeTableData(a);
        alternativesTableView.getSelectionModel().clearSelection();
        alternativesTableView.getItems().add(item);
        alternativesTableView.getSelectionModel().select(
        		alternativesTableView.getItems().size() - 1, 
        		alternativesTableView.getFocusModel().getFocusedCell().getTableColumn());
        alternativesTableView.scrollTo(item);
        this.session.getInput().addAlternative(a);
    }

    @FXML
    void deleteAlternative(MouseEvent event) {
    	ObservableList<AlternativeTableData> selectedItems = alternativesTableView.getSelectionModel().getSelectedItems();
    	selectedItems.stream().forEach(item -> this.session.getInput().removeAlternative(item.getAlternative()));
		alternativesTableView.getItems().removeAll(selectedItems);
    	alternativesTableView.getSelectionModel().clearSelection();
    	setQuestionsFromSession(session);
    }

    @FXML
    void addQuestion(MouseEvent event) {
    	Question q = new Question("Name");
    	QuestionTableData item = new QuestionTableData(q);
        questionsTableView.getSelectionModel().clearSelection();
        questionsTableView.getItems().add(item);
        questionsTableView.getSelectionModel().select(
        		questionsTableView.getItems().size() - 1, 
        		questionsTableView.getFocusModel().getFocusedCell().getTableColumn());
        questionsTableView.scrollTo(item);
        this.session.getInput().addQuestion(q);
    }

    @FXML
    void deleteQuestion(MouseEvent event) {
    	ObservableList<QuestionTableData> selectedItems = questionsTableView.getSelectionModel().getSelectedItems();
    	selectedItems.stream().forEach(item -> this.session.getInput().removeQuestion(item.getQuestion()));
		alternativesTableView.getItems().removeAll(selectedItems);
    	questionsTableView.getSelectionModel().clearSelection();
    }

    @FXML
    void addVoter(MouseEvent event) {
    	Voter v = new Voter("Name");
		VoterTableData item = new VoterTableData(v);
        votersTableView.getSelectionModel().clearSelection();
        votersTableView.getItems().add(item);
        votersTableView.getSelectionModel().select(
        		votersTableView.getItems().size() - 1, 
        		votersTableView.getFocusModel().getFocusedCell().getTableColumn());
        votersTableView.scrollTo(item);
        this.session.getInput().addVoter(v);
    }

    @FXML
    void deleteVoter(MouseEvent event) {
    	ObservableList<VoterTableData> selectedItems = votersTableView.getSelectionModel().getSelectedItems();
    	selectedItems.stream().forEach(item -> this.session.getInput().removeVoter(item.getVoter()));
		votersTableView.getItems().removeAll(selectedItems);
    	votersTableView.getSelectionModel().clearSelection();
    	setQuestionsFromSession(session);
    }

}
