package com.main.glory.Dao;

import com.main.glory.model.basic.PartyQuality;
import com.main.glory.model.quality.QualityWithPartyName;
import org.springframework.data.jpa.repository.JpaRepository;
import com.main.glory.model.quality.Quality;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


@EnableJpaRepositories
public interface QualityDao extends JpaRepository<Quality, Long>  {

    @Query(value = "SELECT quality_id FROM quality as q WHERE q.quality_id = :qid", nativeQuery = true)
     String isQualityNameExist(@Param("qid") String quality_id);


    @Query(value = "Select * from quality as qa where qa.quality_id=:qid",nativeQuery = true)
    List<Quality> getQualityListById(@Param("qid") String quality_id);

    @Query("Select new com.main.glory.model.quality.QualityWithPartyName(q, (Select p.partyName from Party p where p.id = q.partyId AND p.isDelete=false)) from Quality q")
    List<QualityWithPartyName> findAllWithPartyName();

    @Query("Select new com.main.glory.model.quality.QualityWithPartyName(q, (Select p.partyName from Party p where p.id = q.partyId AND p.isDelete=false)) from Quality q where userHeadId = :userHeadId")
    List<QualityWithPartyName> findAllWithPartyNameByUserHeadId(Long userHeadId);

    @Query("Select new com.main.glory.model.quality.QualityWithPartyName(q, (Select p.partyName from Party p where p.id = q.partyId AND p.isDelete=false)) from Quality q where createdBy = :createdBy")
    List<QualityWithPartyName> findAllWithPartyNameByCreatedBy( Long createdBy);

    @Query("Select q from Quality q where q.id = :qualityId")
    Optional<Quality> findByQualityEntryId(Long qualityId);

    Optional<Quality> findByQualityEntryId(String qualityId);

    Optional<Quality> findByQualityIdAndQualityName(String qualityId, String qualityName);

    @Query("Select q from Quality q where q.partyId = :partyId AND q.isDelete=false")
    Optional<List<Quality>> findByPartyId(Long partyId);

    Optional<List<Quality>> findByUserHeadId(Long userHeadId);
}


