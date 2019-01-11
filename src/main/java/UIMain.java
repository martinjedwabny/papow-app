package main.java;

import javax.swing.ImageIcon;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class UIMain extends Application {

	private final static String MAIN_VIEW_FXML_PATH = "/fxml/MainView.fxml";
	private final static String APP_TITLE = "PAPOW!";
	private final static String ICON_PATH = "/img/logo-only.png";

	@SuppressWarnings("restriction")
	@Override
    public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource(MAIN_VIEW_FXML_PATH));
        Scene scene = new Scene(root);
        stage.setTitle(APP_TITLE);
		stage.getIcons().add(new Image(ICON_PATH));
		// if (com.apple.eawt.Application.getApplication() != null) {
		// 	com.apple.eawt.Application.getApplication().setDockIconImage(new ImageIcon(getClass().getResource(ICON_PATH)).getImage());
		// }
        stage.setScene(scene);
        stage.show();
    }
	
	public static void main(String[] args) {
        launch(args);
    }

}
