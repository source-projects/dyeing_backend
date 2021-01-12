package com.main.glory.Dao;

import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.shade.requestmodals.GetShadeByPartyAndQuality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShadeMastDao extends JpaRepository<ShadeMast, Long> {


//	String partyName;
//	String qualityName;
//	String qualityType;
//	String processName;

//	@Query(value = "Select sm.*, (Select party_name from party where entry_id = sm.party_id) as party_name, (Select quality_name from quality where id = sm.quality_id) as quality_name,(Select quality_type from quality where id = sm.quality_id) as quality_type from shade_mast as sm where sm.is_active = :Active", nativeQuery = true)
//	List<ShadeMastWithDetails> findDetailsByIsActive(@Param("Active") Boolean aBoolean);
	List<ShadeMast> findAllByCreatedBy(Long createdBy);
	List<ShadeMast> findAllByUserHeadId(Long userHeadId);
	Optional<ShadeMast> findById(Long aLong);

	@Query("select new com.main.glory.model.shade.requestmodals.GetShadeByPartyAndQuality(s.id, s.partyShadeNo, s.colorTone,s.partyId,(select pp.partyCode from Party pp where pp.id=:partyId),(select pp.partyName from Party pp where pp.id=:partyId),s.qualityEntryId,(select q.qualityId from Quality q where q.id=:qualityId) as qualityIdString,(select q.qualityName from Quality q where q.id=:qualityId)) from ShadeMast s where s.partyId = :partyId AND s.qualityEntryId=:qualityId AND s.partyId IS NOT NULL AND s.qualityEntryId IS NOT NULL")
    List<GetShadeByPartyAndQuality> findByQualityEntryIdAndPartyId(Long qualityId, Long partyId);


	@Query("select s from ShadeMast s")
    List<ShadeMast> getAllShadeMast();
}
