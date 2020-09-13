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
	ShadeMast findByIdAndIsActive(Long aLong, Boolean aBoolean);
}
