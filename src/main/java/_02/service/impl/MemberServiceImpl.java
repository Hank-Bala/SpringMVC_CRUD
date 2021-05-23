package _02.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import _02.model.Member;
import _02.repository.MemberRepository;
import _02.service.MemberService;
@Service
@Transactional
public class MemberServiceImpl implements MemberService {

	@Autowired
	MemberRepository memberDao;
	
	@Override
	public void save(Member member) {
		memberDao.save(member);
	}

	@Override
	public void delete(Integer id) {
		memberDao.delete(id);
	}

	@Override
	public void update(Member member) {
		memberDao.update(member);
	}

	@Override
	public List<Member> getAllMembers() {
		return memberDao.getAll();
	}

	@Override
	public Member get(Integer id) {
		return memberDao.get(id);
		
	}
	@Override
	public void truncateTable() {
		memberDao.truncateTable();
	}
}
