package com.main.glory.Dao.machine;

import com.main.glory.model.machine.MachineCategory;
import com.main.glory.model.machine.MachineMast;
import com.main.glory.model.program.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;


@EnableJpaRepositories
public interface MachineCategoryDao extends  JpaRepository<MachineCategory,Long> {

    @Query("select c from MachineCategory c")
    List<MachineCategory> getAllMahineCategory();

    @Query("select q from MachineCategory q where q.id=:controlId")
    MachineCategory getCategoryById(Long controlId);
}
