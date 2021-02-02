package com.main.glory.Dao.dyeingSlip;

import com.main.glory.model.dyeingSlip.DyeingSlipMast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DyeingSlipMastDao extends JpaRepository<DyeingSlipMast,Long> {
    @Query("select q from DyeingSlipMast q where q.id=:id")
    DyeingSlipMast getDyeingSlipById(Long id);

    //DyeingSlipMast findByBatchIdAndStockId(String batchId, Long stockId);
    @Query("select q from DyeingSlipMast q")
    List<DyeingSlipMast> getAllDyeingSlip();
    @Query("select d from DyeingSlipMast d where d.productionId=:productionId AND d.batchId=:batchId")
    DyeingSlipMast findByBatchIdAndProductionId(String batchId, Long productionId);
}
