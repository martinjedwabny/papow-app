package com.github.martinjedwabny.papow.app.java.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import com.github.martinjedwabny.papow.app.java.util.ComboBoxTableCell;
import com.github.martinjedwabny.papow.main.java.base.Alternative;
import com.github.martinjedwabny.papow.main.java.base.Vote;
import com.github.martinjedwabny.papow.main.java.base.ordering.Ballot;

public class VoteEditViewController implements Initializable {

	private Vote vote;
	private ObservableList<Alternative> alternatives = FXCollections.observableArrayList();
	private ObservableList<String> ranks = FXCollections.observableArrayList();
	private Map<Alternative, Integer> rankForElement = new HashMap<>();
	
    @FXML private TableView<Alternative> ballotTableView;
    @FXML private TableColumn<Alternative, String> alternativeColumn;
    @FXML private TableColumn<Alternative, String> rankingColumn;
    
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

	public void setupView(Vote vote) {
		this.vote = new Vote(vote);
		this.alternatives.setAll(vote.getRanking().getElements());
		this.ranks.clear();
		for (Integer i = 1; i <= vote.getRanking().getElementsCount(); i++)
			this.ranks.add(i.toString());
		rankForElement.clear();
		for (Alternative a : vote.getRanking().getElements())
			rankForElement.put(a, vote.getRanking().getRank(a));
		ballotTableView.getSortOrder().add(alternativeColumn);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setupBallotTableView();
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
