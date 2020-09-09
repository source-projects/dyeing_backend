package com.main.glory.Dao;

import com.main.glory.model.BatchGrDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface BatchGrDetailDao extends JpaRepository<BatchGrDetail, Long> {
}
