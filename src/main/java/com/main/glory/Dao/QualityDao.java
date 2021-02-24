package com.main.glory.Dao;

import com.main.glory.model.basic.PartyQuality;
import com.main.glory.model.quality.QualityWithPartyName;
import org.springframework.data.jpa.repository.JpaRepository;
import com.main.glory.model.quality.Quality;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@EnableJpaRepositories
public interface QualityDao extends JpaRepository<Quality, Long>  {

    @Query(value = "SELECT quality_id FROM quality as q WHERE q.quality_id = :qid", nativeQuery = true)
     String isQualityNameExist(@Param("qid") String quality_id);


    @Query(value = "Select * from quality as qa where qa.quality_id=:qid",nativeQuery = true)
    List<Quality> getQualityListById(@Param("qid") String quality_id);

    @Query("Select new com.main.glory.model.quality.QualityWithPartyName(q, (Select p.partyName from Party p where p.id = q.partyId)) from Quality q where q.partyId IS NOT NULL")
    List<QualityWithPartyName> findAllWithPartyName();

    @Query("Select new com.main.glory.model.quality.QualityWithPartyName(q, (Select p.partyName from Party p where p.id = q.partyId)) from Quality q where q.userHeadId = :userHeadId OR q.createdBy=:userHeadId AND q.partyId IS NOT NULL")
    List<QualityWithPartyName> findAllWithPartyNameByUserHeadId(Long userHeadId);

    @Query("Select new com.main.glory.model.quality.QualityWithPartyName(q, (Select p.partyName from Party p where p.id = q.partyId)) from Quality q where createdBy = :createdBy AND q.partyId IS NOT NULL")
    List<QualityWithPartyName> findAllWithPartyNameByCreatedBy( Long createdBy);

    Optional<Quality> findById(Long qualityId);

    Optional<Quality> findById(String qualityId);

    Optional<Quality> findByQualityIdAndQualityName(String qualityId, String qualityName);

    Optional<List<Quality>> findByPartyId(Long partyId);

    Optional<List<Quality>> findByUserHeadId(Long userHeadId);

    @Query("Select q from Quality q where id = :qualityEntryId AND q.partyId =:partyId")
    Optional<Quality> findByPartyIdAndQualityId(Long qualityEntryId, Long partyId);

    @Query("select q from Quality q")
    List<Quality> getAllQuality();

    @Query("Select new com.main.glory.model.quality.QualityWithPartyName(q, (Select p.partyName from Party p where p.id = q.partyId)) from Quality q where q.userHeadId = :userHeadId OR q.createdBy=:id AND q.partyId IS NOT NULL")
    List<QualityWithPartyName> findAllWithPartyByCreatedAndHeadId(Long id, Long userHeadId);


    @Query("select q from Quality q where q.partyId=:id")
    List<Quality> getQualityListByPartyIdId(Long id);

    @Query("select q from Quality q where q.id=:id")
    Quality getqualityById(Long id);

    @Modifying
    @Transactional
    @Query("delete from Quality q where q.id=:id")
    void deleteByQualtyId(Long id);
}


