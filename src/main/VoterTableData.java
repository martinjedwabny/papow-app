package main;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import main.java.base.Voter;

public class VoterTableData {
	private Voter voter;
	public StringProperty name;

	public VoterTableData (Voter voter) {
		this.voter = voter;
		this.name = new SimpleStringProperty(voter.getName());
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
	
}
