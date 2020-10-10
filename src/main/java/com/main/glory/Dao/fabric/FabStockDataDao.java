package com.main.glory.Dao.fabric;

import com.main.glory.model.fabric.FabStockData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface FabStockDataDao extends JpaRepository<FabStockData, Long> {

}
