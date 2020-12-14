package com.main.glory.Dao;

import com.main.glory.model.party.request.PartyWithName;
import org.springframework.data.jpa.repository.JpaRepository;

import com.main.glory.model.party.Party;
import com.main.glory.model.quality.Quality;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PartyDao extends JpaRepository<Party, Long>  {

	void save(Quality quality);
	@Query(value = "SELECT party_name FROM party as p WHERE p.id = :party_id where p.isDelete = false", nativeQuery = true)
	String getPartyNameByPartyId(@Param("party_id") Long party_id);

	@Query(value = "SELECT p.id,p.party_name FROM party as p where p.isDelete = false ", nativeQuery = true)
	List<PartyWithName> getAllPartiesWithName();

	@Query("Select p from Party p where (p.userHeadId = :createdBy OR p.createdBy =: createdBy) AND p.isDelete = false")
	List<Party> findByCreatedBy(Long createdBy);

	@Query("Select p from Party p where (p.userHeadId = :userHeadId OR p.createdBy =: userHeadId) AND p.isDelete = false")
	List<Party> findByUserHeadId(Long userHeadId);

    Optional<Party> findByGSTIN(String gstin);
}
