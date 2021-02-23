package com.main.glory.Dao;

import com.main.glory.model.program.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface ProgramDao extends JpaRepository<Program,Long> {
    List<Program> findByCreatedBy(Long createdBy);
    @Query("select p from Program p where p.createdBy=:userHeadId OR p.userHeadId=:userHeadId")
    List<Program> findByUserHeadId(Long userHeadId);

    @Query("select p from Program p")
    List<Program> getAllProgramList();

    @Query("select p from Program p where p.createdBy=:id OR p.userHeadId=:userHeadId")
    List<Program> findByUserHeadIdAndCreatedId(Long id, Long userHeadId);

    @Query("select p from Program p where p.qualityEntryId=:id")
    List<Program> getAllProgramByqualityId(Long id);
}
