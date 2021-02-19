package com.main.glory.Dao.dyeingSlip;

import com.main.glory.model.dyeingSlip.DyeingSlipMast;
import com.main.glory.model.dyeingSlip.responce.GetAllAdditionalDyeingSlip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DyeingSlipMastDao extends JpaRepository<DyeingSlipMast,Long> {


    @Query("select q from DyeingSlipMast q where q.id=:id")
    DyeingSlipMast getDyeingSlipById(Long id);

    //DyeingSlipMast findByBatchIdAndStockId(String batchId, Long stockId);
    @Query("select q from DyeingSlipMast q")
    List<DyeingSlipMast> getAllDyeingSlip();
    @Query("select d from DyeingSlipMast d where d.productionId=:productionId AND d.batchId=:batchId")
    DyeingSlipMast findByBatchIdAndProductionId(String batchId, Long productionId);

    @Query("select d from DyeingSlipMast d where d.productionId=:productionId")
    DyeingSlipMast getDyeingSlipByProductionId(Long productionId);

    @Query("select new com.main.glory.model.dyeingSlip.responce.GetAllAdditionalDyeingSlip(d.id,d.stockId,d.jetId,d.productionId,d.batchId) from DyeingSlipMast d where d.id IN (select dd.controlId from DyeingSlipData dd where dd.processType='addition')")
    List<GetAllAdditionalDyeingSlip> getAllAddtionalDyeingProcess();


    @Query("select d from DyeingSlipMast d where d.id IN (select dd.controlId from DyeingSlipData dd where dd.controlId=:id AND dd.processType='addition')")
    DyeingSlipMast getAdditionalDyeingSlipById(Long id);

    @Transactional
    @Modifying
    @Query("delete from DyeingSlipMast d where d.id=:id")
    void deleteDyeingSlipById(Long id);

    @Query("select d from DyeingSlipMast d where d.approvedId = :id")
    List<DyeingSlipMast> getDyeingSlipByApprovedById(Long id);

    @Modifying
    @Transactional
    @Query("update DyeingSlipMast d set d.approvedId=:approved where d.id=:dyeingId")
    void updateDyeingWithApprovedId(Long approved, Long dyeingId);
}
