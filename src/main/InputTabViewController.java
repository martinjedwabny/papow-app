/**
 * 
 */
package main;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
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


	private ObservableList<QuestionTableData> questions = FXCollections.observableArrayList();
	private ObservableList<AlternativeTableData> alternatives = FXCollections.observableArrayList();
	private ObservableList<VoterTableData> voters = FXCollections.observableArrayList();

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

	private void setupQuestionTableView() {
		questionsTableView.setRowFactory( tv -> {
			TableRow<QuestionTableData> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				QuestionTableData rowData = row.getItem();
				System.out.println(rowData);
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
            alternativesTableView.refresh();
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
            votersTableView.refresh();
        });
	}

	public void setSession(Session session) {
		this.session = session;
		questions.clear();
		session.getInput().getQuestions().forEach(question -> {
			this.questions.add(new QuestionTableData(question));
		});
		alternatives.clear();
		session.getInput().getAlternatives().forEach(alternative -> {
			this.alternatives.add(new AlternativeTableData(alternative));
		});
		voters.clear();
		session.getInput().getVoters().forEach(voter -> {
			this.voters.add(new VoterTableData(voter));
		});
	}

}
