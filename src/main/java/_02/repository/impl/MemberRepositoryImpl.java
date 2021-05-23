package _02.repository.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import _00_init.config.WebAppInitializer;
import _02.model.Member;
import _02.repository.MemberRepository;

import java.util.List;

@Repository
public class MemberRepositoryImpl implements MemberRepository {

	@Autowired
	SessionFactory factory;

	@Override
	public void save(Member member) {
		Session session = getSession();
		session.save(member);
	}

	@Override
	public void delete(Integer id) {
		Session session = getSession();
		Member member = get(id);
		if (member != null) {
			member.setCategory(null);
			member.setHobby(null);
			session.delete(member);
		}
	}

	@Override
	public void update(Member member) {
		if (member != null && member.getId() != null) 	{
			Session session = getSession();
			session.saveOrUpdate(member);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Member> getAll() {
		String hql = "FROM Member";
		Session session = getSession();
		List<Member> list = session.createQuery(hql).getResultList();
		return list;
	}

	@Override
	public Member get(Integer id) {
		return factory.getCurrentSession().get(Member.class, id);
	}
	// 本方法展示 
	// 1. session.createQuery(hql).executeUpdate(); 
	// 2. session.createNativeQuery(sql).executeUpdate();
	@Override
	public void truncateTable() {
		Session session = factory.getCurrentSession();
		String hql = "DELETE FROM Member";
		session.createQuery(hql).executeUpdate();    
		String sql = null;
		if (WebAppInitializer.DB_TYPE.equals(WebAppInitializer.DB_MYSQL)) {
			// 將MySQL資料庫底層的自增鍵值(AUTO_INCREMENT)的起始值設定為 1
			sql = "ALTER TABLE member_crud AUTO_INCREMENT = 1";
			session.createNativeQuery(sql).executeUpdate();
	    } else if (WebAppInitializer.DB_TYPE.equals(WebAppInitializer.DB_SQLSERVER)) {
	    	// 將MySQL資料庫底層的自增鍵值(IDENTITY)的起始值設定為 1
	    	sql = "DBCC CHECKIDENT ('member_crud', RESEED, 0)";
	    	session.createNativeQuery(sql).executeUpdate();
	    }
	}
	
	public Session getSession() {
        return factory.getCurrentSession();			
	}
	
}
