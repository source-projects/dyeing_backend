package com.main.glory.Dao.quality;

import com.main.glory.model.quality.QualityWithPartyName;
import com.main.glory.model.quality.response.QualityWithQualityNameParty;
import org.springframework.data.jpa.repository.JpaRepository;

import com.main.glory.Dao.FilterDao;
import com.main.glory.model.quality.Quality;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@EnableJpaRepositories
public interface QualityDao extends FilterDao<Quality>  {

    @Query(value = "SELECT quality_id FROM quality as q WHERE q.quality_id = :qid", nativeQuery = true)
    String isQualityNameExist(@Param("qid") String quality_id);


    @Query(value = "Select * from quality as qa where qa.quality_id=:qid",nativeQuery = true)
    List<Quality> getQualityListById(@Param("qid") String quality_id);

    @Query("Select new com.main.glory.model.quality.QualityWithPartyName(q, q.party.partyName,q.party.partyCode,q.party.userHeadData.userName) from Quality q where q.party IS NOT NULL")
    List<QualityWithPartyName> findAllWithPartyName();

    @Query("Select new com.main.glory.model.quality.QualityWithPartyName(q, q.party.partyName,q.party.partyCode,q.party.userHeadData.userName) from Quality q where q.userHeadData.id = :userHeadId OR q.userCreatedByData.id=:userHeadId AND q.party IS NOT NULL")
    List<QualityWithPartyName> findAllWithPartyNameByUserHeadId(Long userHeadId);

    @Query("Select new com.main.glory.model.quality.QualityWithPartyName(q, q.party.partyName,q.party.partyCode,q.party.userHeadData.userName) from Quality q where q.userCreatedByData.id = :createdBy AND q.party IS NOT NULL")
    List<QualityWithPartyName> findAllWithPartyNameByCreatedBy( Long createdBy);

    Optional<Quality> findById(Long qualityId);

    Optional<Quality> findById(String qualityId);

    Optional<Quality> findByQualityIdAndQualityName(String qualityId, String qualityName);

    @Query("Select q from Quality q where q.party.id =:partyId")
    Optional<List<Quality>> findByPartyId(Long partyId);

    @Query("Select q from Quality q where q.userHeadData.id = :userHeadId")
    Optional<List<Quality>> findByUserHeadId(Long userHeadId);

    @Query("Select q from Quality q where id = :qualityEntryId AND q.party.id =:partyId")
    Optional<Quality> findByPartyIdAndQualityId(Long qualityEntryId, Long partyId);

    @Query("select q from Quality q")
    List<Quality> getAllQuality();

    @Query("Select new com.main.glory.model.quality.QualityWithPartyName(q, q.party.partyName,q.party.partyCode,q.party.userHeadData.userName) from Quality q where q.userHeadData.id = :userHeadId OR q.userCreatedByData.id=:id AND q.party IS NOT NULL")
    List<QualityWithPartyName> findAllWithPartyByCreatedAndHeadId(Long id, Long userHeadId);


    @Query("select q from Quality q where q.party.id=:id")
    List<Quality> getQualityListByPartyIdId(Long id);

    @Query("select q from Quality q where q.id=:id")
    Quality getqualityById(Long id);

    @Modifying
    @Transactional
    @Query("delete from Quality q where q.id=:id")
    void deleteByQualtyId(Long id);

    @Query("select s from Quality s where s.userCreatedByData.id=:userId OR s.userHeadData.id=:userHeadId")
    List<Quality> getAllQualityWithIdAndUserHeadId(Long userId, Long userHeadId);

    @Query("select q from Quality q where q.userCreatedByData.id=:userId")
    List<Quality> getAllQualityCreatedBy(Long userId);

    @Query("Select new com.main.glory.model.quality.QualityWithPartyName(q, q.party.partyName,q.party.partyCode,q.party.userHeadData.userName) from Quality q where q.userHeadData.id = :userHeadId AND q.party IS NOT NULL")
    List<QualityWithPartyName> findQualityByUserHeadId(Long userHeadId);

    @Query("select q from Quality q where LOWER(q.qualityId)=LOWER(:quality_id)")
    Quality getQualityById(String quality_id);

    @Query("select q from Quality q where LOWER(q.qualityId)=LOWER(:quality_id) AND q.id!=:id")
    Optional<Quality> getQualityByIdExceptId(String quality_id, Long id);

    @Query("select q from Quality q where q.qualityName.id=:id")
    Optional<List<Quality>> getAllQualityByQualityNameId(Long id);


    @Query(value = "SELECT * FROM quality as q WHERE q.quality_id = :qualityId", nativeQuery = true)
    Optional<Quality> findByQualityId(String qualityId);

    @Modifying
    @Transactional
    @Query("update Quality q set q.wtPer100m=:per100m,q.mtrPerKg=:mtrPerKg where q.id=:qualityId")
    void updateQualityWtAndMtrKgById(Long qualityId, Double per100m, Double mtrPerKg);

    @Query("Select q from Quality q where id = :qualityId AND q.party.id =:partyId")
    Optional<Quality> getQualityByEntryIdAndPartyId(Long qualityId, Long partyId);

    @Query("select x from Quality x where x.id = (select s.quality.id  from StockMast s where s.id=:controlId)")
    Quality getQualityByStockId(Long controlId);

    @Query("select x from Quality x where LOWER(x.qualityId)=LOWER(:qualityId) AND x.id!=:id")
    Quality getQualityByIdWithExcept(String qualityId, Long id);

    @Query("select new com.main.glory.model.quality.response.QualityWithQualityNameParty(q,(select x.qualityName from QualityName x where x.id=q.qualityName.id)) from Quality q where q.id=:key")
    Optional<QualityWithQualityNameParty> findByIdWithQualityNameResponse(Long key);


    @Query("select x from Quality x where LOWER(x.qualityId)=LOWER(:qualityId) AND x.party.id=:partyId AND x.id!=:id")
    Quality getQualityIdIsExistExceptId(String qualityId, Long partyId, Long id);

    /*@Query("select q from Quality q where q.id IN(select s.quality.id  from StockBatch s where s.id=:stockId)")
    Quality getQualityByStockId(Long stockId);*/
}


