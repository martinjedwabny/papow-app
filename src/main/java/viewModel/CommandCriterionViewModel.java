package main.java.viewModel;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javafx.scene.control.TreeItem;
import main.java.base.criterion.Criterion;
import main.java.base.criterion.CriterionAnd;
import main.java.base.criterion.CriterionEquals;
import main.java.base.criterion.CriterionOr;
import main.java.base.criterion.CriterionTrue;

public class CommandCriterionViewModel extends TreeItem<Criterion> {

	public static final String CRITERION_OR_MESSAGE = "OR";
	public static final String CRITERION_AND_MESSAGE = "AND";
	public static final String CRITERION_EQUALS_MESSAGE = "EQUALS";
	public static final String CRITERION_TRUE_MESSAGE = "ANY";
	
	public static final List<String> CRITERION_MESSAGES = new ArrayList<String>(Arrays.asList(
			CRITERION_EQUALS_MESSAGE, CRITERION_OR_MESSAGE, CRITERION_AND_MESSAGE));

	private Criterion criterion;
	
	/**
	 * Header item
	 * @param criteria
	 */
	public CommandCriterionViewModel(Set<Criterion> criteria) {
		super();
		for (Criterion c : criteria) {
			CommandCriterionViewModel item = new CommandCriterionViewModel(c);
			item.setValue(c);
			this.getChildren().add(item);
			item.setExpanded(true);
		}
		this.criterion = null;
		this.setExpanded(true);
	}

	/**
	 * Recursive item
	 * @param criterion
	 */
	public CommandCriterionViewModel(Criterion criterion) {
		super();
		this.criterion = criterion;
		this.setExpanded(true);
		if (isAnd())
			addChildrenTreeItems(((CriterionAnd) criterion).getSubcriteria());
		if (isOr())
			addChildrenTreeItems(((CriterionOr) criterion).getSubcriteria());
		this.setValue(criterion);
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
	private void addChildrenTreeItems(Collection<Criterion> subcriteria) {
		for (Criterion criterion : subcriteria)
			this.getChildren().add(new CommandCriterionViewModel(criterion));
	}
	
	/**
	 * Add subcriterion as TreeItem to children and criterion as child to
	 * the parent criterion
	 * @param subcriterion
	 */
	public void addChild(Criterion subcriterion) {
		if (canHaveChildren()) {
			this.getChildren().add(new CommandCriterionViewModel(subcriterion));
			if (isAnd())
				((CriterionAnd) getCriterion()).addCriterion(subcriterion);
			if (isOr())
				((CriterionOr) getCriterion()).addCriterion(subcriterion);
		}
		updateValueString();
	}

	/**
	 * Remove subcriterion as TreeItem to children and criterion as child to
	 * the parent criterion
	 * @param subcriterion
	 */
	public void removeChild(CommandCriterionViewModel subcriterion) {
		if (canHaveChildren()) {
			this.getChildren().remove(subcriterion);
			if (isAnd())
				((CriterionAnd) getCriterion()).getSubcriteria().remove(subcriterion.getCriterion());
			if (isOr())
				((CriterionOr) getCriterion()).getSubcriteria().remove(subcriterion.getCriterion());
		}
		updateValueString();
	}
	
	private void updateValueString() {
		if (this.isRoot())
			return;
		this.setValue(criterion);
		if (this.getParent() != null && (this.getParent() instanceof CommandCriterionViewModel))
			((CommandCriterionViewModel) this.getParent()).updateValueString();
	}
	
	/**
	 * @return whether the item is recursive
	 */
	public Boolean canHaveChildren() {
		return this.isAnd() || this.isOr() || this.isRoot();
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

	public boolean isRoot() {
		return this.criterion == null;
	}
}
