package main.java;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class DialogBuilder {
	public static void showConfirmCancelDialog(Pane content, StackPane parent, EventHandler<ActionEvent> confirmHandler) {
		JFXDialog dialog = new JFXDialog(parent, content, JFXDialog.DialogTransition.CENTER);
		JFXButton confirmButton = new JFXButton("Confirm");
		confirmButton.setOnAction(confirmHandler);
		JFXButton cancelButton = new JFXButton("Cancel");
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		    	dialog.close();
		    }
		});
		HBox buttonsContainer = new HBox(10.0, confirmButton, cancelButton);
		buttonsContainer.setMaxHeight(20.0);
		buttonsContainer.setAlignment(Pos.CENTER);
		HBox.setMargin(confirmButton, new Insets(10.0));
		HBox.setMargin(cancelButton, new Insets(10.0));
		StackPane.setAlignment(buttonsContainer, Pos.BOTTOM_CENTER);
		content.getChildren().add(buttonsContainer);
		dialog.show();
	}
}
