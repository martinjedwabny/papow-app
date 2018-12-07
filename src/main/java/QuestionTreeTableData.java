package main.java;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import main.java.base.Alternative;
import main.java.base.Question;
import main.java.base.Voter;
import main.java.base.ordering.Ballot;
import main.java.base.rules.VotingRule;

public class QuestionTreeTableData extends RecursiveTreeObject<QuestionTreeTableData> {
	private StringProperty questionName;
	private StringProperty rule;
	private StringProperty voters;
	private Map<Integer, StringProperty> alternativesPerRank;
	
	public QuestionTreeTableData(Question question, VotingRule rule, Set<Voter> voters, Ballot result) {
		super();
		this.questionName = new SimpleStringProperty(question.getDescription());
		this.rule = new SimpleStringProperty(rule.toString());
		this.voters = new SimpleStringProperty(voters.toString());
		this.alternativesPerRank = new LinkedHashMap<Integer, StringProperty>();
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
	
	
}
