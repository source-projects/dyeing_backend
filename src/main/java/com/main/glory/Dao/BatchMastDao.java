package com.main.glory.Dao;

import com.main.glory.model.BatchMast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


public interface BatchMastDao extends JpaRepository<BatchMast, Long> {

}
