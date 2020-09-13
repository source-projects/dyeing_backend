package com.main.glory.Dao;

import com.main.glory.model.shade.ShadeData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShadeDataDao extends JpaRepository<ShadeData, Long> {
	public List<ShadeData> findByIsActiveAndControlId(Boolean aBoolean, Long aLong);
	public List<ShadeData> findByStateAndControlId(String s, Long aLong);
}
