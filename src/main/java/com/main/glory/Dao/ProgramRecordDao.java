package com.main.glory.Dao;

import com.main.glory.model.ProgramRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


public interface ProgramRecordDao  extends JpaRepository<ProgramRecord ,Long> {
}
