package com.main.glory.Dao;

import com.main.glory.model.shade.ShadeMast;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
