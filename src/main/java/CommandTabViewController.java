package main.java;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import main.java.base.Category;
import main.java.base.CategoryFamily;
import main.java.base.criterion.Criterion;
import main.java.base.criterion.CriterionAnd;
import main.java.base.criterion.CriterionEquals;
import main.java.base.criterion.CriterionOr;
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

    private ObservableList<VotingRuleDisplayData> votingRuleList = FXCollections.observableArrayList();

    @FXML private StackPane mainPane;
    @FXML private ListView<VotingRuleDisplayData> votingRuleListView;
    @FXML private TreeView<String> criterionTreeView;
    @FXML private ComboBox<String> criterionTypeComboBox;
    @FXML private ComboBox<CategoryFamily> criterionFamilyComboBox;
    @FXML private ComboBox<Category> criterionCategoryComboBox;

	public void setSession(Session session) {
		this.session = session;
		setVotingRuleListView(session.getCommand().getRules());
		setCriterionTreeView(session.getCommand().getCriterion());
		setCriterionComboBoxes(session.getInput().getFamilies());
	}

	private void setVotingRuleListView(List<VotingRule> rules) {
		List<VotingRuleDisplayData> possibilities = Arrays.asList(
				new VotingRuleDisplayData(new BordaPessimistic()),
				new VotingRuleDisplayData(new BordaFair()),
				new VotingRuleDisplayData(new BordaOptimistic()),
				new VotingRuleDisplayData(new InstantRunoff()),
				new VotingRuleDisplayData(new Copeland()),
				new VotingRuleDisplayData(new KApproval(1)),
				new VotingRuleDisplayData(new KApproval(2)),
				new VotingRuleDisplayData(new KApproval(3)));
		rules.forEach(r -> possibilities.forEach(p -> {
			if (p.getClass().equals(r.getClass())) {
				p.setRule(r);
				p.setEnabled(true);
			}
		}));
		votingRuleList.setAll(possibilities);
		votingRuleListView.setCellFactory(new Callback<ListView<VotingRuleDisplayData>, ListCell<VotingRuleDisplayData>>() {
			@Override
			public ListCell<VotingRuleDisplayData> call(ListView<VotingRuleDisplayData> param) {
				return new CheckBoxTooltipListCell<VotingRuleDisplayData>(item -> {
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
	
	private void setCriterionTreeView(Criterion criterion) {
		TreeItem<String> root = new CriterionTreeItem(criterion);
		this.criterionTreeView.setRoot(root);
	}

	public void setCriterionComboBoxes(Vector<CategoryFamily> families) {
		setCriterionTypeComboBox();
		setCriterionFamiliesComboBox(families);
		setCriterionCategoriesComboBox(this.criterionFamilyComboBox.getSelectionModel().getSelectedItem());
	}

	private void setCriterionCategoriesComboBox(CategoryFamily selectedItem) {
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
		this.criterionTypeComboBox.setItems(FXCollections.observableArrayList(CriterionTreeItem.CRITERION_MESSAGES));
		if (!this.criterionTypeComboBox.getItems().isEmpty())
			this.criterionTypeComboBox.getSelectionModel().selectFirst();
		this.criterionTypeComboBox.setOnAction(event -> {
			updateComboBoxEnabledStatus();
		});
		updateComboBoxEnabledStatus();
	}

	private void updateComboBoxEnabledStatus() {
		if (!this.criterionTypeComboBox.getSelectionModel().isEmpty() && 
				this.criterionTypeComboBox.getSelectionModel().getSelectedItem().equals(CriterionTreeItem.CRITERION_EQUALS_MESSAGE)) {
			this.criterionFamilyComboBox.setDisable(false);
			this.criterionCategoryComboBox.setDisable(false);
		} else {
			this.criterionFamilyComboBox.setDisable(true);
			this.criterionCategoryComboBox.setDisable(true);
		}
	}

    @FXML
    private void addCriterion(MouseEvent event) {
    	CriterionTreeItem item = (CriterionTreeItem) this.criterionTreeView.getSelectionModel().getSelectedItem();
    	if (item == null || !item.canHaveChlidren())
    		return;
    	Criterion criterion = null;
    	if (this.criterionTypeComboBox.getSelectionModel().getSelectedItem().equals(CriterionTreeItem.CRITERION_OR_MESSAGE))
    		criterion = new CriterionOr();
    	if (this.criterionTypeComboBox.getSelectionModel().getSelectedItem().equals(CriterionTreeItem.CRITERION_AND_MESSAGE))
    		criterion = new CriterionAnd();
    	if (this.criterionTypeComboBox.getSelectionModel().getSelectedItem().equals(CriterionTreeItem.CRITERION_EQUALS_MESSAGE))
    		criterion = new CriterionEquals(
    				this.criterionFamilyComboBox.getSelectionModel().getSelectedItem().getDescription(),
    				this.criterionCategoryComboBox.getSelectionModel().getSelectedItem().getDescription());
    	item.addChild(criterion);
    }

    @FXML
    private void deleteCriterion(MouseEvent event) {
    	CriterionTreeItem item = (CriterionTreeItem) this.criterionTreeView.getSelectionModel().getSelectedItem();
    	if (item == null || this.criterionTreeView.getRoot() == item)
    		return;
    	((CriterionTreeItem) item.getParent()).removeChild(item);
    }

}
