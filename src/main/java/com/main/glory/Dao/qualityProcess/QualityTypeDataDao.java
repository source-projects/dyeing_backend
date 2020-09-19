package com.main.glory.Dao.qualityProcess;

import com.main.glory.model.qualityProcess.QualityTypeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface QualityTypeDataDao extends JpaRepository<QualityTypeData, Long> {
	List<QualityTypeData> findByControlId(Long aLong);
}
