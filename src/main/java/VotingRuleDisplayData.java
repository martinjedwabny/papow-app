package main.java;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import main.java.base.rules.VotingRule;

public class VotingRuleDisplayData {
	private VotingRule rule;
	private StringProperty description;
	private BooleanProperty enabled;
	/**
	 * @param rule
	 * @param description
	 * @param enabled
	 */
	public VotingRuleDisplayData(VotingRule rule, String description) {
		super();
		this.rule = rule;
		this.description = new SimpleStringProperty(description);
		this.enabled = new SimpleBooleanProperty(false);
	}
	/**
	 * @return the rule
	 */
	public VotingRule getRule() {
		return rule;
	}
	/**
	 * @param rule the rule to set
	 */
	public void setRule(VotingRule rule) {
		this.rule = rule;
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
		this.description = new SimpleStringProperty(description);
	}
	/**
	 * @return enabled
	 */
	public BooleanProperty getEnabled() {
		return this.enabled;
	}
	/**
	 * @param enable state to set
	 */
	public void setEnabled(Boolean enabled) {
		this.enabled.set(enabled);;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.description.get();
	}
	
	
}
