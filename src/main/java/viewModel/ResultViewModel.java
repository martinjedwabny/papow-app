package main.java.viewModel;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import main.java.base.Question;
import main.java.base.Voter;
import main.java.base.criterion.Criterion;
import main.java.base.ordering.Ballot;
import main.java.base.rules.VotingRule;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ResultViewModel extends RecursiveTreeObject<ResultViewModel> {
	private StringProperty questionName;
	private StringProperty criterion;
	private StringProperty rule;
	private StringProperty voters;
	private Map<Integer, StringProperty> alternativesPerRank;
	
	public ResultViewModel(Question question, Criterion criterion, VotingRule rule, List<Voter> voters, Ballot result) {
		super();
		this.questionName = new SimpleStringProperty(question.getDescription());
		this.criterion = new SimpleStringProperty(criterion.toString());
		this.rule = new SimpleStringProperty(rule.toString());
		this.voters = new SimpleStringProperty(voters.toString());
		this.alternativesPerRank = new LinkedHashMap<Integer, StringProperty>();
		if (result == null)
			return;
		for (Integer rank : result.getRanks())
			this.alternativesPerRank.put(rank, new SimpleStringProperty(result.getElements(rank).toString()));
	}

	/**
	 * @return the questionName
	 */
	public StringProperty getQuestionName() {
		return questionName;
	}

	/**
	 * @return the rule
	 */
	public StringProperty getRule() {
		return rule;
	}

	/**
	 * @return the voters
	 */
	public StringProperty getVoters() {
		return voters;
	}

	/**
	 * @param rank
	 * @return the alternatives in the rank
	 */
	public StringProperty getAlternativesAtRank(Integer rank) {
		return this.alternativesPerRank.getOrDefault(rank, new SimpleStringProperty());
	}

	/**
	 * @return the criterion
	 */
	public StringProperty getCriterion() {
		return criterion;
	}
	
	
}
