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

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.StackPane;
import main.java.base.Question;
import main.java.base.Vote;
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
    private JFXTreeTableColumn<QuestionTreeTableData, String> ruleColumn;
    private JFXTreeTableColumn<QuestionTreeTableData, String> votersColumn;
    private Vector<JFXTreeTableColumn<QuestionTreeTableData, String>> rankingColumns;

    public void setSession(Session session) {
    	this.session = session;
    	setResultTreeTableView();
    }
    
    public void updateResults() {
    	setResultTreeTableItems();
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

	private void setResultTreeTableView() {
		setResultTreeTableColumns();
		setResultTreeTableItems();
		this.resultTreeTableView.group(this.questionNameColumn);
	}

	@SuppressWarnings("unchecked")
	private void setResultTreeTableItems() {
		Map<Question, List<Vote>> validVotes = this.session.getResult().getValidVotes();
		Map<Question, Map<VotingRule, Ballot>> results = this.session.getResult().getResults();
		ObservableList<QuestionTreeTableData> items = FXCollections.observableArrayList();
		for (Question question : this.session.getInput().getQuestions())
			for (VotingRule rule : this.session.getCommand().getRules())
				if (!validVotes.get(question).isEmpty())
					items.add(new QuestionTreeTableData(question, rule,
						validVotes.get(question).stream().map(Vote::getVoter).collect(Collectors.toSet()),
						results.get(question).get(rule)));
		this.resultTreeTableView.setRoot(new RecursiveTreeItem<QuestionTreeTableData>(items, RecursiveTreeObject::getChildren));
		this.resultTreeTableView.setShowRoot(false);
		this.resultTreeTableView.getRoot().getChildren().forEach(item -> item.setExpanded(true));
	}

	private void setResultTreeTableColumns() {
		questionNameColumn = new JFXTreeTableColumn<>("Question");
		setupCellValueFactory(questionNameColumn, QuestionTreeTableData::getQuestionName);
		ruleColumn = new JFXTreeTableColumn<>("Voting Rule");
		setupCellValueFactory(ruleColumn, QuestionTreeTableData::getRule);
		votersColumn = new JFXTreeTableColumn<>("Voters");
		Integer alternativeCount = this.session.getInput().getAlternatives().size();
		rankingColumns = new Vector<JFXTreeTableColumn<QuestionTreeTableData, String>>(alternativeCount);
		setupCellValueFactory(votersColumn, QuestionTreeTableData::getVoters);
		for (Integer i = 1; i <= alternativeCount; i++)
			addRankingColumnForRank(i);
		this.resultTreeTableView.getColumns().clear();
		this.resultTreeTableView.getColumns().add(questionNameColumn);
		this.resultTreeTableView.getColumns().add(ruleColumn);
		this.resultTreeTableView.getColumns().add(votersColumn);
		this.resultTreeTableView.getColumns().addAll(rankingColumns);
	}

	private void addRankingColumnForRank(Integer rank) {
		JFXTreeTableColumn<QuestionTreeTableData, String> rankColumn = new JFXTreeTableColumn<>(String.valueOf(rank));
		rankingColumns.add(rankColumn);
		setupCellValueFactory(rankColumn, q -> q.getAlternativesAtRank(rank));
	}
	
	private <S,T> void setupCellValueFactory(JFXTreeTableColumn<S, T> column, Function<S, ObservableValue<T>> mapper) {
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<S, T> param) -> {
            if (column.validateValue(param))
                return mapper.apply(param.getValue().getValue());
            else
                return column.getComputedValue(param);
        });
    }
}
