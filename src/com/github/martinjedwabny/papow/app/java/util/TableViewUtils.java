package com.github.martinjedwabny.papow.app.java.util;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

public class TableViewUtils {
	public static void setAutomaticCellHeightResize(TableView<?> tableView, Integer cellHeight, Integer minHeight) {
		tableView.setFixedCellSize(cellHeight);
	    tableView.prefHeightProperty().bind(Bindings.max(tableView.fixedCellSizeProperty().multiply(Bindings.size(tableView.getItems()).add(1.01)), minHeight));
	    tableView.minHeightProperty().bind(tableView.prefHeightProperty());
	    tableView.maxHeightProperty().bind(tableView.prefHeightProperty());
	}

	public static <S, T> void setMultiLineCells(TableColumn<S,T> column) {
		column.setCellFactory(tc -> {
		    TableCell<S, T> cell = new TableCell<>();
		    Text text = new Text();
		    cell.setGraphic(text);
		    cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
		    text.wrappingWidthProperty().bind(column.widthProperty());
		    text.textProperty().bind(Bindings.convert(cell.itemProperty()));
		    return cell;
		});
	}

	public static void setMultiLineCells(TableView<?> tableView) {
		for (TableColumn<?,?> column : tableView.getColumns())
			setMultiLineCells(column);
	}
}
