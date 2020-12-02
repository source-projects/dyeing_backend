package com.main.glory.Dao.qualityProcess;

import com.main.glory.model.qualityProcess.Chemical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface ChemicalDao extends JpaRepository<Chemical, Long> {
}
