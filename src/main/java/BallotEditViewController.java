package main.java;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.java.base.Alternative;
import main.java.base.Vote;
import main.java.base.ordering.Ballot;

public class BallotEditViewController {

	private Vote vote;
	
	private Ballot ballot;

    private ObservableList<Alternative> alternatives = FXCollections.observableArrayList();

    @FXML
    private TableView<Vote> ballotTableView;

    @FXML
    private TableColumn<Vote, Alternative> alternativeColumn;

    @FXML
    private TableColumn<Vote, Integer> rankingColumn;

	public void setVote(Vote vote) {
		this.vote = vote;
		this.ballot = new Ballot(vote.getRanking()); // Copy constructor
		setBallotTableView();
	}

	private void setBallotTableView() {
		
	}

}
