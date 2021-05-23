package _02.service;

import java.util.List;
import org.springframework.stereotype.Repository;

import _02.model.Member;

@Repository
public interface MemberService {
	void save(Member member);
	void delete(Integer id);
	Member get(Integer id);
	void update(Member member);
	List<Member> getAllMembers();
	void truncateTable();
	
}
