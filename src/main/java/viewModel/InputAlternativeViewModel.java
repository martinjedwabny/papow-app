package main.java.viewModel;

import main.java.base.Alternative;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class InputAlternativeViewModel {
	private Alternative alternative; 
	private StringProperty name;

	public InputAlternativeViewModel (Alternative alternative) {
		this.alternative = alternative;
		this.name = new SimpleStringProperty(alternative.getName());
	}

	/**
	 * @return the alternative
	 */
	public Alternative getAlternative() {
		return alternative;
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
		this.alternative.setName(name);
		this.name.set(name);
	}
	
	
}
