package main.java.util;

import java.util.function.Function;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.cells.editors.base.JFXTreeTableCell;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

public class JFXTreeTableViewUtils {
	public static <S,T> void setupCellValueFactory(JFXTreeTableColumn<S, T> column, Function<S, ObservableValue<T>> mapper) {
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<S, T> param) -> {
            if (column.validateValue(param))
                return mapper.apply(param.getValue().getValue());
            else
                return column.getComputedValue(param);
        });
    }
	
	public static <S,T> void setupMultilineCellFactory (JFXTreeTableColumn<S, T> column, Integer maxChars) {
		column.setCellFactory(new Callback<TreeTableColumn<S, T>, TreeTableCell<S, T>>() {
            @Override
            public TreeTableCell<S, T> call(TreeTableColumn<S, T> param) {
            	return new JFXTreeTableCell<S, T>() {
                    @Override
                    protected void updateItem(T item, boolean empty) {
                        if (!empty && item != null && !item.toString().isEmpty()) {
                		    this.setPadding(new Insets(2.0));
                		    String msg = item.toString();
	                		if (item.toString().length() > maxChars)
	                			msg = item.toString().substring(0, Math.min(maxChars, item.toString().length()))+"...";
                		    Text text = new Text();
                		    text.setFill(Color.WHITE);
                		    text.wrappingWidthProperty().bind(column.widthProperty());
                		    text.setText(msg);
                		    text.setTextAlignment(TextAlignment.CENTER);
                		    if (this.getDisclosureNode().isVisible())
                		    	text.wrappingWidthProperty().bind(this.widthProperty().subtract(30));
                		    else
                		    	text.wrappingWidthProperty().bind(this.widthProperty());
                		    setGraphic(text);
                        } else {
                            setGraphic(null);
                        }
                        setText(null);
                    }
                };
            }
        });
	}

	public static void setupMultilineCellFactory(JFXTreeTableView<?> table, int maxChars) {
		for (TreeTableColumn<?, ?> column : table.getColumns())
			setupMultilineCellFactory((JFXTreeTableColumn<?, ?>) column, maxChars);
	}
}
