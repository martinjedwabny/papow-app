package main.java;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import main.java.base.Category;
import main.java.base.CategoryFamily;

public class CategoryTreeTableData extends RecursiveTreeObject<CategoryTreeTableData> {
	private CategoryFamily family;
	private Category category;
	private StringProperty familyName;
	private StringProperty categoryName;
	
	public CategoryTreeTableData(CategoryFamily family, Category category) {
		super();
		this.family = family;
		this.category = category;
		this.familyName = new SimpleStringProperty(family.getDescription());
		this.categoryName = new SimpleStringProperty(category.getDescription());
	}
	public StringProperty getFamilyName() {
		return familyName;
	}
	public void setFamilyName(String familyName) {
		this.familyName.set(familyName);
		this.family.setDescription(familyName);
	}
	public StringProperty getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName.set(categoryName);
		this.category.setDescription(categoryName);
	}
	public CategoryFamily getFamily() {
		return family;
	}
	public Category getCategory() {
		return category;
	}
}
