package main.java;

import java.util.Vector;

import javafx.scene.control.TreeItem;
import main.java.base.criterion.Criterion;
import main.java.base.criterion.CriterionAnd;
import main.java.base.criterion.CriterionEquals;
import main.java.base.criterion.CriterionOr;
import main.java.base.criterion.CriterionTrue;

public class CriterionTreeItem extends TreeItem<String> {

	private Criterion criterion;
	
	/**
	 * @param criterion
	 */
	public CriterionTreeItem(Criterion criterion) {
		super();
		this.criterion = criterion;
		this.setExpanded(true);
		if (criterion instanceof CriterionAnd)
			addChildrenTreeItems(((CriterionAnd) criterion).getSubcriteria());
		if (criterion instanceof CriterionOr)
			addChildrenTreeItems(((CriterionOr) criterion).getSubcriteria());
		String itemText = "";
		if (criterion instanceof CriterionTrue)
			itemText = "Any";
		if (criterion instanceof CriterionOr)
			itemText = "Any of:";
		if (criterion instanceof CriterionAnd)
			itemText = "All of:";
		if (criterion instanceof CriterionEquals)
			itemText = ((CriterionEquals) criterion).getKey() + " = " + ((CriterionEquals) criterion).getValue();
		this.setValue(itemText);
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
		if (getCriterion() instanceof CriterionAnd || getCriterion() instanceof CriterionOr) {
			this.getChildren().add(new CriterionTreeItem(subcriterion));
			if (getCriterion() instanceof CriterionAnd)
				((CriterionAnd) getCriterion()).addCriterion(subcriterion);
			if (getCriterion() instanceof CriterionOr)
				((CriterionOr) getCriterion()).addCriterion(subcriterion);
		}		
	}

	/**
	 * Remove subcriterion as TreeItem to children and criterion as child to
	 * the parent criterion
	 * @param subcriterion
	 */
	public void removeChild(CriterionTreeItem subcriterion) {
		if (getCriterion() instanceof CriterionAnd || getCriterion() instanceof CriterionOr) {
			this.getChildren().remove(subcriterion);
			if (getCriterion() instanceof CriterionAnd)
				((CriterionAnd) getCriterion()).getSubcriteria().remove(subcriterion.getCriterion());
			if (getCriterion() instanceof CriterionOr)
				((CriterionOr) getCriterion()).getSubcriteria().remove(subcriterion.getCriterion());
		}
	}
	
	/**
	 * @return whether the item is recursive
	 */
	public Boolean canHaveChlidren() {
		return this.getCriterion() instanceof CriterionAnd || this.getCriterion() instanceof CriterionOr;
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
		return getCriterion() instanceof CriterionEquals ||
				getCriterion() instanceof CriterionTrue ||
				(getCriterion() instanceof CriterionAnd && ((CriterionAnd) getCriterion()).getSubcriteria().isEmpty()) ||
				(getCriterion() instanceof CriterionOr && ((CriterionOr) getCriterion()).getSubcriteria().isEmpty());
	}
}
