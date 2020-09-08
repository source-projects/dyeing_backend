package Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import model.Party;
import model.Quality;

public interface PartyDao extends JpaRepository<Party, Long>  {

	void save(Quality quality);



}
