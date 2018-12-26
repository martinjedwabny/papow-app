package main.java.util;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class CheckBoxBiLabeledListCell<T> extends CheckBoxListCell<T> {
	
	private Callback<T, String> getSubtitle = null;

	public CheckBoxBiLabeledListCell(
            final Callback<T, ObservableValue<Boolean>> getSelectedProperty,
            final Callback<T, String> getTooltipString) {
		super(getSelectedProperty);
		this.getSubtitle = getTooltipString;
	}
	
	@Override public void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		if (getSubtitle  != null && item != null) {
			if (this.getGraphic() != null) {
				this.setText("");
				HBox pane = new HBox();
				VBox subpane = new VBox();
				pane.getChildren().add(this.getGraphic());
				subpane.getChildren().add(getStylizedLabel(item.toString(), 13));
				subpane.getChildren().add(getStylizedLabel(this.getSubtitle.call(item), 11));
				pane.getChildren().add(subpane);
				this.setGraphic(pane);
				this.setContentDisplay(ContentDisplay.BOTTOM);
			}
		}
	}
	
	private Label getStylizedLabel(String msg, Integer textSize) {
		Label label = new Label(msg);
		label.setTextFill(Color.WHITE);
		label.setStyle("-fx-font: Fira Sans;");
		label.setStyle("-fx-font-size: "+textSize+"px");
		return label;
	}
}
	
