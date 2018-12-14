package main.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
    public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/main/fxml/MainView.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("PAPOW!");
        stage.getIcons().add(new Image("/main/img/logo-only.png"));
        stage.setScene(scene);
        stage.show();
    }
	
	public static void main(String[] args) {
        launch(args);
    }

}
