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

    @Query(value = "SELECT quality_id FROM quality as q WHERE q.quality_id = :qid AND q.qualityIsDeleted=false AND q.qualityIsDeleted IS NOT NULL ", nativeQuery = true)
     String isQualityNameExist(@Param("qid") String quality_id);


    @Query(value = "Select * from quality as qa where qa.quality_id=:qid AND q.qualityIsDeleted=false AND q.qualityIsDeleted IS NOT NULL",nativeQuery = true)
    List<Quality> getQualityListById(@Param("qid") String quality_id);

    @Query("Select new com.main.glory.model.quality.QualityWithPartyName(q, (Select p.partyName from Party p where p.id = q.partyId AND p.partyIsDeleted=false AND p.partyIsDeleted IS NOT NULL)) from Quality q where q.qualityIsDeleted=false AND q.qualityIsDeleted IS NOT NULL AND q.partyId IS NOT NULL")
    List<QualityWithPartyName> findAllWithPartyName();

    @Query("Select new com.main.glory.model.quality.QualityWithPartyName(q, (Select p.partyName from Party p where p.id = q.partyId AND p.partyIsDeleted=false AND p.partyIsDeleted IS NOT NULL)) from Quality q where q.userHeadId = :userHeadId AND q.qualityIsDeleted=false AND q.qualityIsDeleted IS NOT NULL")
    List<QualityWithPartyName> findAllWithPartyNameByUserHeadId(Long userHeadId);

    @Query("Select new com.main.glory.model.quality.QualityWithPartyName(q, (Select p.partyName from Party p where p.id = q.partyId AND p.partyIsDeleted=false AND p.partyIsDeleted IS NOT NULL)) from Quality q where q.createdBy = :createdBy AND q.qualityIsDeleted=false AND q.qualityIsDeleted IS NOT NULL")
    List<QualityWithPartyName> findAllWithPartyNameByCreatedBy( Long createdBy);

    @Query("Select q from Quality q where q.id = :qualityId AND q.qualityIsDeleted=false AND q.qualityIsDeleted IS NOT NULL")
    Optional<Quality> findByQualityId(Long qualityId);

    @Query("Select q from Quality q where q.qualityId = :qualityId AND q.qualityIsDeleted=false AND q.qualityIsDeleted IS NOT NULL")
    Optional<Quality> findByQualityId(String qualityId);

    @Query("Select q from Quality q where qualityId = :qualityId AND qualityName=:qualityName AND q.qualityIsDeleted=false AND q.qualityIsDeleted IS NOT NULL")
    Optional<Quality> findByQualityIdAndQualityName(String qualityId, String qualityName);

    @Query("Select q from Quality q where partyId=:partyId AND q.qualityIsDeleted=false AND q.qualityIsDeleted IS NOT NULL")
    Optional<List<Quality>> findByPartyId(Long partyId);

    @Query("Select q from Quality q where createdBy=:userHeadId OR userHeadId=:userHeadId AND q.qualityIsDeleted=false AND q.qualityIsDeleted IS NOT NULL")
    Optional<List<Quality>> findByUserHeadId(Long userHeadId);
}


