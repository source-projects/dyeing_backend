package com.main.glory.Dao;

import com.main.glory.model.shade.ShadeMast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ShadeMastDao extends JpaRepository<ShadeMast, Long> {

	@Transactional
	@Modifying
	@Query("update ShadeMast s set s.isActive = 0 where s.id = :eid")
	public void setInactiveById(@Param("eid") Long id);

	List<ShadeMast> findByIsActive(Boolean aBoolean);

//	String partyName;
//	String qualityName;
//	String qualityType;
//	String processName;

//	@Query(value = "Select sm.*, (Select party_name from party where entry_id = sm.party_id) as party_name, (Select quality_name from quality where id = sm.quality_id) as quality_name,(Select quality_type from quality where id = sm.quality_id) as quality_type from shade_mast as sm where sm.is_active = :Active", nativeQuery = true)
//	List<ShadeMastWithDetails> findDetailsByIsActive(@Param("Active") Boolean aBoolean);

	ShadeMast findByIdAndIsActive(Long aLong, Boolean aBoolean);
}
