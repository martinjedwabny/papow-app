package main.java;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import main.java.base.Vote;

public class VoteTableData {
	Vote vote;
	private StringProperty voter;
	private StringProperty ranking;
	private StringProperty categories;
	
	public VoteTableData(Vote vote) {
		this.vote = vote;
		this.voter = new SimpleStringProperty(vote.getVoter().getName());
		this.ranking = new SimpleStringProperty(vote.getRanking().toString());
		this.categories = new SimpleStringProperty(vote.getCategories().toString());
	}

	/**
	 * @return the voter
	 */
	public StringProperty getVoter() {
		return voter;
	}

	/**
	 * @param voter the voter to set
	 */
	public void setVoter(StringProperty voter) {
		this.voter = voter;
	}

	/**
	 * @return the ranking
	 */
	public StringProperty getRanking() {
		return ranking;
	}

	/**
	 * @param ranking the ranking to set
	 */
	public void setRanking(StringProperty ranking) {
		this.ranking = ranking;
	}

	/**
	 * @return the categories
	 */
	public StringProperty getCategories() {
		return categories;
	}

	/**
	 * @param categories the categories to set
	 */
	public void setCategories(StringProperty categories) {
		this.categories = categories;
	}
	
	
}
