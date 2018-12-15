package main.java.viewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import main.java.base.Question;

public class InputQuestionViewModel {
	private Question question;
    private StringProperty description;
    private StringProperty alternatives;
    private StringProperty votes;

	public InputQuestionViewModel(Question question) {
		this.question = question;
		this.description = new SimpleStringProperty(question.getDescription());
		this.alternatives = new SimpleStringProperty(question.getAlternatives().toString());
		this.votes = new SimpleStringProperty(question.getVotes().size()+" votes");
	}

	/**
	 * @return the question
	 */
	public Question getQuestion() {
		return question;
	}
	
	/**
	 * @param question the question to set
	 */
	public void setQuestion(Question question) {
		this.question = question;
	}

	/**
	 * @return the description
	 */
	public StringProperty getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description.set(description);
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(StringProperty description) {
		this.description = description;
	}

	/**
	 * @return the alternatives
	 */
	public StringProperty getAlternatives() {
		return alternatives;
	}

	/**
	 * @param alternatives the alternatives to set
	 */
	public void setAlternatives(StringProperty alternatives) {
		this.alternatives = alternatives;
	}

	/**
	 * @return the votes
	 */
	public StringProperty getVotes() {
		return votes;
	}

	/**
	 * @param votes the votes to set
	 */
	public void setVotes(StringProperty votes) {
		this.votes = votes;
	}
}