package com.main.glory.Dao.qualityProcess;

import com.main.glory.model.qualityProcess.QualityProcessMast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface QualityProcessMastDao extends JpaRepository<QualityProcessMast, Long> {

}
