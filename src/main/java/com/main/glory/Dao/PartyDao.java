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
	@Query(value = "SELECT party_name FROM party as p WHERE p.id = :party_id AND partyIsDeleted=false", nativeQuery = true)
	String getPartyNameByPartyId(@Param("party_id") Long party_id);

	@Query(value = "SELECT p.id,p.party_name FROM party as p where AND partyIsDeleted=false ", nativeQuery = true)
	List<PartyWithName> getAllPartiesWithName();

	@Query("Select p from Party p where p.createdBy=:createdBy AND p.partyIsDeleted=false AND p.partyIsDeleted IS NOT NULL")
	List<Party> findByCreatedBy(Long createdBy);

	@Query("Select p from Party p where p.createdBy=:userHeadId OR p.userHeadId=:userHeadId AND p.partyIsDeleted=false AND p.partyIsDeleted IS NOT NULL")
	List<Party> findByUserHeadId(Long userHeadId);

	@Query("Select p from Party p where p.GSTIN=:gstin AND p.partyIsDeleted=false AND p.partyIsDeleted IS NOT NULL")
    Optional<Party> findByGSTIN(String gstin);

	@Query("Select p from Party p where p.partyCode=:partyCode AND p.partyIsDeleted=false AND p.partyIsDeleted IS NOT NULL")
	Optional<Party>findByPartyCode(String partyCode);

	@Query("Select p from Party p where p.partyIsDeleted=false AND p.partyIsDeleted IS NOT NULL")
	List<Party> findAllParty();

	@Query("Select p from Party p where p.id=:id AND p.partyIsDeleted=false AND p.partyIsDeleted IS NOT NULL")
	Optional<Party> findByPartyId(Long id);
}
