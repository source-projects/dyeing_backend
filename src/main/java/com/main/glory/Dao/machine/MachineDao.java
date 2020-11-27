package com.main.glory.Dao.machine;

import com.main.glory.model.machine.MachineMast;
import com.main.glory.model.program.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories
public interface MachineDao extends JpaRepository<MachineMast,Long> {
    Optional<MachineMast> findByMachineName(String machineName);
}
