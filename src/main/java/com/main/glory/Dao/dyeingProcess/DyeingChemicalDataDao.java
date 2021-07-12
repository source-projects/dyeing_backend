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

    @Query("select q from DyeingChemicalData q where q.itemId=:id")
    List<DyeingChemicalData> getChemicalDataByItemId(Long id);

    @Query("select new com.main.glory.model.dyeingProcess.DyeingChemicalData(x,(select s.itemName from SupplierRate s where s.id= x.itemId) as itemname) from DyeingChemicalData x where x.controlId=:controlId")
    List<DyeingChemicalData> getChemicalListResponseByControlId(Long controlId);
}
