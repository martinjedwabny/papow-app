package main.java;

import java.util.function.Function;

import com.jfoenix.controls.JFXTreeTableColumn;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeTableColumn;

public class JFXTreeTableViewUtils {
	public static <S,T> void setupCellValueFactory(JFXTreeTableColumn<S, T> column, Function<S, ObservableValue<T>> mapper) {
        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<S, T> param) -> {
            if (column.validateValue(param))
                return mapper.apply(param.getValue().getValue());
            else
                return column.getComputedValue(param);
        });
    }
}
