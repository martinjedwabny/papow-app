package main.java.viewModel;

import java.util.Map;

import main.java.base.Category;
import main.java.base.CategoryFamily;
import main.java.base.Voter;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class InputVoterViewModel {
	private Voter voter;
	public StringProperty name;
	public StringProperty categories;

	public InputVoterViewModel(Voter voter) {
		super();
		setVoter(voter);
	}

	/**
	 * @return the voter
	 */
	public Voter getVoter() {
		return voter;
	}

	/**
	 * @param voter the voter to set
	 */
	public void setVoter(Voter voter) {
		this.voter = voter;
		this.name = new SimpleStringProperty(voter.getName());
		this.categories = new SimpleStringProperty(voter.getCategories().toString());
	}

	/**
	 * @return the name
	 */
	public StringProperty getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.voter.setName(name);
		this.name.set(name);
	}

	/**
	 * @return the categories
	 */
	public StringProperty getCategories() {
		return categories;
	}

	/**
	 * @param categories to set
	 */
	public void setCategories(Map<CategoryFamily, Category> categories) {
		this.voter.setCategories(categories);
		this.categories.set(categories.toString());;
	}
	
}
