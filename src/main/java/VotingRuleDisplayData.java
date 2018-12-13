package main.java;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import main.java.base.rules.VotingRule;
import main.java.base.rules.iterative.InstantRunoff;
import main.java.base.rules.scoring.BordaFair;
import main.java.base.rules.scoring.BordaOptimistic;
import main.java.base.rules.scoring.BordaPessimistic;
import main.java.base.rules.scoring.Copeland;
import main.java.base.rules.scoring.KApproval;

public class VotingRuleDisplayData {
	private VotingRule rule;
	private StringProperty name;
	private String description;
	private BooleanProperty enabled;
	/**
	 * @param rule
	 */
	public VotingRuleDisplayData(VotingRule rule) {
		super();
		this.rule = rule;
		this.name = new SimpleStringProperty(getNameForRule(rule));
		this.description = getDescriptionForRule(rule);
		this.enabled = new SimpleBooleanProperty(false);
	}
	
	private String getDescriptionForRule(VotingRule rule) {
		if (rule instanceof BordaPessimistic)
			return "Takes the amount of alternatives that are lesser, greater and equally ranked as itself\n" + 
					"as well as the rank of the alternative as outputs the score:\n" + 
					"score = (amount of lesser ranked alternatives)";
		if (rule instanceof BordaFair)
			return "Takes the amount of alternatives that are lesser, greater and equally ranked as itself\n" + 
					"as well as the rank of the alternative as outputs the score:\n" + 
					"score = (amount of lesser ranked alternatives) + (1 / (amount of equally ranked alternatives))";
		if (rule instanceof BordaOptimistic)
			return "Takes the amount of alternatives that are lesser, greater and equally ranked as itself\n" + 
					"as well as the rank of the alternative as outputs the score:\n" + 
					"score = (amount of lesser ranked alternatives) + (amount of equally ranked alternatives) - 1";
		if (rule instanceof InstantRunoff)
			return "For each iteration step, eliminate the alternatives (set) with the lowest amount of first places.\n" + 
					"When there are no more alternatives left, return the alternatives sorted by the reverse order in which\n" + 
					"they were eliminated.";
		if (rule instanceof Copeland)
			return "The score of each alternative is calculated as the amount of alternatives to which\n" + 
					"it is superior minus the amount to which it is inferior.";
		if (rule instanceof KApproval && ((KApproval) this.rule).getK() == 1)
			return "Largest amount of 1st places";
		if (rule instanceof KApproval && ((KApproval) this.rule).getK() == 2)
			return "Largest amount of top 2 places";
		if (rule instanceof KApproval && ((KApproval) this.rule).getK() == 3)
			return "Largest amount of top 3 places";
		return null;
	}

	private String getNameForRule(VotingRule rule) {
		if (rule instanceof BordaPessimistic)
			return "Pessimistic Borda";
		if (rule instanceof BordaFair)
			return "Fair Borda";
		if (rule instanceof BordaOptimistic)
			return "Optimistic Borda";
		if (rule instanceof InstantRunoff)
			return "Instant Runoff";
		if (rule instanceof Copeland)
			return "Copeland";
		if (rule instanceof KApproval && ((KApproval) this.rule).getK() == 1)
			return "Plurality";
		if (rule instanceof KApproval && ((KApproval) this.rule).getK() == 2)
			return "2-approval";
		if (rule instanceof KApproval && ((KApproval) this.rule).getK() == 3)
			return "3-approval";
		return null;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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
	 * @return the name
	 */
	public StringProperty getName() {
		return name;
	}
	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = new SimpleStringProperty(name);
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
		return this.name.get();
	}
	
	
}
