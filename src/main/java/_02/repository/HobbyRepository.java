package _02.repository;

import java.util.List;

import _02.model.Hobby;

public interface HobbyRepository {
	List<Hobby> getAllHobbies();
	Hobby getHobby(Integer id);
	void save(Hobby hobby);
	void truncateTable();
}
