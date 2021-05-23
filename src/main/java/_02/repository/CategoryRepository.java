package _02.repository;

import java.util.List;

import _02.model.Category;

public interface CategoryRepository {
	List<Category> getAllCategories();
	
	Category getCategory(Integer id);
	
	void save(Category category);
	
	void truncateTable();
}
