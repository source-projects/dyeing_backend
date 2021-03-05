package com.main.glory.Dao;

import com.main.glory.model.shade.ShadeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ShadeDataDao extends JpaRepository<ShadeData, Long> {

	@Query("select s from ShadeData s where s.controlId=:aLong")
	List<ShadeData> findByControlId(Long aLong);

	@Query("select s from ShadeData s where s.supplierItemId=:e")
    List<ShadeData> getShadeDataByItemid(Long e);
}
