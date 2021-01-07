package com.main.glory.Dao.waterJet;

import com.main.glory.model.waterJet.WaterJet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface WaterJetDao extends JpaRepository<WaterJet, Long> {

    @Query("select j from WaterJet j")
    List<WaterJet> getAllWaterJet();
}
