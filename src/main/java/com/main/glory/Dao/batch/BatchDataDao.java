package com.main.glory.Dao.batch;

import com.main.glory.model.batch.BatchData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface BatchDataDao extends JpaRepository<BatchData, Long> {

}
