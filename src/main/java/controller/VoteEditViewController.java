package main.java.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.java.base.Alternative;
import main.java.base.Category;
import main.java.base.CategoryFamily;
import main.java.base.Vote;
import main.java.base.ordering.Ballot;
import main.java.util.ComboBoxTableCell;

public class VoteEditViewController implements Initializable {

	private Vote vote;
	private ObservableList<Alternative> alternatives = FXCollections.observableArrayList();
	private ObservableList<CategoryFamily> families = FXCollections.observableArrayList();
	private Map<CategoryFamily, ObservableList<String>> categories = new HashMap<>();
	private ObservableList<String> ranks = FXCollections.observableArrayList();
	private Map<Alternative, Integer> rankForElement = new HashMap<>();
	
    @FXML private TableView<Alternative> ballotTableView;
    @FXML private TableColumn<Alternative, String> alternativeColumn;
    @FXML private TableColumn<Alternative, String> rankingColumn;
    
    @FXML private TableView<CategoryFamily> categoryTableView;
    @FXML private TableColumn<CategoryFamily, String> familyColumn;
    @FXML private TableColumn<CategoryFamily, String> categoryColumn;
    
    public Map<CategoryFamily, Category> getCategories() {
    	return this.vote.getCategories();
    }
    
    public Ballot getBallot() {
    	Map<Alternative, Integer> ranksCorrect = new HashMap<>();
    	Map<Integer, Set<Alternative>> elementForRank = new HashMap<>();
    	rankForElement.forEach((k,v) -> {
    		if (!elementForRank.containsKey(v))
    			elementForRank.put(v, new HashSet<>());
    		elementForRank.get(v).add(k);
    	});
    	Integer rank = 1;
    	for (Map.Entry<Integer, Set<Alternative>> entry : elementForRank.entrySet()) {
    		for (Alternative a : entry.getValue())
    			ranksCorrect.put(a, rank);
    		rank += entry.getValue().size();
    	}
    	return new Ballot(ranksCorrect);
    }

	public void setupView(Vote vote, Vector<CategoryFamily> families) {
		this.vote = new Vote(vote);
		this.alternatives.setAll(vote.getRanking().getElements());
		this.families.setAll(families);
		this.categories.clear();
		for (CategoryFamily f : families) {
			this.categories.put(f, FXCollections.observableArrayList());
			this.categories.get(f).add("NONE");
			for (Category c : f.getPossibilities()) {
				this.categories.get(f).add(c.getDescription());
			}
		}
		this.ranks.clear();
		for (Integer i = 1; i <= vote.getRanking().getElementsCount(); i++)
			this.ranks.add(i.toString());
		rankForElement.clear();
		for (Alternative a : vote.getRanking().getElements())
			rankForElement.put(a, vote.getRanking().getRank(a));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setupBallotTableView();
		setupCategoryTableView();
	}

	private void setupCategoryTableView() {
		categoryTableView.setEditable(true);
		categoryTableView.setItems(families);
		familyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
		familyColumn.setEditable(false);
		categoryColumn.setCellValueFactory(cellData -> {
			Category c = vote.getCategories().getOrDefault(cellData.getValue(), null);
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
				this.vote.getCategories().put(family, c);
				return;
			}
		}
	}

	private void removeCategory(CategoryFamily family) {
		this.vote.getCategories().remove(family);
	}

	private void setupBallotTableView() {
		ballotTableView.setEditable(true);
		ballotTableView.setItems(alternatives);
		alternativeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
		alternativeColumn.setEditable(false);
		rankingColumn.setCellValueFactory(cellData -> new SimpleStringProperty(vote.getRanking().getRank(cellData.getValue()).toString()));
		rankingColumn.setCellFactory(column ->
				new ComboBoxTableCell<Alternative>(item -> this.ranks));
		rankingColumn.setEditable(true);
		rankingColumn.setOnEditCommit(event -> {
			Alternative a = event.getRowValue();
			Integer rank = Integer.valueOf(event.getNewValue());
			rankForElement.put(a, rank);
		});
	}

}
