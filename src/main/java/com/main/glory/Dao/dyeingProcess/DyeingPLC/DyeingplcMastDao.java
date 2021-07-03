package com.main.glory.Dao.dyeingProcess.DyeingPLC;

import com.main.glory.model.dyeingProcess.DyeingPLC.DyeingplcMast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface DyeingplcMastDao extends JpaRepository<DyeingplcMast,Long> {

    @Query("select x from DyeingplcMast x where x.dyeingProcessMastId=:dyeingProcessMastId")
    DyeingplcMast getDyeingPlcMastByDyeingProcessMastId(Long dyeingProcessMastId);

    @Modifying
    @Transactional
    @Query("delete from DyeingplcMast x where x.id=:id")
    void deleteByEntryId(Long id);
}
