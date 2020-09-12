package com.main.glory.Dao;

import com.main.glory.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface ProgramDao extends JpaRepository<Program,Long> {
}
