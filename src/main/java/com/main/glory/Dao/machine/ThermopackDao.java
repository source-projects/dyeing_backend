package com.main.glory.Dao.machine;

import com.main.glory.model.machine.Thermopack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface ThermopackDao extends JpaRepository<Thermopack,Long> {
}
