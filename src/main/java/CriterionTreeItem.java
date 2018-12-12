package main.java;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javafx.scene.control.TreeItem;
import main.java.base.criterion.Criterion;
import main.java.base.criterion.CriterionAnd;
import main.java.base.criterion.CriterionEquals;
import main.java.base.criterion.CriterionOr;
import main.java.base.criterion.CriterionTrue;

public class CriterionTreeItem extends TreeItem<String> {

	public static final String CRITERION_OR_MESSAGE = "Any of:";
	public static final String CRITERION_AND_MESSAGE = "All of:";
	public static final String CRITERION_EQUALS_MESSAGE = "Equals";
	public static final String CRITERION_TRUE_MESSAGE = "Any";
	
	public static final List<String> CRITERION_MESSAGES = new ArrayList<String>(Arrays.asList(
			CRITERION_EQUALS_MESSAGE, CRITERION_OR_MESSAGE, CRITERION_AND_MESSAGE));

	private Criterion criterion;
	
	/**
	 * @param criterion
	 */
	public CriterionTreeItem(Criterion criterion) {
		super();
		this.criterion = criterion;
		this.setExpanded(true);
		if (isAnd())
			addChildrenTreeItems(((CriterionAnd) criterion).getSubcriteria());
		if (isOr())
			addChildrenTreeItems(((CriterionOr) criterion).getSubcriteria());
		this.setValue(this.getMessage());
	}
	
	public String getMessage() {
		if (isOr())
			return CRITERION_OR_MESSAGE;
		if (isAnd())
			return CRITERION_AND_MESSAGE;
		if (isEquals())
			return CRITERION_EQUALS_MESSAGE;
		return CRITERION_TRUE_MESSAGE;
	}

	private boolean isEquals() {
		return criterion instanceof CriterionEquals;
	}

	public boolean isAnd() {
		return criterion instanceof CriterionAnd;
	}

	public boolean isOr() {
		return criterion instanceof CriterionOr;
	}
	
	public boolean isTrue() {
		return criterion instanceof CriterionTrue;
	}

	/**
	 * Add subcriteria as TreeItem children recursively
	 * @param subcriteria
	 */
	private void addChildrenTreeItems(Vector<Criterion> subcriteria) {
		for (Criterion criterion : subcriteria)
			this.getChildren().add(new CriterionTreeItem(criterion));
	}
	
	/**
	 * Add subcriterion as TreeItem to children and criterion as child to
	 * the parent criterion
	 * @param subcriterion
	 */
	public void addChild(Criterion subcriterion) {
		if (isAnd() || isOr()) {
			this.getChildren().add(new CriterionTreeItem(subcriterion));
			if (isAnd())
				((CriterionAnd) getCriterion()).addCriterion(subcriterion);
			if (isOr())
				((CriterionOr) getCriterion()).addCriterion(subcriterion);
		}		
	}

	/**
	 * Remove subcriterion as TreeItem to children and criterion as child to
	 * the parent criterion
	 * @param subcriterion
	 */
	public void removeChild(CriterionTreeItem subcriterion) {
		if (isAnd() || isOr()) {
			this.getChildren().remove(subcriterion);
			if (isAnd())
				((CriterionAnd) getCriterion()).getSubcriteria().remove(subcriterion.getCriterion());
			if (isOr())
				((CriterionOr) getCriterion()).getSubcriteria().remove(subcriterion.getCriterion());
		}
	}
	
	/**
	 * @return whether the item is recursive
	 */
	public Boolean canHaveChlidren() {
		return this.isAnd() || this.isOr();
	}

	/**
	 * @return the criterion
	 */
	public Criterion getCriterion() {
		return criterion;
	}

	/* (non-Javadoc)
	 * @see javafx.scene.control.TreeItem#isLeaf()
	 */
	@Override
	public boolean isLeaf() {
		return isEquals() ||
				isTrue() ||
				(isAnd() && ((CriterionAnd) getCriterion()).getSubcriteria().isEmpty()) ||
				(isOr() && ((CriterionOr) getCriterion()).getSubcriteria().isEmpty());
	}
}
