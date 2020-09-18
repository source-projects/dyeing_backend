package com.main.glory.Dao.qualityProcess;

import com.main.glory.model.qualityProcess.QualityProcessData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface QualityProcessDataDao extends JpaRepository<QualityProcessData, Long> {
	List<QualityProcessData> findByControlId(Long id);
}
