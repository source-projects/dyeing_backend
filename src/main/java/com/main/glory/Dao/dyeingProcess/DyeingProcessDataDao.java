package com.main.glory.Dao.dyeingProcess;

import com.main.glory.model.dyeingProcess.DyeingProcessData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface DyeingProcessDataDao extends JpaRepository<DyeingProcessData,Long> {
}
