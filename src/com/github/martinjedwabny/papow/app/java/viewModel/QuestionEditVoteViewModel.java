package com.github.martinjedwabny.papow.app.java.viewModel;

import main.java.base.Vote;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class QuestionEditVoteViewModel {
	Vote vote;
	private StringProperty voter;
	private StringProperty ranking;
	
	public QuestionEditVoteViewModel(Vote vote) {
		this.vote = vote;
		this.voter = new SimpleStringProperty(vote.getVoter().getName());
		this.ranking = new SimpleStringProperty(vote.getRanking().toString());
	}

	/**
	 * @return the vote
	 */
	public Vote getVote() {
		return vote;
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
	
	
}
