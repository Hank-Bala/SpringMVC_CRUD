package _02.service;

import java.util.List;

import _02.model.Hobby;

public interface HobbyService {
	List<Hobby> getAllHobbies();
	
	Hobby getHobby(Integer id);
	
	void save(Hobby hobby);
	
	void truncateTable();
}

