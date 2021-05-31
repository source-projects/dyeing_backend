package com.main.glory.Dao;

import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.shade.requestmodals.GetShadeByPartyAndQuality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ShadeMastDao extends JpaRepository<ShadeMast, Long> {


//	String partyName;
//	String qualityName;
//	String qualityType;
//	String processName;

//	@Query(value = "Select sm.*, (Select party_name from party where entry_id = sm.party_id) as party_name, (Select quality_name from quality where id = sm.quality_id) as quality_name,(Select quality_type from quality where id = sm.quality_id) as quality_type from shade_mast as sm where sm.is_active = :Active", nativeQuery = true)
//	List<ShadeMastWithDetails> findDetailsByIsActive(@Param("Active") Boolean aBoolean);
	@Query("select s from ShadeMast s where s.createdBy=:createdBy")
	List<ShadeMast> findAllByCreatedBy(Long createdBy);

	@Query("select s from ShadeMast s where s.createdBy=:userHeadId OR s.userHeadId=:userHeadId")
	List<ShadeMast> findAllByUserHeadId(Long userHeadId);
	Optional<ShadeMast> findById(Long aLong);

	@Query("select new com.main.glory.model.shade.requestmodals.GetShadeByPartyAndQuality(s.id, s.partyShadeNo, s.colorTone,s.partyId,(select pp.partyCode from Party pp where pp.id=:partyId),(select pp.partyName from Party pp where pp.id=:partyId),s.qualityEntryId,(select q.qualityId from Quality q where q.id=:qualityId) as qualityIdString,(select q.qualityName from QualityName q where q.id=(select qq.qualityNameId from Quality qq where qq.id=:qualityId))) from ShadeMast s where s.partyId = :partyId AND s.qualityEntryId=:qualityId AND s.partyId IS NOT NULL AND s.qualityEntryId IS NOT NULL")
    List<GetShadeByPartyAndQuality> findByQualityEntryIdAndPartyId(Long qualityId, Long partyId);

	@Query("select new com.main.glory.model.shade.requestmodals.GetShadeByPartyAndQuality(s.id, s.partyShadeNo, s.colorTone,s.partyId,(select pp.partyCode from Party pp where pp.id=:partyId),(select pp.partyName from Party pp where pp.id=:partyId),s.qualityEntryId,(select q.qualityId from Quality q where q.id=:qualityId) as qualityIdString,(select q.qualityName from QualityName q where q.id=(select qq.qualityNameId from Quality qq where qq.id=:qualityId))) from ShadeMast s where s.partyId = :partyId AND s.qualityEntryId=:qualityId AND (s.createdBy=:userId OR s.userHeadId=:userHeadId) AND s.partyId IS NOT NULL AND s.qualityEntryId IS NOT NULL")
	List<GetShadeByPartyAndQuality> findByQualityEntryIdAndPartyId(Long qualityId, Long partyId,Long userId,Long userHeadId);

	@Query("select s from ShadeMast s where s.pending=false")
    List<ShadeMast> getAllShadeMast();

	@Query("select s from ShadeMast s where s.createdBy=:id OR s.userHeadId=:userHeadId")
	List<ShadeMast> findAllByCreatedByAndHeadId(Long id, Long userHeadId);

	@Query("select s from ShadeMast s where s.id = :id")
    ShadeMast getShadeMastById(Long id);

	@Query("select s from ShadeMast s")
    List<ShadeMast> getAllPendingShadeMast();

	@Query("select s from ShadeMast s where s.processId=:id")
    List<ShadeMast> getAllShadeByProcessId(Long id);

	@Modifying
	@Transactional
	@Query("update ShadeMast s set s.processId=:processId where s.id=:shadeId")
	void updateProcessId(Long shadeId, Long processId);

	@Query("select s from ShadeMast s where s.id =(select ss.shadeId from ProductionPlan ss where ss.id=:productionId)")
    ShadeMast getShadeColorToneByProductionId(Long productionId);

	@Query("select s from ShadeMast s where s.partyId=:id")
    List<ShadeMast> getAllShadeByPartyId(Long id);

	@Query("select s from ShadeMast s where s.qualityEntryId=:id")
    List<ShadeMast> getAllShadeByQualityId(Long id);

	@Query("select x from ShadeMast x where x.createdBy=:userId AND x.userHeadId=:userHeadId")
    List<ShadeMast> getAllShadeByCreatedByAndUserHeadId(Long userId, Long userHeadId);

	@Query("select x from ShadeMast x where x.partyShadeNo=:partyShadeNo AND x.qualityEntryId=:qualityEntryId AND x.id!=:shadeId")
    ShadeMast getShadeByPartyShadeNoAndQualityEntryIdWithExceptShadeId(Long qualityEntryId, String partyShadeNo, Long shadeId);
}
