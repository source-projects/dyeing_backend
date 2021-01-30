package com.main.glory.Dao.dyeingProcess;

import com.main.glory.model.dyeingProcess.DyeingProcessData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface DyeingProcessDataDao extends JpaRepository<DyeingProcessData,Long> {


    @Query("select q from DyeingProcessData q where q.controlId=:id ")
    List<DyeingProcessData> findDyeingProcessDataByControlId(Long id);
}
