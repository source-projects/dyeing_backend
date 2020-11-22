package com.main.glory.Dao;

import com.main.glory.model.program.ProgramRecord;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProgramRecordDao  extends JpaRepository<ProgramRecord ,Long> {
}
