package _02.repository.impl;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import _02.model.Category;
import _02.repository.CategoryRepository;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

	@Autowired
	SessionFactory factory;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Category> getAllCategories() {
		String hql = "FROM Category";
		Session session = factory.getCurrentSession();
		return session.createQuery(hql).getResultList();
	}
	
	@Override
	public Category getCategory(Integer id) {
		Session session = factory.getCurrentSession();
		return session.get(Category.class, id);
	}
	@Override
	public void save(Category category) {
		Session session = factory.getCurrentSession();
		session.save(category);
		session.flush();
	}

	@Override
	public void truncateTable() {
		Session session = factory.getCurrentSession();
		String hql = "DELETE FROM Category";
		session.createQuery(hql).executeUpdate();
	}
}
