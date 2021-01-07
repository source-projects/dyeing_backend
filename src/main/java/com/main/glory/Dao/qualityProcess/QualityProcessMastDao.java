package com.main.glory.Dao.qualityProcess;

import com.main.glory.model.qualityProcess.QualityProcessMast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface QualityProcessMastDao extends JpaRepository<QualityProcessMast, Long> {
    List<QualityProcessMast> findAllByCreatedBy(Long createdBy);
    List<QualityProcessMast> findAllByUserHeadId(Long userHeadId);

    @Query("select qp from QualityProcessMast qp")
    List<QualityProcessMast> getAllQualityProcess();
}
