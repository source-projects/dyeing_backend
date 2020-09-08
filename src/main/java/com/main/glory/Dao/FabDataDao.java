package com.main.glory.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.main.glory.model.FabricInRecord;

@EnableJpaRepositories
public interface FabDataDao extends JpaRepository<FabricInRecord, Long>{

}
