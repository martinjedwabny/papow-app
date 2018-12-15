package main.java;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import main.java.base.Question;
import main.java.base.Vote;
import main.java.base.criterion.Criterion;
import main.java.base.ordering.Ballot;
import main.java.base.rules.VotingRule;
import main.java.base.session.Session;

public class ResultTabViewController {
	
	private Session session;

    @FXML
    private StackPane mainPane;
    
    @FXML
    private JFXTreeTableView<QuestionTreeTableData> resultTreeTableView;
    
    private JFXTreeTableColumn<QuestionTreeTableData, String> questionNameColumn;
    private JFXTreeTableColumn<QuestionTreeTableData, String> criterionColumn;
    private JFXTreeTableColumn<QuestionTreeTableData, String> ruleColumn;
    private JFXTreeTableColumn<QuestionTreeTableData, String> votersColumn;
    private Vector<JFXTreeTableColumn<QuestionTreeTableData, String>> rankingColumns;
    
    ObservableList<QuestionTreeTableData> items;


    public void setSession(Session session) {
    	this.session = session;
    	this.resultTreeTableView.setVisible(false);
    	this.resultTreeTableView.unGroup(this.questionNameColumn);
    	this.resultTreeTableView.setRoot(null);
    	new Thread(() -> {
    	       try {
    	          Thread.sleep(100); // Wait for 100 msec before updating
    	       } catch (InterruptedException e) {
    	          e.printStackTrace();
    	       }
    	       Platform.runLater(() -> {
    	       	setResultTreeTableColumns();
    	    	setResultTreeTableItems();
    	    	this.resultTreeTableView.group(this.questionNameColumn);
    			this.resultTreeTableView.getRoot().getChildren().forEach(item -> item.setExpanded(true));
    	    	this.resultTreeTableView.setVisible(true);
    	       });
    	}).start();
    }
    
    public void updateResults() {
    	setResultTreeTableItems();
    	if (rankingColumns != null) {
	    	Integer columnsOld = this.rankingColumns.size();
	    	Integer columnsNew = this.session.getInput().getAlternatives().size();
	    	while (columnsNew > columnsOld) {
	    		addRankingColumnForRank(this.rankingColumns.size()+1);
	    		this.resultTreeTableView.getColumns().add(this.rankingColumns.lastElement());
	    		columnsNew--;
	    	}
	    	while (columnsNew < columnsOld) {
	    		this.resultTreeTableView.getColumns().remove(this.rankingColumns.lastElement());
	    		this.rankingColumns.remove(this.rankingColumns.size()-1);
	    		columnsNew++;
	    	}
    	}
    }

	private void setResultTreeTableItems() {
		Map<Question, Map<Criterion, List<Vote>>> validVotes = this.session.getResult().getValidVotes();
		Map<Question, Map<Criterion, Map<VotingRule, Ballot>>> results = this.session.getResult().getResults();
		items = FXCollections.observableArrayList();
		for (Question question : this.session.getInput().getQuestions())
			for (Criterion criterion : this.session.getCommand().getCriteria())
				for (VotingRule rule : this.session.getCommand().getRules())
					if (!validVotes.get(question).get(criterion).isEmpty())
						items.add(new QuestionTreeTableData(question, criterion, rule,
								validVotes.get(question).get(criterion).stream().map(Vote::getVoter).collect(Collectors.toSet()),
								results.get(question).get(criterion).get(rule)));
		this.resultTreeTableView.setRoot(new RecursiveTreeItem<QuestionTreeTableData>(items, RecursiveTreeObject::getChildren));
		this.resultTreeTableView.setShowRoot(false);
		this.resultTreeTableView.getRoot().getChildren().forEach(item -> item.setExpanded(true));
	}

	private void setResultTreeTableColumns() {
		this.resultTreeTableView.getColumns().clear();
		
		this.questionNameColumn = new JFXTreeTableColumn<>("Question");
		JFXTreeTableViewUtils.setupCellValueFactory(questionNameColumn, QuestionTreeTableData::getQuestionName);
		this.resultTreeTableView.getColumns().add(questionNameColumn);

		this.criterionColumn = new JFXTreeTableColumn<>("Criterion");
		JFXTreeTableViewUtils.setupCellValueFactory(criterionColumn, QuestionTreeTableData::getCriterion);
		this.resultTreeTableView.getColumns().add(criterionColumn);

		this.ruleColumn = new JFXTreeTableColumn<>("Voting Rule");
		JFXTreeTableViewUtils.setupCellValueFactory(ruleColumn, QuestionTreeTableData::getRule);
		this.resultTreeTableView.getColumns().add(ruleColumn);
		
		this.votersColumn = new JFXTreeTableColumn<>("Voters");
		JFXTreeTableViewUtils.setupCellValueFactory(votersColumn, QuestionTreeTableData::getVoters);
		this.resultTreeTableView.getColumns().add(votersColumn);

		this.rankingColumns = new Vector<JFXTreeTableColumn<QuestionTreeTableData, String>>();
		if (this.session != null) {
			Integer alternativeCount = this.session.getInput().getAlternatives().size();
			for (Integer i = 1; i <= alternativeCount; i++)
				addRankingColumnForRank(i);
			this.resultTreeTableView.getColumns().addAll(rankingColumns);
		}
	}

	private void addRankingColumnForRank(Integer rank) {
		JFXTreeTableColumn<QuestionTreeTableData, String> rankColumn = new JFXTreeTableColumn<>(String.valueOf(rank));
		rankingColumns.add(rankColumn);
		JFXTreeTableViewUtils.setupCellValueFactory(rankColumn, q -> q.getAlternativesAtRank(rank));
	}
	
	
}
