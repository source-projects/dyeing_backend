package com.main.glory.Dao.dyeingProcess;

import com.main.glory.model.dyeingProcess.DyeingChemicalData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface DyeingChemicalDataDao extends JpaRepository<DyeingChemicalData,Long> {

    @Query("select q from DyeingChemicalData q where q.controlId=:id")
    List<DyeingChemicalData> getChemicalListByControlId(Long id);
}
