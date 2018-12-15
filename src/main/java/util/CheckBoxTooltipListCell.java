package main.java.util;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.util.Callback;

public class CheckBoxTooltipListCell<T> extends CheckBoxListCell<T> {
	
	private Callback<T, String> getTooltipString = null;

	public CheckBoxTooltipListCell(
            final Callback<T, ObservableValue<Boolean>> getSelectedProperty,
            final Callback<T, String> getTooltipString) {
		super(getSelectedProperty);
		this.getTooltipString = getTooltipString;
	}
	
	@Override public void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		if (getTooltipString  != null && item != null) {
			this.setTooltip(new Tooltip(getTooltipString.call(item)));
		}
	}
}
	
