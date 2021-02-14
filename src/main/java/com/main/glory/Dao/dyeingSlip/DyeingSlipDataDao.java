package com.main.glory.Dao.dyeingSlip;

import com.main.glory.model.dyeingSlip.DyeingSlipData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DyeingSlipDataDao extends JpaRepository<DyeingSlipData,Long> {

    @Query("select x from DyeingSlipData x where x.controlId=:id")
    List<DyeingSlipData> findByControlId(Long id);
}
