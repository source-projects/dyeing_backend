package com.main.glory.Dao.machine;

import com.main.glory.model.machine.MachineRecord;
import com.main.glory.model.program.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@EnableJpaRepositories
public interface MachineRecordDao extends JpaRepository<MachineRecord,Long> {

    @Query("select s from MachineRecord s where s.controlId=:id")
    List<MachineRecord> getMachineRecordByControlId(Long id);

    @Modifying
    @Transactional
    @Query("update MachineRecord m set m.controlId=:id1 where m.id=:id")
    void updateMachineRecord(Long id, Long id1);
}
