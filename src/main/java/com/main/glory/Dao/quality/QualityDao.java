package com.main.glory.Dao.quality;

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

    @Query("Select new com.main.glory.model.quality.QualityWithPartyName(q, (Select p.partyName from Party p where p.id = q.partyId),(Select p.partyCode from Party p where p.id = q.partyId)) from Quality q where q.partyId IS NOT NULL")
    List<QualityWithPartyName> findAllWithPartyName();

    @Query("Select new com.main.glory.model.quality.QualityWithPartyName(q, (Select p.partyName from Party p where p.id = q.partyId),(Select p.partyCode from Party p where p.id = q.partyId)) from Quality q where q.userHeadId = :userHeadId OR q.createdBy=:userHeadId AND q.partyId IS NOT NULL")
    List<QualityWithPartyName> findAllWithPartyNameByUserHeadId(Long userHeadId);

    @Query("Select new com.main.glory.model.quality.QualityWithPartyName(q, (Select p.partyName from Party p where p.id = q.partyId),(Select p.partyCode from Party p where p.id = q.partyId)) from Quality q where createdBy = :createdBy AND q.partyId IS NOT NULL")
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

    @Query("Select new com.main.glory.model.quality.QualityWithPartyName(q, (Select p.partyName from Party p where p.id = q.partyId),(Select p.partyCode from Party p where p.id = q.partyId)) from Quality q where q.userHeadId = :userHeadId OR q.createdBy=:id AND q.partyId IS NOT NULL")
    List<QualityWithPartyName> findAllWithPartyByCreatedAndHeadId(Long id, Long userHeadId);


    @Query("select q from Quality q where q.partyId=:id")
    List<Quality> getQualityListByPartyIdId(Long id);

    @Query("select q from Quality q where q.id=:id")
    Quality getqualityById(Long id);

    @Modifying
    @Transactional
    @Query("delete from Quality q where q.id=:id")
    void deleteByQualtyId(Long id);

    @Query("select s from Quality s where s.createdBy=:userId OR s.userHeadId=:userHeadId")
    List<Quality> getAllQualityWithIdAndUserHeadId(Long userId, Long userHeadId);

    @Query("select q from Quality q where q.createdBy=:userId")
    List<Quality> getAllQualityCreatedBy(Long userId);

    @Query("Select new com.main.glory.model.quality.QualityWithPartyName(q, (Select p.partyName from Party p where p.id = q.partyId),(Select p.partyCode from Party p where p.id = q.partyId)) from Quality q where q.userHeadId = :userHeadId AND q.partyId IS NOT NULL")
    List<QualityWithPartyName> findQualityByUserHeadId(Long userHeadId);

    @Query("select q from Quality q where LOWER(q.qualityId)=LOWER(:quality_id)")
    Quality getQualityById(String quality_id);

    @Query("select q from Quality q where LOWER(q.qualityId)=LOWER(:quality_id) AND q.id!=:id")
    Optional<Quality> getQualityByIdExceptId(String quality_id, Long id);

    @Query("select q from Quality q where q.qualityNameId=:id")
    Optional<List<Quality>> getAllQualityByQualityNameId(Long id);


    @Query(value = "SELECT * FROM quality as q WHERE q.quality_id = :qualityId", nativeQuery = true)
    Optional<Quality> findByQualityId(String qualityId);

    @Modifying
    @Transactional
    @Query("update Quality q set q.wtPer100m=:per100m,q.mtrPerKg=:mtrPerKg where q.id=:qualityId")
    void updateQualityWtAndMtrKgById(Long qualityId, Double per100m, Double mtrPerKg);

    @Query("Select q from Quality q where id = :qualityId AND q.partyId =:partyId")
    Optional<Quality> getQualityByEntryIdAndPartyId(Long qualityId, Long partyId);

    @Query("select q from Quality q where q.id=(select s.qualityId from StockBatch s where s.id=:stockId)")
    Quality getQualityByStockId(Long stockId);
}


