package _02.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import _02.model.Category;
import _02.repository.CategoryRepository;
import _02.service.CategoryService;
@Transactional
@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	CategoryRepository categoryDao;
	
	@Override
	public List<Category> getAllCategories() {
		return categoryDao.getAllCategories();
	}

	@Override
	public Category getCategory(Integer id) {
		return categoryDao.getCategory(id);
	}

	@Override
	public void save(Category category) {
		categoryDao.save(category);
	}

	@Override
	public void truncateTable() {
		categoryDao.truncateTable();
	}
}
