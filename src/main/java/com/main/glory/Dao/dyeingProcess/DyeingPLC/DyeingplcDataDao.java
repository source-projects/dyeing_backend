package com.main.glory.Dao.dyeingProcess.DyeingPLC;


import com.main.glory.model.dyeingProcess.DyeingPLC.DyeingplcData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface DyeingplcDataDao extends JpaRepository<DyeingplcData,Long> {
    @Modifying
    @Transactional
    @Query("delete from DyeingplcData x where x.controlId=:controlId")
    void deleteByControlId(Long controlId);
}
