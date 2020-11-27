package com.main.glory.Dao.machine;

import com.main.glory.model.machine.MachineRecord;
import com.main.glory.model.program.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface MachineRecordDao extends JpaRepository<MachineRecord,Long> {

}
