package com.main.glory.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.glory.model.Party;
import com.main.glory.model.Quality;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

public interface PartyDao extends JpaRepository<Party, Long>  {

	void save(Quality quality);
	@Query(value = "SELECT party_name FROM party as p WHERE p.entry_id = :pparty_id", nativeQuery = true)
	String getPartyNameByPartyId(@Param("pparty_id") Long quality_id);

}
