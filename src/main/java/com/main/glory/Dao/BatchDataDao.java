package com.main.glory.Dao;

import com.main.glory.model.BatchData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface BatchDataDao extends JpaRepository<BatchData, Long> {

}
