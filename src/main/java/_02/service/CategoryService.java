package _02.service;

import java.util.List;

import _02.model.Category;

public interface CategoryService {
	List<Category> getAllCategories();
	
	Category getCategory(Integer id);
	
	void save(Category category);
	
	void truncateTable();
}
