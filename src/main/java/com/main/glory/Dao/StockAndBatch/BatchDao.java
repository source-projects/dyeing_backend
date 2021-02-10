package com.main.glory.Dao.StockAndBatch;


import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.response.BatchWithTotalMTRandFinishMTR;
import com.main.glory.model.StockDataBatchData.response.GetAllBatch;
import com.main.glory.model.StockDataBatchData.response.GetAllBatchResponse;
import com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface BatchDao extends  JpaRepository<BatchData, Long> {


    List<BatchData> findByControlId(Long controlId);

    //get total mtr based on stock it without extra batch mtr count
    @Query("select new com.main.glory.model.StockDataBatchData.response.GetAllBatchResponse(SUM(p.mtr) as MTR, SUM(p.wt) as WT, p.batchId as batchId) from BatchData p where p.controlId =:id AND isProductionPlanned = false AND isExtra=false GROUP BY p.batchId ")
    List<GetAllBatchResponse> findAllQTYControlId(@Param("id") Long id);

    Optional<List<BatchData>> findByBatchId(String batchId);


    @Query("select b from BatchData b where b.controlId=:controlId AND b.batchId=:batchId AND b.batchId IS NOT NULL AND b.controlId IS NOT NULL")
    List<BatchData> findByControlIdAndBatchId(Long controlId,String batchId);

    List<BatchData> findByControlIdAndBatchIdAndIsProductionPlanned(Long controlId, String batchId, Boolean b);

    List<BatchData> findByControlIdAndBatchIdAndIsExtra(Long controlId, String batchId, boolean b);

    //get the data for batch without extra batches
    @Query("select new com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId(p.batchId as batchId,p.controlId as controlId,SUM(p.wt) as WT,SUM(p.mtr) as MTR) from BatchData p where p.isProductionPlanned = false AND p.isExtra=false AND p.batchId IS NOT NULL AND p.controlId IS NOT NULL GROUP BY p.batchId,p.controlId ")
    List<GetBatchWithControlId> findAllBasedOnControlIdAndBatchId();


    //fetch the list of extra batch based on bill not generated
    @Query("select p from BatchData p where isExtra = true AND isBillGenrated = false")
    List<BatchData> findByControlIdAndBatchIdWithExtraBatch(Long controlId, String batchId);

    @Query("select p from BatchData p where p.batchId =:batchId AND p.controlId =:controlId AND isFinishMtrSave = true AND isBillGenrated = false ")
    List<BatchData> findByControlIdAndBatchIdWithFinishMtr(String batchId, Long controlId);

    //get all stoch grey mtr based on stock id
    @Query("select new com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId(p.batchId as batchId,p.controlId as controlId,SUM(p.wt) as WT,SUM(p.mtr) as MTR) from BatchData p where p.controlId=:id AND p.isProductionPlanned = true AND p.isFinishMtrSave=true GROUP BY p.batchId ")
    List<GetBatchWithControlId> getAllQtyByStockAndParty(Long id);



    //update the bill when the invoice is updated
    @Transactional
    @Modifying
    @Query("update BatchData b set b.isBillGenrated=false where b.id=:key")
    void updateBillStatus(Long key);

    //get the totat grey mtr based on stock id and batchId
    @Query("select new com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId(p.batchId as batchId,p.controlId as controlId,SUM(p.wt) as WT,SUM(p.mtr) as MTR) from BatchData p where p.controlId=:stockId AND p.batchId = :batchId AND p.isProductionPlanned = true AND p.isFinishMtrSave=true GROUP BY p.batchId ")
    GetBatchWithControlId findByBatchIdAndControId(String batchId, Long stockId);


    //get the batch with finish mtr detail based on stock id
    @Query("select new com.main.glory.model.StockDataBatchData.response.BatchWithTotalMTRandFinishMTR(p.batchId as batchId,p.controlId as controlId,SUM(p.wt) as WT,SUM(p.mtr) as MTR,SUM(p.finishMtr) as finishMtr,count(p.id) as count ) from BatchData p where p.controlId=:id AND p.isProductionPlanned = true AND p.isFinishMtrSave=true AND isBillGenrated=false GROUP BY p.batchId ")
    List<BatchWithTotalMTRandFinishMTR> getAllBatchByStockIdWithTotalFinishMtr(Long id);

    //get the totat grey mtr based on stock id and batchId
    @Query("select new com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId(p.batchId as batchId,p.controlId as controlId,SUM(p.wt) as WT,SUM(p.mtr) as MTR) from BatchData p where p.controlId=:stockId AND p.batchId = :batchId AND p.isProductionPlanned = true AND p.isFinishMtrSave=false AND p.wt IS NOT NULL")
    GetBatchWithControlId findByBatchIdAndControIdWithoutFinishMtr(String batchId, Long stockId);

    //get all batche without production plan
    @Query("select new com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId(p.batchId as batchId,p.controlId as controlId,SUM(p.wt) as WT,SUM(p.mtr) as MTR) from BatchData p where p.isProductionPlanned = false GROUP BY p.batchId,p.controlId ")
    List<GetBatchWithControlId> findAllBatcheWithoutProductionPlan();

    @Query("select b from BatchData b where b.id=:batchEntryId AND b.isFinishMtrSave=true")
    BatchData findByBatchEntryId(Long batchEntryId);

    //get Batch List with stockID
    @Query("select new com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId(p.batchId as batchId,p.controlId as controlId,SUM(p.wt) as WT,SUM(p.mtr) as MTR) from BatchData p where p.controlId = :id AND p.isProductionPlanned = false GROUP BY p.batchId,p.controlId ")
    List<GetBatchWithControlId> getBatchAndStockListWithoutProductionPlanByStockId(Long id);

    @Query("select SUM(b.wt) from BatchData b where b.isProductionPlanned = true AND b.controlId=:controlId AND b.batchId=:batchId GROUP BY b.batchId,b.controlId")
    Double getTotalWtByControlIdAndBatchId(Long controlId, String batchId);

    @Query("select b from BatchData b where b.batchId=:batchId AND b.controlId=:stockId AND b.isBillGenrated=true")
    List<BatchData> findBatchWithBillGenerated(String batchId, Long stockId);

    @Query("select new com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId(b.batchId,b.controlId,SUM(b.wt) as WT,SUM(b.mtr) as MTR) from BatchData b where b.batchId IS NOT NULL AND b.controlId IS NOT NULL GROUP BY b.batchId,b.controlId")
    List<GetBatchWithControlId> getAllBatchQty();


    @Query("select new com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId(b.batchId,b.controlId,SUM(b.wt) as WT,SUM(b.mtr) as MTR) from BatchData b where b.isProductionPlanned=false AND b.batchId IS NOT NULL AND b.controlId IS NOT NULL GROUP BY b.batchId,b.controlId")
    List<GetBatchWithControlId> getAllBatchQtyWithoutPlan();

    @Query("select new com.main.glory.model.StockDataBatchData.response.GetAllBatch(SUM(b.wt)as WT,b.controlId as controlId,b.batchId,b.isProductionPlanned,b.isBillGenrated,(select p.id from Party p where p.id=(select s.partyId from StockMast s where s.id=b.controlId)) as partyId,(select p.partyName from Party p where p.id=(select s.partyId from StockMast s where s.id=b.controlId))as partyName,(select q.id from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId)) as qId,(select q.qualityId from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId))as qualityId,(select q.qualityName from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId))as qualityName,(select q.qualityType from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId))as qualityType) from BatchData b where b.isBillGenrated=false AND b.controlId IS NOT NULL AND b.batchId IS NOT NULL GROUP BY b.batchId,b.controlId")
    List<GetAllBatch> getAllBatchWithoutBillGenerated();
}

