package main.java;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import main.java.base.session.Session;

public class CommandTabViewController {
	
	private Session session;

    @FXML
    private StackPane mainPane;

    @FXML
    private ListView<?> votingRuleListView;

    @FXML
    private TreeView<?> criterionTreeView;

    @FXML
    void addCriterion(MouseEvent event) {

    }

    @FXML
    void deleteCriterion(MouseEvent event) {

    }

	public void setSession(Session session) {
		this.session = session;
	}

}
