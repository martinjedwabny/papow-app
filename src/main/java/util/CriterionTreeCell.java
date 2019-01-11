package main.java.util;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javafx.scene.control.TreeCell;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import main.java.viewModel.CommandCriterionViewModel;
import main.java.base.criterion.Criterion;
import main.java.base.criterion.CriterionAnd;
import main.java.base.criterion.CriterionEquals;
import main.java.base.criterion.CriterionOr;
import main.java.base.criterion.CriterionTrue;

public class CriterionTreeCell extends TreeCell<Criterion> {

	private static final String ROOT_MESSAGE = "Criteria: ";
	private static final String SELECTED_TOKEN = " <";
	
	private static final String FONT = "Fira Sans";
	private static final Integer FONT_SIZE = 13;
	private static final Color FAMILY_TEXT_COLOR = Color.web("#FF652F");
	private static final Color EQUALS_TEXT_COLOR = Color.web("0xffffff");
	private static final Color CATEGORY_TEXT_COLOR = Color.web("0xffffff");
	private static final Color CONNECTOR_TEXT_COLOR = Color.web("0xffffff");
	private static final Color EXTRA_TEXT_COLOR = Color.web("0xffffff");

	@Override
	protected void updateItem(Criterion item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setGraphic(null);
			return;
		}
		if (item != null) {
			updateNonRoot(item);
		} else if (this.getTreeItem() != null && ((CommandCriterionViewModel)this.getTreeItem()).isRoot()) {
			updateRoot();
		}
		updateSelectedToken();
	}
	
	@Override
	public void updateSelected(boolean selected) {
		super.updateSelected(selected);
		this.updateItem(this.getItem(), false);
	}

	private void updateSelectedToken() {
		if (isSelected() && this.getGraphic() instanceof TextFlow)
			((TextFlow)this.getGraphic()).getChildren().add(plainText(SELECTED_TOKEN));
	}

	private void updateRoot() {
		setGraphic(plainText(ROOT_MESSAGE));
	}

	private void updateNonRoot(Criterion item) {
		TextFlow textFlow = new TextFlow();
		textFlow.getChildren().addAll(getFormattedTextForCriterion(item));
		setGraphic(textFlow);
	}

	private List<Text> getFormattedTextForCriterion(Criterion item) {
		if (item instanceof CriterionEquals)
			return getFormattedTextForCriterionEquals(((CriterionEquals) item).getKey(), ((CriterionEquals) item).getValue());
		if (item instanceof CriterionOr)
			return getFormattedTextForCriterionRecursive(((CriterionOr) item).getSubcriteria(), "OR");
		if (item instanceof CriterionAnd)
			return getFormattedTextForCriterionRecursive(((CriterionAnd) item).getSubcriteria(), "AND");
		if (item instanceof CriterionTrue)
			return Arrays.asList(plainText("ANY"));
		return null;
	}

	private List<Text> getFormattedTextForCriterionEquals(String key, String value) {
		Text text1 = textWithColor(key, FAMILY_TEXT_COLOR);
		Text text2 = textWithColor("=", EQUALS_TEXT_COLOR);
		Text text3 = textWithColor(value, CATEGORY_TEXT_COLOR);
		return new Vector<Text>(Arrays.asList(text1,text2,text3));
	}

	private List<Text> getFormattedTextForCriterionRecursive(Vector<Criterion> subcriteria, String connector) {
		if (subcriteria == null)
			return null;
		if (subcriteria.isEmpty()) {
			Text text1 = plainText("(");
			Text text2 = textWithColor(connector, CONNECTOR_TEXT_COLOR);
			Text text3 = plainText(" EMPTY)");
			return new Vector<Text>(Arrays.asList(text1,text2,text3));
		} else if (subcriteria.size() == 1) {
			Text text1 = plainText("(");
			Text text2 = textWithColor(connector+" ", CONNECTOR_TEXT_COLOR);
			List<Text> ans = getFormattedTextForCriterion(subcriteria.firstElement());
			Text text3 = plainText(")");
			ans.add(0, text1);
			ans.add(1, text2);
			ans.add(text3);
			return ans;
		} else {
			Vector<Text> ans = new Vector<Text>();
			Text text1 = plainText("(");
			ans.add(text1);
			for (Criterion c : subcriteria) {
				ans.addAll(getFormattedTextForCriterion(c));
				if (!c.equals(subcriteria.lastElement())) {
					ans.add(textWithColor(" "+connector+" ", CONNECTOR_TEXT_COLOR));
				}
			}
			Text text2 = plainText(")");
			ans.add(text2);
			return ans;
		}
	}

	private Text plainText(String msg) {
		return textWithColor(msg, EXTRA_TEXT_COLOR);
	}
	
	private Text textWithColor(String msg, Color color) {
		Text text1 = new Text(msg);
		text1.setFill(color);
		text1.setFont(Font.font(FONT, FONT_SIZE));
		return text1;
	}
}
