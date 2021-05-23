package _02.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import _02.model.Hobby;
import _02.repository.HobbyRepository;
import _02.service.HobbyService;

import java.util.List;
@Service
@Transactional
public class HobbyServiceImpl implements HobbyService {

	@Autowired
	HobbyRepository hobbyDao;
	
	@Override
	public List<Hobby> getAllHobbies() {
		return hobbyDao.getAllHobbies();
	}

	@Override
	public Hobby getHobby(Integer id) {
		return hobbyDao.getHobby(id);
	}

	@Override
	public void save(Hobby hobby) {
		hobbyDao.save(hobby);
	}

	@Override
	public void truncateTable() {
		hobbyDao.truncateTable();
	}
}
