/**
 * 
 */
package main.java;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.cells.editors.base.GenericEditableTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellEditEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import main.java.base.Alternative;
import main.java.base.Category;
import main.java.base.CategoryFamily;
import main.java.base.Question;
import main.java.base.Voter;
import main.java.base.criterion.Criterion;
import main.java.base.session.Session;

/**
 * @author martin
 *
 */
public class InputTabViewController implements Initializable {

	/**
	 * Data objects
	 */
	private Session session = new Session();

	/**
	 * TableView list objects
	 */
	private ObservableList<QuestionTableData> questions = FXCollections.observableArrayList();
	private ObservableList<AlternativeTableData> alternatives = FXCollections.observableArrayList();
	private ObservableList<VoterTableData> voters = FXCollections.observableArrayList();

	/**
	 * UI objects
	 */
    @FXML private StackPane mainPane;
    
    @FXML private TableView<QuestionTableData> questionsTableView;
    @FXML private TableColumn<QuestionTableData, String> questionDescriptionColumn;
    @FXML private TableColumn<QuestionTableData, String> questionAlternativesColumn;
    @FXML private TableColumn<QuestionTableData, String> questionVotesColumn;

    @FXML private TableView<AlternativeTableData> alternativesTableView;
    @FXML private TableColumn<AlternativeTableData, String> alternativeNameColumn;

    @FXML private TableView<VoterTableData> votersTableView;
    @FXML private TableColumn<VoterTableData, String> voterNameColumn;

    @FXML private JFXTreeTableView<CategoryTreeTableData> categoryTreeTableView;
    @FXML private JFXTreeTableColumn<CategoryTreeTableData, String> categoryFamilyColumn;
    @FXML private JFXTreeTableColumn<CategoryTreeTableData, String> categoryNameColumn;

    @FXML private TextField categoryFamilyTextField;
    @FXML private TextField categoryNameTextField;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
    	questionsTableView.setItems(questions);
    	alternativesTableView.setItems(alternatives);
    	votersTableView.setItems(voters);
    	setupQuestionTableView();
    	setupAlternativeTableView();
    	setupVotersTableView();
    	setupCategoryTreeTableView();
	}

	/**
	 * 
	 * 
	 * TableView Setup
	 * 
	 * 
	 */
	private void setupQuestionTableView() {
		questionsTableView.setRowFactory( tv -> {
			TableRow<QuestionTableData> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if(event.getButton().equals(MouseButton.PRIMARY)){
		            if(event.getClickCount() == 2) {
						showQuestionEditDialog(row.getItem().getQuestion());
		            }
		        }
			});
			return row ;
		});
		questionDescriptionColumn.setCellFactory(column -> EditCell.createStringEditCell());
    	questionDescriptionColumn.setCellValueFactory(cellData -> {
    		return cellData.getValue().getDescription();
    	});
    	questionAlternativesColumn.setCellValueFactory(cellData -> {
    		return cellData.getValue().getAlternatives();
    	});
    	questionVotesColumn.setCellValueFactory(cellData -> {
    		return cellData.getValue().getVotes();
    	});
    	questionsTableView.getSortOrder().add(questionDescriptionColumn);
	}

	private void setupAlternativeTableView() {
		alternativeNameColumn.setCellFactory(column -> EditCell.createStringEditCell());
		alternativeNameColumn.setCellValueFactory(cellData -> {
    		return cellData.getValue().getName();
    	});
		alternativeNameColumn.setOnEditCommit(event -> {
            String name = event.getNewValue().trim();
			if (name.isEmpty() || this.session.getInput().getAlternatives().stream().anyMatch(a -> a.getName().equals(name))) {
	            event.getRowValue().setName(event.getOldValue());
	            return;
			}
            ((AlternativeTableData) event.getTableView().getItems()
                .get(event.getTablePosition().getRow())).setName(name);
            updateQuestionTableItems();
        });
		alternativesTableView.getSortOrder().add(alternativeNameColumn);
	}
	
	private void setupVotersTableView() {
		voterNameColumn.setCellFactory(column -> EditCell.createStringEditCell());
		voterNameColumn.setCellValueFactory(cellData -> {
    		return cellData.getValue().getName();
    	});
		voterNameColumn.setOnEditCommit(event -> {
            String name = event.getNewValue();
            ((VoterTableData) event.getTableView().getItems()
                .get(event.getTablePosition().getRow())).setName(name);
        });
		votersTableView.getSortOrder().add(voterNameColumn);
	}

	private void setupCategoryTreeTableView() {
		categoryTreeTableView.setEditable(true);
		categoryFamilyColumn.setEditable(true);
		categoryNameColumn.setEditable(true);
		categoryTreeTableView.setShowRoot(false);
		categoryFamilyColumn.setCellFactory(column -> new GenericEditableTreeTableCell<CategoryTreeTableData, String>());
		categoryNameColumn.setCellFactory(column -> new GenericEditableTreeTableCell<CategoryTreeTableData, String>());
		JFXTreeTableViewUtils.setupCellValueFactory(categoryFamilyColumn, CategoryTreeTableData::getFamilyName);
		JFXTreeTableViewUtils.setupCellValueFactory(categoryNameColumn, CategoryTreeTableData::getCategoryName);
		categoryFamilyColumn.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<CategoryTreeTableData,String>>() {
			@Override
			public void handle(TreeTableColumn.CellEditEvent<CategoryTreeTableData, String> event) {
				CategoryTreeTableData item = event.getTreeTablePosition().getTreeItem().getValue();
				for (Criterion c : session.getCommand().getCriteria())
					c.updateKey(item.getFamily().getDescription(), event.getNewValue());
				item.setFamilyName(event.getNewValue());
			}
		});
		categoryNameColumn.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<CategoryTreeTableData,String>>() {
			@Override
			public void handle(TreeTableColumn.CellEditEvent<CategoryTreeTableData, String> event) {
				CategoryTreeTableData item = event.getTreeTablePosition().getTreeItem().getValue();
				for (Criterion c : session.getCommand().getCriteria())
					c.updateValue(item.getFamily().getDescription(), item.getCategory().getDescription(), event.getNewValue());
				item.setCategoryName(event.getNewValue());
			}
		});
	}

	/**
	 * 
	 * 
	 * Question edit handler (opens popup dialog)
	 * 
	 * 
	 */
	private void showQuestionEditDialog(Question question) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/fxml/QuestionEditView.fxml"));
			StackPane content = loader.load();
			QuestionEditViewController controller = (QuestionEditViewController) loader.getController();
			controller.setupView(question, this.session.getInput().getAlternatives(), this.session.getInput().getFamilies(), this.session.getInput().getVoters());
			DialogBuilder.showConfirmCancelDialog(content, this.mainPane, new EventHandler<ActionEvent>() {
			    @Override public void handle(ActionEvent e) {
			    	Question editedQuestion = controller.getEditedQuestion();
			    	Vector<CategoryFamily> editedCategories = controller.getEditedCategories();
			    	question.setDescription(editedQuestion.getDescription());
			    	question.setVotes(editedQuestion.getVotes());
			    	question.setAlternatives(editedQuestion.getAlternatives());
			    	session.getInput().setFamilies(editedCategories);
			    	updateQuestionTableItems();
			    }
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 
	 * Session setup (feed data to TableViews)
	 * 
	 * 
	 */
	public void setSession(Session session) {
		this.session = session;
		updateQuestionTableItems();
		updateAlternativeTableItems();
		updateVoterTableItems();
		updateCategoryItems();
	}

	private void updateCategoryItems() {
		ObservableList<CategoryTreeTableData> items = FXCollections.observableArrayList();
		for (CategoryFamily f : this.session.getInput().getFamilies())
			for (Category c : f.getPossibilities())
				items.add(new CategoryTreeTableData(f, c));
		TreeItem<CategoryTreeTableData> root = new RecursiveTreeItem<CategoryTreeTableData>(items, RecursiveTreeObject::getChildren);
		root.setExpanded(true);
    	categoryTreeTableView.setRoot(root);
		categoryTreeTableView.unGroup(categoryNameColumn);
		categoryTreeTableView.unGroup(categoryFamilyColumn);
		categoryTreeTableView.group(categoryFamilyColumn);
		categoryTreeTableView.getRoot().getChildren().forEach(item -> item.setExpanded(true));
	}

	private void updateVoterTableItems() {
		voters.clear();
		this.session.getInput().getVoters().forEach(voter -> {
			this.voters.add(new VoterTableData(voter));
		});
	}

	private void updateAlternativeTableItems() {
		alternatives.clear();
		this.session.getInput().getAlternatives().forEach(alternative -> {
			this.alternatives.add(new AlternativeTableData(alternative));
		});
	}

	private void updateQuestionTableItems() {
		questions.clear();
		this.session.getInput().getQuestions().forEach(question -> {
			this.questions.add(new QuestionTableData(question));
		});
	}

	/**
	 * 
	 * 
	 * TableView Add/Delete Button handlers
	 * 
	 * 
	 */
    @SuppressWarnings("unchecked")
	@FXML
    void addAlternative(MouseEvent event) {
    	Alternative a = new Alternative("Alternative "+this.alternatives.size());
    	AlternativeTableData item = new AlternativeTableData(a);
        alternativesTableView.getSelectionModel().clearSelection();
        alternativesTableView.getItems().add(item);
        alternativesTableView.getSelectionModel().select(
        		alternativesTableView.getItems().size() - 1, 
        		alternativesTableView.getFocusModel().getFocusedCell().getTableColumn());
        alternativesTableView.scrollTo(item);
        this.session.getInput().addAlternative(a);
    }

    @FXML
    void deleteAlternative(MouseEvent event) {
    	ObservableList<AlternativeTableData> selectedItems = alternativesTableView.getSelectionModel().getSelectedItems();
    	selectedItems.stream().forEach(item -> this.session.getInput().removeAlternative(item.getAlternative()));
		alternativesTableView.getItems().removeAll(selectedItems);
    	updateQuestionTableItems();
    }

    @SuppressWarnings("unchecked")
	@FXML
    void addQuestion(MouseEvent event) {
    	Question q = new Question("Name");
    	QuestionTableData item = new QuestionTableData(q);
        questionsTableView.getSelectionModel().clearSelection();
        questionsTableView.getItems().add(item);
        questionsTableView.getSelectionModel().select(
        		questionsTableView.getItems().size() - 1, 
        		questionsTableView.getFocusModel().getFocusedCell().getTableColumn());
        questionsTableView.scrollTo(item);
        this.session.getInput().addQuestion(q);
    }

    @FXML
    void deleteQuestion(MouseEvent event) {
    	ObservableList<QuestionTableData> selectedItems = questionsTableView.getSelectionModel().getSelectedItems();
    	selectedItems.stream().forEach(item -> this.session.getInput().removeQuestion(item.getQuestion()));
    	questionsTableView.getItems().removeAll(selectedItems);
    }

    @SuppressWarnings("unchecked")
	@FXML
    void addVoter(MouseEvent event) {
    	Voter v = new Voter("Name");
		VoterTableData item = new VoterTableData(v);
        votersTableView.getSelectionModel().clearSelection();
        votersTableView.getItems().add(item);
        votersTableView.getSelectionModel().select(
        		votersTableView.getItems().size() - 1, 
        		votersTableView.getFocusModel().getFocusedCell().getTableColumn());
        votersTableView.scrollTo(item);
        this.session.getInput().addVoter(v);
    }

    @FXML
    void deleteVoter(MouseEvent event) {
    	ObservableList<VoterTableData> selectedItems = votersTableView.getSelectionModel().getSelectedItems();
    	selectedItems.stream().forEach(item -> this.session.getInput().removeVoter(item.getVoter()));
		votersTableView.getItems().removeAll(selectedItems);
    	updateQuestionTableItems();
    }

	@FXML
    private void addCategory(MouseEvent event) {
		String familyName = this.categoryFamilyTextField.getText().trim();
		String categoryName = this.categoryNameTextField.getText().trim();
		for (CategoryFamily f : this.session.getInput().getFamilies()) {
			if (f.getDescription().equals(familyName)) {
				for (Category c : f.getPossibilities()) {
					if (c.getDescription().equals(categoryName)) {
						updateCategoryItems();
						return;
					}
				}
				f.getPossibilities().add(new Category(categoryName));
				updateCategoryItems();
				return;
			}
		}
		CategoryFamily f = new CategoryFamily(familyName);
		f.getPossibilities().add(new Category(categoryName));
		this.session.getInput().getFamilies().add(f);
		updateCategoryItems();
    }

    @FXML
    private void deleteCategory(MouseEvent event) {
    	TreeItem<CategoryTreeTableData> item = this.categoryTreeTableView.getSelectionModel().getSelectedItem();
    	if (item == null)
    		return;
    	if (item.getChildren().isEmpty()) {
    		CategoryFamily familyDeleted = item.getValue().getFamily();
    		Category categoryDeleted = item.getValue().getCategory();
    		this.session.getCommand().removeCategory(familyDeleted, categoryDeleted);
    		this.session.getInput().removeCategory(familyDeleted, categoryDeleted);
    	} else {
    		CategoryFamily familyDeleted = item.getChildren().get(0).getValue().getFamily();
    		this.session.getCommand().removeFamily(familyDeleted);
    		this.session.getInput().removeFamily(familyDeleted);
    	}
    	updateCategoryItems();
    }

}
