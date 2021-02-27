package com.main.glory.Dao;

import com.main.glory.model.party.request.PartyWithName;
import com.main.glory.model.party.request.PartyWithUserHeadName;
import org.springframework.data.jpa.repository.JpaRepository;

import com.main.glory.model.party.Party;
import com.main.glory.model.quality.Quality;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PartyDao extends JpaRepository<Party, Long>  {

	void save(Quality quality);
	@Query(value = "SELECT party_name FROM party as p WHERE p.id = :party_id", nativeQuery = true)
	String getPartyNameByPartyId(@Param("party_id") Long party_id);

	@Query(value = "SELECT p.id,p.party_name FROM party as p ", nativeQuery = true)
	List<PartyWithName> getAllPartiesWithName();

	List<Party> findByCreatedBy(Long createdBy);

	@Query("select p from Party p where p.userHeadId=:userHeadId")
	List<Party> findByUserHeadId(Long userHeadId);


	@Query("select p from Party p where p.GSTIN=:gstin")
    Party findByGSTIN(String gstin);

    @Query("select p from Party p where p.partyCode=:partyCode")
	Party findByPartyCode(String partyCode);

	@Query("select p from Party p")
    List<Party> getAllParty();

	@Query("select p from Party p where p.id=:id")
    Party findByPartyId(Long id);

	@Query("select p from Party p where p.createdBy=:id OR p.userHeadId=:userHeadId")
	List<Party> findByCreatedByAndUserHeadId(Long id, Long userHeadId);

	@Modifying
	@Transactional
	@Query(value = "truncate table party",nativeQuery = true)
    void trucateRecord();

	@Query("select p from Party p where LOWER(p.partyName)=LOWER(:partyName)")
    Party getPartyByName(String partyName);


	@Query("select p from Party p where (:userId IS NULL OR p.createdBy=:userId) OR (:userHeadId IS NULL OR p.userHeadId = :userHeadId)")
	List<Party> getAllPartiesByUserId(Long userId, Long userHeadId);

	@Query("select p from Party p where p.createdBy=:userId OR p.userHeadId=:userHeadId")
	List<Party> getAllPartyByCreatedAndHead(Long userId, Long userHeadId);

	@Query("select p from Party p where p.createdBy=:userId")
	List<Party> getAllPartyByCreatedBy(Long userId);

	@Query("select new com.main.glory.model.party.request.PartyWithUserHeadName(p,(select u.firstName from UserData u where u.id=p.userHeadId )) from Party p where p.id = :id")
	PartyWithUserHeadName findPartyWithUserHeadById(Long id);

	@Query("select p from Party p where LOWER(p.partyName)=LOWER(:name) AND p.id!=:id")
    Party getPartyByNameExceptId(String name, Long id);


	@Query("select p from Party p where p.partyCode=:partyCode AND p.id!=:id")
	Party findByPartyCodeExceptId(String partyCode, Long id);

//	@Modifying
//	@Transactional
//	@Query("drop database :dbname")
//    void dropCommand(String dbname);
}
