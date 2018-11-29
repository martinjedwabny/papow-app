package main.java;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import main.java.base.Alternative;

public class AlternativeTableData {
	private Alternative alternative; 
	private StringProperty name;

	public AlternativeTableData (Alternative alternative) {
		this.alternative = alternative;
		this.name = new SimpleStringProperty(alternative.getName());
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
