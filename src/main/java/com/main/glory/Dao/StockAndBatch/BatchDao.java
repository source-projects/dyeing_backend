package com.main.glory.Dao.StockAndBatch;


import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.request.BatchDetail;
import com.main.glory.model.StockDataBatchData.request.WTByStockAndBatch;
import com.main.glory.model.StockDataBatchData.response.*;
import com.main.glory.model.dispatch.request.QualityBillByInvoiceNumber;
import com.main.glory.model.dispatch.response.BatchListWithInvoice;
import com.main.glory.model.dispatch.response.GetBatchByInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface BatchDao extends  JpaRepository<BatchData, Long> {


    List<BatchData> findByControlId(Long controlId);

    //get total mtr based on stock it without extra batch mtr count
    @Query("select new com.main.glory.model.StockDataBatchData.response.GetAllBatchResponse(SUM(p.mtr) as MTR, SUM(p.wt) as WT, p.batchId as batchId) from BatchData p where p.controlId =:id AND isProductionPlanned = false AND isExtra=false GROUP BY p.batchId ")
    List<GetAllBatchResponse> findAllQTYControlId(@Param("id") Long id);


    //getAll batch by stock ud
    @Query("select new com.main.glory.model.StockDataBatchData.response.GetAllBatchResponse(SUM(p.mtr) as MTR, SUM(p.wt) as WT, p.batchId as batchId) from BatchData p where p.controlId =:id GROUP BY p.batchId ")
    List<GetAllBatchResponse> findAllBatchesByControlId(@Param("id") Long id);

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

    @Query("select SUM(b.wt) from BatchData b where b.controlId=:controlId AND b.batchId=:batchId GROUP BY b.batchId,b.controlId")
    Double getTotalWtByControlIdAndBatchId(Long controlId, String batchId);

    @Query("select b from BatchData b where b.batchId=:batchId AND b.controlId=:stockId AND b.isBillGenrated=true")
    List<BatchData> findBatchWithBillGenerated(String batchId, Long stockId);

    @Query("select new com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId(b.batchId,b.controlId,SUM(b.wt) as WT,SUM(b.mtr) as MTR) from BatchData b where b.batchId IS NOT NULL AND b.controlId IS NOT NULL GROUP BY b.batchId,b.controlId")
    List<GetBatchWithControlId> getAllBatchQty();


    @Query("select new com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId(b.batchId,b.controlId,SUM(b.wt) as WT,SUM(b.mtr) as MTR) from BatchData b where b.isProductionPlanned=false AND b.batchId IS NOT NULL AND b.controlId IS NOT NULL GROUP BY b.batchId,b.controlId")
    List<GetBatchWithControlId> getAllBatchQtyWithoutPlan();

    @Query("select new com.main.glory.model.StockDataBatchData.response.GetAllBatch(SUM(b.wt)as WT,b.controlId as controlId,b.batchId,b.isProductionPlanned,b.isBillGenrated,(select p.id from Party p where p.id=(select s.partyId from StockMast s where s.id=b.controlId)) as partyId,(select p.partyName from Party p where p.id=(select s.partyId from StockMast s where s.id=b.controlId))as partyName,(select q.id from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId)) as qId,(select q.qualityName from QualityName q where q.id=(select qq.qualityNameId from Quality qq where qq.id=(select s.qualityId from StockMast s where s.id=b.controlId)))as qualityId,(select q.qualityName from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId))as qualityName,(select q.qualityType from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId))as qualityType) from BatchData b where b.isBillGenrated=false AND b.isProductionPlanned=true AND b.controlId IS NOT NULL AND b.batchId IS NOT NULL GROUP BY b.batchId,b.controlId")
    List<GetAllBatch> getAllBatchWithoutBillGenerated();

    @Query("select new com.main.glory.model.StockDataBatchData.response.GetAllBatch(SUM(b.wt)as WT,b.controlId as controlId,b.batchId,b.isProductionPlanned,b.isBillGenrated,(select p.id from Party p where p.id=(select s.partyId from StockMast s where s.id=b.controlId)) as partyId,(select p.partyName from Party p where p.id=(select s.partyId from StockMast s where s.id=b.controlId))as partyName,(select q.id from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId)) as qId,(select q.qualityName from QualityName q where q.id=(select qq.qualityNameId from Quality qq where qq.id=(select s.qualityId from StockMast s where s.id=b.controlId)))as qualityId,(select q.qualityName from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId))as qualityName,(select q.qualityType from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId))as qualityType) from BatchData b where b.isBillGenrated=false AND b.isProductionPlanned=true AND b.controlId IN (select ss from StockMast ss where ss.createdBy=:userId OR ss.userHeadId=:userHeadId) AND b.batchId IS NOT NULL GROUP BY b.batchId,b.controlId")
    List<GetAllBatch> getAllBatchWithoutBillGenerated(Long userId,Long userHeadId);



    //batches by stock id
    @Query("select new com.main.glory.model.dispatch.response.GetBatchByInvoice(SUM(b.id)as wt,b.batchId,b.controlId) from BatchData b where b.isProductionPlanned=true AND b.isBillGenrated=false AND b.controlId=:id GROUP BY b.batchId,b.controlId")
    List<GetBatchByInvoice> getBatcheByStockIdWithoutBillGenerated(Long id);


    @Query("select SUM(b.wt) from BatchData b where b.batchId=:batchId AND b.controlId=:stockId")
    Double getAllBatchQtyByBatchIdAndStockId(String batchId, Long stockId);

    @Query("select new com.main.glory.model.StockDataBatchData.request.WTByStockAndBatch(b.batchId,b.controlId,SUM(b.wt)) from BatchData b where b.batchId=:batchId AND b.controlId=:stockId")
    WTByStockAndBatch getWtByStockAndBatchId(String batchId, Long stockId);

    @Query("select s from BatchData s where s.id=:key")
    BatchData getBatchDataById(Long key);

    @Modifying
    @Transactional
    @Query("update BatchData b set b.isProductionPlanned=:b where b.id=:id")
    void updateProductionPlanned(Long id, boolean b);

    @Query(value = "select * from batch_data as b where b.batch_id=:name AND b.control_id!=:id ORDER BY b.id LIMIT 1",nativeQuery = true)
    Optional<BatchData> isBatchUnique(String name, Long id);

    @Query("select SUM(b.mtr) from BatchData b where b.controlId=:stockId AND b.batchId=:batchId")
    Double getTotalMtrByControlIdAndBatchId(Long stockId, String batchId);


    @Query("select new com.main.glory.model.StockDataBatchData.response.BatchWithTotalMTRandFinishMTR(b.batchId,b.controlId,SUM(b.wt),SUM(b.mtr),SUM(b.finishMtr),COUNT(b.id)) from BatchData b where b.controlId=:stockId AND b.batchId=:batchId")
    BatchWithTotalMTRandFinishMTR getAllBatchWithTotalMtrAndTotalFinishMtr(String batchId, Long stockId);


    //get the quality bill responce by stock and batch id 3rd parameter for bill generated or not
   /* @Query("select new com.main.glory.model.dispatch.request.QualityBillByInvoiceNumber(" +
            "(select q.qualityId from Quality q where q.id=(select sm.qualityId from StockMast sm where sm.id=:stockId)) AS qualityId," +
            "(select q.qualityName from QualityName q where q.id=(select qq.qualityNameId from Quality qq where qq.id=(select sm.qualityId from StockMast sm where sm.id=:stockId))) AS qualityName," +
            "(select q.rate from Quality q where q.id=(select sm.qualityId from StockMast sm where sm.id=:stockId)) AS rate," +
            "(select q.HSN from Quality q where q.id=(select sm.qualityId from StockMast sm where sm.id=:stockId)) AS HSN," +
            ":batchId AS batchId," +
            "SUM(b.mtr) AS totalMtr," +
            "SUM(b.finishMtr) AS totalFinishMtr," +
            "COUNT(b.id) AS totalPcs," +
            "(select sm.chlNo from StockMast sm where sm.id=:stockId) AS pchalNo" +
            ") from BatchData b where b.batchId=:batchId AND b.controlId=:stockId AND b.isBillGenrated=:flag")
    QualityBillByInvoiceNumber getQualityBillByStockAndBatchId(Long stockId, String batchId, boolean flag);*/



    @Query("select b from BatchData b where b.batchId=:batchId AND b.controlId=:stockId AND b.isBillGenrated=:flag ")
    List<BatchData> getBatchRecordByBillGeneratedFlag(Long stockId, String batchId, boolean flag);

    @Query("select SUM(b.finishMtr) from BatchData b where b.batchId=:batchId AND b.controlId=:stockId")
    Double getTotalFinishMtrByBatchAndStock(String batchId, Long stockId);

    @Query("select count(x.id) from BatchData x where x.batchId=:batchId AND x.controlId=:stockId")
    Long getTotalPcsByBatchAndStockId(Long stockId, String batchId);


    /*//for party report batch detail
    @Query("select new com.main.glory.model.StockDataBatchData.request.BatchDetail(b.controlId,b.batchId,b.isProductionPlanned,b.isBillGenrated,b.isFinishMtrSave,(:qualityId)as qualityEntrI," +
            "(select q.qualityName from QualityName q where q.id=(select qq.qualityNameId from Quality qq where qq.id=:qualityId))as qualityNam," +
            "(select qq.qualityId from Quality qq where qq.id=:qualityId)as qualityI," +
            "count(b.id),sum(b.mtr),(select s.receiveDate from StockMast s where s.id=b.controlId)" +
            ") from BatchData b where b.batchId IS NOT NULL AND b.controlId IS NULL AND b.controlId = (select sm.id from StockMast sm where sm.partyId=:partyId AND sm.qualityId=:qualityId) GROUP BY b.batchId")
    List<BatchDetail> getBatchDetailForReportByPartyIdAndQualityId(Long partyId, Long qualityId);
*/

    @Query("select new com.main.glory.model.StockDataBatchData.request.BatchDetail(b.controlId,b.batchId,b.isProductionPlanned,b.isBillGenrated,b.isFinishMtrSave," +
            "count(b.id),sum(b.mtr),sum(b.finishMtr),(select s.receiveDate from StockMast s where s.id=:id)" +
            ") from BatchData b where b.batchId IS NOT NULL AND b.controlId = :id GROUP BY b.batchId,b.controlId,b.isProductionPlanned,b.isBillGenrated,b.isFinishMtrSave")
    List<BatchDetail> getBatchDetailByStockId(Long id);


    @Query("select new com.main.glory.model.StockDataBatchData.response.GetAllBatch(SUM(b.wt)as WT,b.controlId as controlId,b.batchId,(select p.id from Party p where p.id=(select s.partyId from StockMast s where s.id=b.controlId)) as partyId,(select p.partyName from Party p where p.id=(select s.partyId from StockMast s where s.id=b.controlId))as partyName,(select q.id from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId)) as qId,(select q.qualityId from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId))as qualityId,(select q.qualityName from QualityName q where q.id=(select qq.qualityNameId from Quality qq where qq.id=(select s.qualityId from StockMast s where s.id=b.controlId)))as qualityName,(select q.qualityType from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId))as qualityType) from BatchData b where b.controlId=:stockId AND b.batchId=:batchId GROUP BY b.batchId")
    GetAllBatch getBatchForAdditionalSlipByBatchAndStock(Long stockId, String batchId);


    @Query("select new com.main.glory.model.StockDataBatchData.response.GetAllBatch(SUM(b.wt)as WT,b.controlId as controlId,b.batchId,(select p.id from Party p where p.id=(select s.partyId from StockMast s where s.id=b.controlId)) as partyId,(select p.partyName from Party p where p.id=(select s.partyId from StockMast s where s.id=b.controlId))as partyName,(select q.id from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId)) as qId,(select q.qualityId from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId))as qualityId,(select q.qualityName from QualityName q where q.id=(select qq.qualityNameId from Quality qq where qq.id=(select s.qualityId from StockMast s where s.id=b.controlId)))as qualityName,(select q.qualityType from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId))as qualityType) from BatchData b where b.controlId=:stockId AND b.batchId=:batchId AND b.controlId =(select ss.id from StockMast ss where (ss.createdBy=:userId OR ss.userHeadId=:userHeadId) AND ss.id=:stockId) GROUP BY b.batchId")
    GetAllBatch getBatchForAdditionalSlipByBatchAndStock(Long stockId, String batchId,Long userId,Long userHeadId);

    @Query("select b.isFinishMtrSave from BatchData b where b.batchId=:batchId AND b.controlId=:stockId GROUP BY b.controlId,b.batchId,b.isFinishMtrSave")
    boolean isFinishMtrSave(String batchId, Long stockId);

    //get the data for batch without extra batches
    @Query("select new com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId(p.batchId as batchId,p.controlId as controlId,SUM(p.wt) as WT,SUM(p.mtr) as MTR) from BatchData p where p.isProductionPlanned = false AND p.isExtra=false AND p.batchId IS NOT NULL AND p.controlId IN (select ss.id from StockMast ss where ss.createdBy=:userId OR ss.userHeadId=:userHeadId) GROUP BY p.batchId,p.controlId ")
    List<GetBatchWithControlId> findAllBasedOnControlIdAndBatchIdByCreatedAndHeadId(Long userId, Long userHeadId);

    @Query(value = "select * from batch_data as b where b.merge_batch_id=:batchId LIMIT 1",nativeQuery = true)
    BatchData getMergeBatchExist(@RequestParam(name = "batchId") String batchId);

    @Query("select x from BatchData x where x.mergeBatchId=:batchId")
    List<BatchData> getMergeBatchListByMergeBatchId(String batchId);

    @Query("select x.id from BatchData x where x.mergeBatchId=:batchId")
    List<Long> getMergeBatchIdListByMergeBatchId(String batchId);

    @Modifying
    @Transactional
    @Query("update BatchData b set b.mergeBatchId=:batchId where b.id=:e")
    void updateMergeIdByBatchEntryId(Long e, String batchId);

    @Query("select new com.main.glory.model.StockDataBatchData.response.MergeBatchId(sum(b.mtr) as sum,b.mergeBatchId) from BatchData b where b.mergeBatchId IS NOT NULL GROUP BY b.mergeBatchId ")
    List<MergeBatchId> getAllMergeBatchId();

    @Query("select new com.main.glory.model.StockDataBatchData.response.MergeBatchId(sum(b.mtr) as sum,b.mergeBatchId) from BatchData b where b.mergeBatchId =:mergeBatchId ")
    MergeBatchId getMergeBatchByMergeBatchId(String mergeBatchId);

    @Query("select x from BatchData x where x.mergeBatchId = :mergeBatchId")
    List<BatchData> getMergeBatchDataByMergeBatchId(String mergeBatchId);
/*
    @Query("select new com.main.glory.model.StockDataBatchData.response.GetAllBatch(SUM(b.wt)as WT,b.controlId as controlId,b.batchId,b.isProductionPlanned,b.isBillGenrated,(select p.id from Party p where p.id=(select s.partyId from StockMast s where s.id=b.controlId)) as partyId,(select p.partyName from Party p where p.id=(select s.partyId from StockMast s where s.id=b.controlId))as partyName,(select q.id from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId)) as qId,(select q.qualityId from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId))as qualityId,(select q.qualityName from QualityName q where q.id=(select qq.qualityNameId from Quality qq where qq.id=(select s.qualityId from StockMast s where s.id=b.controlId)))as qualityName,(select q.qualityType from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId))as qualityType) from BatchData b where GROUP BY b.controlId, b.batchId")
    List<GetAllBatch> getAllBatchWithoutBillGeneratedAndFinishMtrSave();*/
}

