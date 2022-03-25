package com.main.glory.Dao;

import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.shade.requestmodals.GetShadeByPartyAndQuality;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ShadeMastDao extends FilterDao<ShadeMast> {


//	String partyName;
//	String qualityName;
//	String qualityType;
//	String processName;

//	@Query(value = "Select sm.*, (Select party_name from party where entry_id = sm.party_id) as party_name, (Select quality_name from quality where id = sm.quality_id) as quality_name,(Select quality_type from quality where id = sm.quality_id) as quality_type from shade_mast as sm where sm.is_active = :Active", nativeQuery = true)
//	List<ShadeMastWithDetails> findDetailsByIsActive(@Param("Active") Boolean aBoolean);
	@Query("select s from ShadeMast s where s.createdBy.id=:createdBy")
	List<ShadeMast> findAllByCreatedBy(Long createdBy);

	@Query("select s from ShadeMast s where s.createdBy.id=:userHeadId OR s.userHeadData.id=:userHeadId")
	List<ShadeMast> findAllByUserHeadId(Long userHeadId);

	@Query("select new com.main.glory.model.shade.ShadeMast(x,(select d.processName from DyeingProcessMast d where d.id=x.processId)) from ShadeMast x where x.id=:aLong ")
	Optional<ShadeMast> findById(Long aLong);

	@Query("select new com.main.glory.model.shade.requestmodals.GetShadeByPartyAndQuality(s.id, s.partyShadeNo, s.colorTone,s.party.id,(select pp.partyCode from Party pp where pp.id=:partyId),(select pp.partyName from Party pp where pp.id=:partyId),s.quality.id,(select q.qualityId from Quality q where q.id=:qualityId) as qualityIdString,(select q.qualityName from QualityName q where q.id=(select qq.qualityName.id from Quality qq where qq.id=:qualityId)),s.colorName) from ShadeMast s where s.party.id = :partyId AND s.quality.id=:qualityId AND s.party.id IS NOT NULL AND s.quality.id IS NOT NULL")
    List<GetShadeByPartyAndQuality> findByQualityEntryIdAndPartyId(Long qualityId, Long partyId);

	@Query("select new com.main.glory.model.shade.requestmodals.GetShadeByPartyAndQuality(s.id, s.partyShadeNo, s.colorTone,s.party.id,(select pp.partyCode from Party pp where pp.id=:partyId),(select pp.partyName from Party pp where pp.id=:partyId),s.quality.id,(select q.qualityId from Quality q where q.id=:qualityId) as qualityIdString,(select q.qualityName from QualityName q where q.id=(select qq.qualityName.id from Quality qq where qq.id=:qualityId)),s.colorName) from ShadeMast s where s.party.id = :partyId AND s.quality.id=:qualityId AND (s.createdBy.id=:userId OR s.userHeadData.id=:userHeadId) AND s.party.id IS NOT NULL AND s.quality.id IS NOT NULL")
	List<GetShadeByPartyAndQuality> findByQualityEntryIdAndPartyId(Long qualityId, Long partyId,Long userId,Long userHeadId);

	@Query("select s from ShadeMast s where s.pending=false")
    List<ShadeMast> getAllShadeMast();

	@Query("select s from ShadeMast s where s.createdBy.id=:id OR s.userHeadData.id=:userHeadId")
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

	@Query("select s from ShadeMast s where s.party.id=:id")
    List<ShadeMast> getAllShadeByPartyId(Long id);

	@Query("select s from ShadeMast s where s.quality.id=:id")
    List<ShadeMast> getAllShadeByQualityId(Long id);

	@Query("select x from ShadeMast x where x.createdBy.id=:userId AND x.userHeadData.id=:userHeadId")
    List<ShadeMast> getAllShadeByCreatedByAndUserHeadId(Long userId, Long userHeadId);

	@Query("select x from ShadeMast x where x.partyShadeNo=:partyShadeNo AND x.quality.id=:qualityEntryId AND x.id!=:shadeId")
    ShadeMast getShadeByPartyShadeNoAndQualityEntryIdWithExceptShadeId(Long qualityEntryId, String partyShadeNo, Long shadeId);

	@Query("select x from ShadeMast x where LOWER(x.factoryShadeNo) = LOWER(:factoryShadeNo) AND x.id != :shadeId")
    ShadeMast getShadeMastByFactoryShadeNoExceptShadeId(Long shadeId,String factoryShadeNo);
}
