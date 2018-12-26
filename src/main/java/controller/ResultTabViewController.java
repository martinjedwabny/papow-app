package main.java.controller;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import main.java.base.Question;
import main.java.base.Voter;
import main.java.base.criterion.Criterion;
import main.java.base.ordering.Ballot;
import main.java.base.rules.VotingRule;
import main.java.base.session.Session;
import main.java.util.JFXTreeTableViewUtils;
import main.java.util.TableViewUtils;
import main.java.viewModel.ResultViewModel;

public class ResultTabViewController {
	
	private Session session;

    @FXML
    private StackPane mainPane;
    
    @FXML
    private JFXTreeTableView<ResultViewModel> resultTreeTableView;
    
    private JFXTreeTableColumn<ResultViewModel, String> questionNameColumn;
    private JFXTreeTableColumn<ResultViewModel, String> criterionColumn;
    private JFXTreeTableColumn<ResultViewModel, String> ruleColumn;
    private JFXTreeTableColumn<ResultViewModel, String> votersColumn;
    private Vector<JFXTreeTableColumn<ResultViewModel, String>> rankingColumns;
    
    ObservableList<ResultViewModel> items;


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
		Map<Criterion, List<Voter>> validVoters = this.session.getResult().getValidVoters();
		Map<Question, Map<Criterion, Map<VotingRule, Ballot>>> results = this.session.getResult().getResults();
		items = FXCollections.observableArrayList();
		for (Question question : this.session.getInput().getQuestions())
			for (Criterion criterion : this.session.getCommand().getCriteria())
				for (VotingRule rule : this.session.getCommand().getRules())
					if (!validVoters.get(criterion).isEmpty())
						items.add(new ResultViewModel(question, criterion, rule,
								validVoters.get(criterion),
								results.get(question).get(criterion).get(rule)));
		this.resultTreeTableView.setRoot(new RecursiveTreeItem<ResultViewModel>(items, RecursiveTreeObject::getChildren));
		this.resultTreeTableView.setShowRoot(false);
		this.resultTreeTableView.getRoot().getChildren().forEach(item -> item.setExpanded(true));
	}

	private void setResultTreeTableColumns() {
		this.resultTreeTableView.getColumns().clear();
		
		this.questionNameColumn = new JFXTreeTableColumn<>("Question");
		JFXTreeTableViewUtils.setupCellValueFactory(questionNameColumn, ResultViewModel::getQuestionName);
		this.resultTreeTableView.getColumns().add(questionNameColumn);
		this.questionNameColumn.setMinWidth(80.0);

		this.criterionColumn = new JFXTreeTableColumn<>("Criterion");
		JFXTreeTableViewUtils.setupCellValueFactory(criterionColumn, ResultViewModel::getCriterion);
		this.resultTreeTableView.getColumns().add(criterionColumn);

		this.ruleColumn = new JFXTreeTableColumn<>("Voting Rule");
		JFXTreeTableViewUtils.setupCellValueFactory(ruleColumn, ResultViewModel::getRule);
		this.resultTreeTableView.getColumns().add(ruleColumn);
		
		this.votersColumn = new JFXTreeTableColumn<>("Voters");
		JFXTreeTableViewUtils.setupCellValueFactory(votersColumn, ResultViewModel::getVoters);
		this.resultTreeTableView.getColumns().add(votersColumn);

		this.rankingColumns = new Vector<JFXTreeTableColumn<ResultViewModel, String>>();
		if (this.session != null) {
			Integer alternativeCount = this.session.getInput().getAlternatives().size();
			for (Integer i = 1; i <= alternativeCount; i++)
				addRankingColumnForRank(i);
			this.resultTreeTableView.getColumns().addAll(rankingColumns);
		}
		
		JFXTreeTableViewUtils.setupMultilineCellFactory(this.resultTreeTableView, 40);
	}

	private void addRankingColumnForRank(Integer rank) {
		JFXTreeTableColumn<ResultViewModel, String> rankColumn = new JFXTreeTableColumn<>(String.valueOf(rank));
		rankingColumns.add(rankColumn);
		JFXTreeTableViewUtils.setupCellValueFactory(rankColumn, q -> q.getAlternativesAtRank(rank));
	}
	
	
}
