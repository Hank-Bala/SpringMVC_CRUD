package _02.repository.impl;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import _02.model.Hobby;

@Repository
public class HobbyRepository implements _02.repository.HobbyRepository {

	@Autowired
	SessionFactory factory;

	@SuppressWarnings("unchecked")
	@Override
	public List<Hobby> getAllHobbies() {
		String hql = "FROM Hobby";
		Session session = factory.getCurrentSession();
		return session.createQuery(hql).getResultList();
	}

	@Override
	public Hobby getHobby(Integer id) {
		Session session = factory.getCurrentSession();
		return session.get(Hobby.class, id);
	}

	@Override
	public void save(Hobby hobby) {
		Session session = factory.getCurrentSession();
		session.save(hobby);
		session.flush();
	}

	@Override
	public void truncateTable() {
		Session session = factory.getCurrentSession();
		String hql = "DELETE FROM Hobby";
		session.createQuery(hql).executeUpdate();
	}
}
