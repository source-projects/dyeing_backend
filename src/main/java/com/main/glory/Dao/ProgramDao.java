package com.main.glory.Dao;

import com.main.glory.model.program.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface ProgramDao extends JpaRepository<Program,Long> {
    List<Program> findByCreatedBy(Long createdBy);
    List<Program> findByUserHeadId(Long userHeadId);

    @Query("select p from Program p")
    List<Program> getAllProgramList();
}
