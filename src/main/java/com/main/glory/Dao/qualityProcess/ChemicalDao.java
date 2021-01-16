package com.main.glory.Dao.qualityProcess;

import com.main.glory.model.qualityProcess.Chemical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface ChemicalDao extends JpaRepository<Chemical, Long> {
    @Query("select c from Chemical c where c.qualityProcessControlId=:id ")
    List<Chemical> findByControlId(Long id);
}
