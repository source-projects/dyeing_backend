package com.main.glory.Dao;

import com.main.glory.model.shade.ShadeMast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShadeMastDao extends JpaRepository<ShadeMast, Long> {
	List<ShadeMast> findByIsActive(Boolean aBoolean);
	ShadeMast findByIdAndIsActive(Long aLong, Boolean aBoolean);
}
