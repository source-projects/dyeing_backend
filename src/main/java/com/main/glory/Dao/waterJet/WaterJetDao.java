package com.main.glory.Dao.waterJet;

import com.main.glory.model.waterJet.WaterJet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface WaterJetDao extends JpaRepository<WaterJet, Long> {

}
