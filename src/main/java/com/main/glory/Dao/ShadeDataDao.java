package com.main.glory.Dao;

import com.main.glory.model.shade.ShadeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ShadeDataDao extends JpaRepository<ShadeData, Long> {

	@Transactional
	@Modifying
	@Query("update ShadeData s set s.isActive = 0 where s.controlId = :cid")
	public void setInactiveByControlId(@Param("cid") Long controlId);

	public List<ShadeData> findByIsActiveAndControlId(Boolean aBoolean, Long aLong);
	public List<ShadeData> findByStateAndControlId(String s, Long aLong);
}
