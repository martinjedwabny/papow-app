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
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
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
				new VotingRuleDisplayData(new BordaPessimistic(), "Pessimistic Borda"),
				new VotingRuleDisplayData(new BordaFair(), "Fair Borda"),
				new VotingRuleDisplayData(new BordaOptimistic(), "Optimistic Borda"),
				new VotingRuleDisplayData(new InstantRunoff(), "Instant Runoff"),
				new VotingRuleDisplayData(new Copeland(), "Copeland"),
				new VotingRuleDisplayData(new KApproval(1), "Plurality"),
				new VotingRuleDisplayData(new KApproval(2), "2-approval"),
				new VotingRuleDisplayData(new KApproval(3), "3-approval"));
		rules.forEach(r -> possibilities.forEach(p -> {
			if (p.getClass().equals(r.getClass())) {
				p.setRule(r);
				p.setEnabled(true);
			}
		}));
		votingRuleList.setAll(possibilities);
		votingRuleListView.setCellFactory(CheckBoxListCell.forListView(item -> {
			BooleanProperty cb = item.getEnabled();
			cb.addListener((obs,wasSelected,nowSelected) -> {
				if (nowSelected) {
					if (!this.session.getCommand().getRules().contains(item.getRule())) {
						this.session.getCommand().getRules().add(item.getRule());
					}
				} else {
					this.session.getCommand().getRules().remove(item.getRule());
				}
			});
			return cb;
		}));
		votingRuleListView.setItems(votingRuleList);
		votingRuleListView.prefHeightProperty().bind(Bindings.size(votingRuleListView.getItems()).multiply(28));
	}
	
	private void setCriterionTreeView(Criterion criterion) {
		TreeItem<String> root = new CriterionTreeItem(criterion);
		this.criterionTreeView.setRoot(root);
	}

	private void setCriterionComboBoxes(Vector<CategoryFamily> families) {
		if (families.isEmpty())
			return;
		setCriterionTypeComboBox();
		setCriterionFamiliesComboBox(families);
		setCriterionCategoriesComboBox(this.criterionFamilyComboBox.getSelectionModel().getSelectedItem());
	}

	private void setCriterionCategoriesComboBox(CategoryFamily selectedItem) {
		this.criterionCategoryComboBox.setItems(FXCollections.observableArrayList(selectedItem.getPossibilities()));
		this.criterionCategoryComboBox.getSelectionModel().selectFirst();
	}

	private void setCriterionFamiliesComboBox(Vector<CategoryFamily> families) {
		this.criterionFamilyComboBox.setItems(FXCollections.observableArrayList(families));
		this.criterionFamilyComboBox.getSelectionModel().selectFirst();
		this.criterionFamilyComboBox.setOnAction(event -> {
			setCriterionCategoriesComboBox(this.criterionFamilyComboBox.getSelectionModel().getSelectedItem());
		});
	}

	private void setCriterionTypeComboBox() {
		this.criterionTypeComboBox.setItems(FXCollections.observableArrayList(CriterionTreeItem.CRITERION_MESSAGES));
		this.criterionTypeComboBox.getSelectionModel().selectFirst();
		this.criterionFamilyComboBox.setDisable(true);
		this.criterionCategoryComboBox.setDisable(true);
		this.criterionTypeComboBox.setOnAction(event -> {
			if (this.criterionTypeComboBox.getSelectionModel().getSelectedItem().equals(CriterionTreeItem.CRITERION_EQUALS_MESSAGE)) {
				this.criterionFamilyComboBox.setDisable(false);
				this.criterionCategoryComboBox.setDisable(false);
			} else {
				this.criterionFamilyComboBox.setDisable(true);
				this.criterionCategoryComboBox.setDisable(true);
			}
		});
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
