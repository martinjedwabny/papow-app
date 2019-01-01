package main.java.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import com.github.martinjedwabny.main.java.base.Category;
import com.github.martinjedwabny.main.java.base.CategoryFamily;
import com.github.martinjedwabny.main.java.base.Voter;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.java.util.ComboBoxTableCell;

public class VoterEditViewController implements Initializable {
	
	private Voter voter;

	private Map<CategoryFamily, ObservableList<String>> categories = new HashMap<>();
	private ObservableList<CategoryFamily> families = FXCollections.observableArrayList();

    @FXML private JFXTextField nameTextField;
    @FXML private TableView<CategoryFamily> categoryTableView;
    @FXML private TableColumn<CategoryFamily, String> familyColumn;
    @FXML private TableColumn<CategoryFamily, String> categoryColumn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setupCategoryTableView();
	}
	
	public String getVoterName() {
		return this.voter.getName();
	}
    
    public Map<CategoryFamily, Category> getVoterCategories() {
    	return this.voter.getCategories();
    }

	public void setupView(Voter voter, Vector<CategoryFamily> families) {
		this.voter = new Voter(voter);
		updateVoterNameTextField(this.voter);
		updateCategoryTableViewItems(this.voter, families);
	}
	
	private void updateVoterNameTextField(Voter voter) {
		nameTextField.setText(voter.getName());
		nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
			this.voter.setName(newValue);
		});
	}

	private void updateCategoryTableViewItems(Voter voter, Vector<CategoryFamily> families) {
		this.families.setAll(families);
		this.categories.clear();
		for (CategoryFamily f : families) {
			this.categories.put(f, FXCollections.observableArrayList());
			this.categories.get(f).add("NONE");
			for (Category c : f.getPossibilities()) {
				this.categories.get(f).add(c.getDescription());
			}
		}
	}


	private void setupCategoryTableView() {
		categoryTableView.setEditable(true);
		categoryTableView.setItems(families);
		familyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
		familyColumn.setEditable(false);
		categoryColumn.setCellValueFactory(cellData -> {
			Category c = voter.getCategories().getOrDefault(cellData.getValue(), null);
			return c == null ? new SimpleStringProperty("NONE") : new SimpleStringProperty(c.getDescription());
		});
		categoryColumn.setCellFactory(column -> 
			new ComboBoxTableCell<CategoryFamily>(item -> this.categories.get(item)));
		categoryColumn.setEditable(true);
		categoryColumn.setOnEditCommit(event -> {
			CategoryFamily family = event.getRowValue();
			String categoryString = event.getNewValue();
			if (categoryString.equals("NONE"))
				removeCategory(family);
			else
				updateCategory(family, categoryString);
		});
	}

	private void updateCategory(CategoryFamily family, String categoryString) {
		for (Category c : family.getPossibilities()) {
			if (c.getDescription().equals(categoryString)) {
				this.voter.getCategories().put(family, c);
				return;
			}
		}
	}

	private void removeCategory(CategoryFamily family) {
		this.voter.getCategories().remove(family);
	}

}
