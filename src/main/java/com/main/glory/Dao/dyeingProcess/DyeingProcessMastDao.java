package com.main.glory.Dao.dyeingProcess;

import com.main.glory.model.dyeingProcess.DyeingProcessMast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface DyeingProcessMastDao extends JpaRepository<DyeingProcessMast,Long> {
    @Query("select q from DyeingProcessMast q")
    List<DyeingProcessMast> getAllProcess();
}
