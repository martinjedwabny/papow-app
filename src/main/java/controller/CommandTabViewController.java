package main.java.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

import main.java.util.CheckBoxBiLabeledListCell;
import main.java.util.CriterionTreeCell;
import main.java.viewModel.CommandCriterionViewModel;
import main.java.viewModel.CommandVotingRuleViewModel;
import main.java.base.Category;
import main.java.base.CategoryFamily;
import main.java.base.criterion.Criterion;
import main.java.base.criterion.CriterionAnd;
import main.java.base.criterion.CriterionEquals;
import main.java.base.criterion.CriterionOr;
import main.java.base.criterion.CriterionTrue;
import main.java.base.rules.VotingRule;
import main.java.base.rules.iterative.InstantRunoff;
import main.java.base.rules.scoring.BordaFair;
import main.java.base.rules.scoring.BordaOptimistic;
import main.java.base.rules.scoring.BordaPessimistic;
import main.java.base.rules.scoring.Copeland;
import main.java.base.rules.scoring.KApproval;
import main.java.base.session.Session;

public class CommandTabViewController {
	
	private Session session;

    private ObservableList<CommandVotingRuleViewModel> votingRuleList = FXCollections.observableArrayList();

    @FXML private StackPane mainPane;
    @FXML private ListView<CommandVotingRuleViewModel> votingRuleListView;
    @FXML private TreeView<Criterion> criterionTreeView;
    @FXML private ComboBox<String> criterionTypeComboBox;
    @FXML private ComboBox<CategoryFamily> criterionFamilyComboBox;
    @FXML private ComboBox<Category> criterionCategoryComboBox;

	public void setSession(Session session) {
		this.session = session;
		setVotingRuleListView(session.getCommand().getRules());
		setCriterionTreeView(session.getCommand().getCriteria());
		setCriterionComboBoxes(session.getInput().getFamilies());
	}

	private void setVotingRuleListView(List<VotingRule> rules) {
		List<CommandVotingRuleViewModel> possibilities = Arrays.asList(
				new CommandVotingRuleViewModel(new BordaPessimistic()),
				new CommandVotingRuleViewModel(new BordaFair()),
				new CommandVotingRuleViewModel(new BordaOptimistic()),
				new CommandVotingRuleViewModel(new InstantRunoff()),
				new CommandVotingRuleViewModel(new Copeland()),
				new CommandVotingRuleViewModel(new KApproval(1)),
				new CommandVotingRuleViewModel(new KApproval(2)),
				new CommandVotingRuleViewModel(new KApproval(3)));
		rules.forEach(r -> possibilities.forEach(p -> {
			if (p.getRule().getClass().equals(r.getClass()) &&
					(!(p.getRule() instanceof KApproval) ||
						((KApproval) p.getRule()).getK().equals(((KApproval)r).getK()))) {
				p.setRule(r);
				p.setEnabled(true);
			}
		}));
		votingRuleList.setAll(possibilities);
		votingRuleListView.setCellFactory(new Callback<ListView<CommandVotingRuleViewModel>, ListCell<CommandVotingRuleViewModel>>() {
			@Override
			public ListCell<CommandVotingRuleViewModel> call(ListView<CommandVotingRuleViewModel> param) {
				return new CheckBoxBiLabeledListCell<CommandVotingRuleViewModel>(item -> {
					BooleanProperty cb = item.getEnabled();
					cb.addListener((obs,wasSelected,nowSelected) -> {
						if (nowSelected && !session.getCommand().getRules().contains(item.getRule()))
							session.getCommand().getRules().add(item.getRule());
						else if (!nowSelected)
							session.getCommand().getRules().remove(item.getRule());
					});
					return cb;
				}, item -> {
					return item.getDescription();
				});
			}
		});
		votingRuleListView.setItems(votingRuleList);
		votingRuleListView.prefHeightProperty().bind(Bindings.size(votingRuleListView.getItems()).multiply(28));
	}
	
	public void setCriterionTreeView(Set<Criterion> criteria) {
		TreeItem<Criterion> root = new CommandCriterionViewModel(criteria);
		this.criterionTreeView.setCellFactory(tableView -> new CriterionTreeCell());
		this.criterionTreeView.setRoot(root);
		this.criterionTreeView.setShowRoot(true);
	}

	public void setCriterionComboBoxes(Vector<CategoryFamily> families) {
		setCriterionTypeComboBox();
		setCriterionFamiliesComboBox(families);
		if (families.isEmpty())
			setCriterionCategoriesComboBox(null);
		else
			setCriterionCategoriesComboBox(this.criterionFamilyComboBox.getSelectionModel().getSelectedItem());
	}

	private void setCriterionCategoriesComboBox(CategoryFamily selectedItem) {
		if (selectedItem == null) {
			this.criterionCategoryComboBox.setItems(FXCollections.observableArrayList());
			return;
		}
		this.criterionCategoryComboBox.setItems(FXCollections.observableArrayList(selectedItem.getPossibilities()));
		if (!this.criterionCategoryComboBox.getItems().isEmpty())
			this.criterionCategoryComboBox.getSelectionModel().selectFirst();
	}

	private void setCriterionFamiliesComboBox(Vector<CategoryFamily> families) {
		this.criterionFamilyComboBox.setItems(FXCollections.observableArrayList(families));
		if (!this.criterionFamilyComboBox.getItems().isEmpty())
			this.criterionFamilyComboBox.getSelectionModel().selectFirst();
		this.criterionFamilyComboBox.setOnAction(event -> {
			if (this.criterionFamilyComboBox.getSelectionModel().getSelectedItem() != null)
				setCriterionCategoriesComboBox(this.criterionFamilyComboBox.getSelectionModel().getSelectedItem());
		});
	}

	private void setCriterionTypeComboBox() {
		this.criterionTypeComboBox.setItems(FXCollections.observableArrayList(CommandCriterionViewModel.CRITERION_MESSAGES));
		if (!this.criterionTypeComboBox.getItems().isEmpty())
			this.criterionTypeComboBox.getSelectionModel().selectFirst();
		this.criterionTypeComboBox.setOnAction(event -> {
			updateComboBoxEnabledStatus();
		});
		updateComboBoxEnabledStatus();
	}

	private void updateComboBoxEnabledStatus() {
		if (!this.criterionTypeComboBox.getSelectionModel().isEmpty() && 
				this.criterionTypeComboBox.getSelectionModel().getSelectedItem().equals(CommandCriterionViewModel.CRITERION_EQUALS_MESSAGE)) {
			this.criterionFamilyComboBox.setDisable(false);
			this.criterionCategoryComboBox.setDisable(false);
		} else {
			this.criterionFamilyComboBox.setDisable(true);
			this.criterionCategoryComboBox.setDisable(true);
		}
	}

    @FXML
    private void addCriterion(MouseEvent event) {
    	CommandCriterionViewModel item = (CommandCriterionViewModel) this.criterionTreeView.getSelectionModel().getSelectedItem();
    	if (item == null || !item.canHaveChildren())
    		return;
    	Criterion criterion = null;
    	if (this.criterionTypeComboBox.getSelectionModel().getSelectedItem().equals(CommandCriterionViewModel.CRITERION_OR_MESSAGE))
    		criterion = new CriterionOr();
    	if (this.criterionTypeComboBox.getSelectionModel().getSelectedItem().equals(CommandCriterionViewModel.CRITERION_AND_MESSAGE))
    		criterion = new CriterionAnd();
    	if (this.criterionTypeComboBox.getSelectionModel().getSelectedItem().equals(CommandCriterionViewModel.CRITERION_TRUE_MESSAGE))
    		criterion = new CriterionTrue();
    	if (this.criterionTypeComboBox.getSelectionModel().getSelectedItem().equals(CommandCriterionViewModel.CRITERION_EQUALS_MESSAGE)) {
    		if (this.criterionFamilyComboBox.getSelectionModel().getSelectedItem() == null ||
    				this.criterionFamilyComboBox.getSelectionModel().getSelectedItem().getDescription().isEmpty() ||
    				this.criterionCategoryComboBox.getSelectionModel().getSelectedItem() == null ||
    	    		this.criterionCategoryComboBox.getSelectionModel().getSelectedItem().getDescription().isEmpty())
    			return;
    		else
    			criterion = new CriterionEquals(
    				this.criterionFamilyComboBox.getSelectionModel().getSelectedItem().getDescription(),
    				this.criterionCategoryComboBox.getSelectionModel().getSelectedItem().getDescription());
    	}
    	if (item.isRoot() && criterion != null)
    		this.session.getCommand().getCriteria().add(criterion);
    	item.addChild(criterion);
    }

    @FXML
    private void deleteCriterion(MouseEvent event) {
    	CommandCriterionViewModel item = (CommandCriterionViewModel) this.criterionTreeView.getSelectionModel().getSelectedItem();
    	if (item == null || item.isRoot())
    		return;
    	if (((CommandCriterionViewModel) item.getParent()).isRoot())
    		this.session.getCommand().getCriteria().remove(item.getCriterion());
    	((CommandCriterionViewModel) item.getParent()).removeChild(item);
    }

}
