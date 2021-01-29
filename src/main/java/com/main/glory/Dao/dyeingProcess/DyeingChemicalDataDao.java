package com.main.glory.Dao.dyeingProcess;

import com.main.glory.model.dyeingProcess.DyeingChemicalData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface DyeingChemicalDataDao extends JpaRepository<DyeingChemicalData,Long> {
}
