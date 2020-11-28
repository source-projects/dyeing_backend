package com.main.glory.Dao.machine;

import com.main.glory.model.machine.MachineCategory;
import com.main.glory.model.machine.MachineMast;
import com.main.glory.model.program.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;



@EnableJpaRepositories
public interface MachineCategoryDao extends  JpaRepository<MachineCategory,Long> {

}
