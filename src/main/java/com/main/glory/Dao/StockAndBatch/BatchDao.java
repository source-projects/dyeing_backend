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
    @Query("select new com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId(p.batchId as batchId,p.controlId as controlId,SUM(p.wt) as WT,SUM(p.mtr) as MTR) from BatchData p where p.isProductionPlanned = false AND p.isExtra=false AND p.batchId IS NOT NULL AND p.controlId IS NOT NULL AND p.mergeBatchId IS NULL GROUP BY p.batchId,p.controlId ")
    List<GetBatchWithControlId> findAllBasedOnControlIdAndBatchId();

    //get the data for batch without extra batches
    @Query("select new com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId(p.mergeBatchId, p.batchId as batchId,p.controlId as controlId,SUM(p.wt) as WT,SUM(p.mtr) as MTR) from BatchData p where p.isProductionPlanned = false AND p.isExtra=false AND p.batchId IS NOT NULL AND p.mergeBatchId IS NOT NULL AND p.controlId IS NOT NULL GROUP BY p.mergeBatchId,p.batchId,p.controlId")
    List<GetBatchWithControlId> findAllBasedOnControlIdAndBatchIdAndMergeBatchId();


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
    @Query("select new com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId(p.batchId as batchId,p.controlId as controlId,SUM(p.wt) as WT,SUM(p.mtr) as MTR) from BatchData p where p.controlId=:stockId AND p.batchId = :batchId AND p.isProductionPlanned = true AND p.isFinishMtrSave=true ")
    GetBatchWithControlId findByBatchIdAndControId(String batchId, Long stockId);


    //get the batch with finish mtr detail based on stock id
    @Query("select new com.main.glory.model.StockDataBatchData.response.BatchWithTotalMTRandFinishMTR(p.batchId as batchId,p.controlId as controlId,SUM(p.wt) as WT,SUM(p.mtr) as MTR,SUM(p.finishMtr) as finishMtr,count(p.id) as count ) from BatchData p where p.controlId=:id AND p.isProductionPlanned = true AND p.isFinishMtrSave=true AND isBillGenrated=false GROUP BY p.batchId,p.controlId")
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
    @Query("select new com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId(p.batchId as batchId,p.controlId as controlId,SUM(p.wt) as WT,SUM(p.mtr) as MTR) from BatchData p where p.controlId = :id AND p.isProductionPlanned = false AND p.mergeBatchId IS NULL GROUP BY p.batchId,p.controlId")
    List<GetBatchWithControlId> getBatchAndStockListWithoutProductionPlanByStockId(Long id);

    @Query("select SUM(b.wt) from BatchData b where b.controlId=:controlId AND b.batchId=:batchId GROUP BY b.batchId,b.controlId")
    Double getTotalWtByControlIdAndBatchId(Long controlId, String batchId);

    @Query("select b from BatchData b where b.batchId=:batchId AND b.controlId=:stockId AND b.isBillGenrated=true")
    List<BatchData> findBatchWithBillGenerated(String batchId, Long stockId);

    @Query("select new com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId(b.batchId,b.controlId,SUM(b.wt) as WT,SUM(b.mtr) as MTR) from BatchData b where b.batchId IS NOT NULL AND b.controlId IS NOT NULL GROUP BY b.batchId,b.controlId")
    List<GetBatchWithControlId> getAllBatchQty();


    @Query("select new com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId(b.batchId,b.controlId,SUM(b.wt) as WT,SUM(b.mtr) as MTR) from BatchData b where b.isProductionPlanned=false AND b.batchId IS NOT NULL AND b.controlId IS NOT NULL GROUP BY b.batchId,b.controlId")
    List<GetBatchWithControlId> getAllBatchQtyWithoutPlan();

    @Query("select new com.main.glory.model.StockDataBatchData.response.GetAllBatch(SUM(b.wt)as WT,b.controlId as controlId,b.batchId,b.isProductionPlanned,b.isBillGenrated,(select p.id from Party p where p.id=(select s.partyId from StockMast s where s.id=b.controlId)) as partyId,(select p.partyName from Party p where p.id=(select s.partyId from StockMast s where s.id=b.controlId))as partyName,(select q.id from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId)) as qId,(select q.qualityName from QualityName q where q.id=(select qq.qualityNameId from Quality qq where qq.id=(select s.qualityId from StockMast s where s.id=b.controlId)))as qualityId,(select q.qualityName from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId))as qualityName,(select q.qualityType from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId))as qualityType) from BatchData b where b.isBillGenrated=false AND b.isProductionPlanned=true AND b.mergeBatchId IS NULL AND b.controlId IS NOT NULL AND b.batchId IS NOT NULL GROUP BY b.batchId,b.controlId")
    List<GetAllBatch> getAllBatchWithoutBillGenerated();

    @Query("select new com.main.glory.model.StockDataBatchData.response.GetAllBatch(SUM(b.wt)as WT,b.controlId as controlId,b.batchId,b.isProductionPlanned,b.isBillGenrated,(select p.id from Party p where p.id=(select s.partyId from StockMast s where s.id=b.controlId)) as partyId,(select p.partyName from Party p where p.id=(select s.partyId from StockMast s where s.id=b.controlId))as partyName,(select q.id from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId)) as qId,(select q.qualityName from QualityName q where q.id=(select qq.qualityNameId from Quality qq where qq.id=(select s.qualityId from StockMast s where s.id=b.controlId)))as qualityId,(select q.qualityName from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId))as qualityName,(select q.qualityType from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId))as qualityType) from BatchData b where b.isBillGenrated=false AND b.isProductionPlanned=true AND b.controlId IN (select ss from StockMast ss where ss.createdBy=:userId OR ss.userHeadId=:userHeadId) AND b.batchId IS NOT NULL GROUP BY b.batchId,b.controlId")
    List<GetAllBatch> getAllBatchWithoutBillGenerated(Long userId,Long userHeadId);



    //batches by stock id
    @Query("select new com.main.glory.model.dispatch.response.GetBatchByInvoice(SUM(b.id)as wt,b.batchId,b.controlId) from BatchData b where b.isProductionPlanned=true AND b.isBillGenrated=false AND b.controlId=:id AND b.mergeBatchId IS NULL GROUP BY b.batchId,b.controlId")
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

    @Query("select count(x.id) from BatchData x where x.batchId=:batchId AND x.controlId=:stockId AND x.isFinishMtrSave=true AND x.isBillGenrated=false")
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
            "count(b.id),sum(b.mtr),sum(b.finishMtr),(select s.receiveDate from StockMast s where s.id=:id)," +
            "b.mergeBatchId) from BatchData b where b.batchId IS NOT NULL AND b.controlId = :id GROUP BY b.batchId,b.controlId,b.isProductionPlanned,b.isBillGenrated,b.isFinishMtrSave,b.mergeBatchId")
    List<BatchDetail> getBatchDetailByStockId(Long id);


    /*@Query("select new com.main.glory.model.StockDataBatchData.response.GetAllBatch(SUM(b.wt)as WT,b.controlId as controlId,b.batchId,(select p.id from Party p where p.id=(select s.partyId from StockMast s where s.id=b.controlId)) as partyId,(select p.partyName from Party p where p.id=(select s.partyId from StockMast s where s.id=b.controlId))as partyName,(select q.id from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId)) as qId,(select q.qualityId from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId))as qualityId,(select q.qualityName from QualityName q where q.id=(select qq.qualityNameId from Quality qq where qq.id=(select s.qualityId from StockMast s where s.id=b.controlId)))as qualityName,(select q.qualityType from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId))as qualityType) from BatchData b where b.mergeBatchId IS NULL b.batchId=:batchId GROUP BY b.batchId,b.controlId")
    GetAllBatch getBatchForAdditionalSlipByBatchAndStock(String batchId);


    @Query("select new com.main.glory.model.StockDataBatchData.response.GetAllBatch(SUM(b.wt)as WT,b.controlId as controlId,b.batchId,(select p.id from Party p where p.id=(select s.partyId from StockMast s where s.id=b.controlId)) as partyId,(select p.partyName from Party p where p.id=(select s.partyId from StockMast s where s.id=b.controlId))as partyName,(select q.id from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId)) as qId,(select q.qualityId from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId))as qualityId,(select q.qualityName from QualityName q where q.id=(select qq.qualityNameId from Quality qq where qq.id=(select s.qualityId from StockMast s where s.id=b.controlId)))as qualityName,(select q.qualityType from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId))as qualityType) from BatchData b where b.batchId=:batchId AND b.mergeBatchId IS NULL AND b.controlId =(select ss.id from StockMast ss where (ss.createdBy=:userId OR ss.userHeadId=:userHeadId) AND ss.id=:stockId) GROUP BY b.batchId,b.controlId")
    GetAllBatch getBatchForAdditionalSlipByBatchAndStock(String batchId,Long userId,Long userHeadId);
*/
    @Query("select b.isFinishMtrSave from BatchData b where b.batchId=:batchId AND b.controlId=:stockId GROUP BY b.controlId,b.batchId,b.isFinishMtrSave")
    boolean isFinishMtrSave(String batchId);

    //get the data for batch without extra batches
    @Query("select new com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId(p.batchId as batchId,p.controlId as controlId,SUM(p.wt) as WT,SUM(p.mtr) as MTR) from BatchData p where p.isProductionPlanned = false AND p.isExtra=false AND p.batchId IS NOT NULL AND p.mergeBatchId IS NULL AND p.controlId IN (select ss.id from StockMast ss where ss.createdBy=:userId OR ss.userHeadId=:userHeadId) GROUP BY p.batchId,p.controlId ")
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

    @Query("select new com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId(p.mergeBatchId,p.batchId as batchId,p.controlId as controlId,SUM(p.wt) as WT,SUM(p.mtr) as MTR) from BatchData p where p.isProductionPlanned = false AND p.isExtra=false AND p.batchId IS NOT NULL AND p.mergeBatchId IS NOT NULL AND  p.controlId IN (select ss.id from StockMast ss where ss.createdBy=:userId OR ss.userHeadId=:userHeadId) GROUP BY p.mergeBatchId,p.batchId,p.controlId ")
    List<GetBatchWithControlId> findAllBasedOnControlIdAndBatchIdAndMergeBatchIdByCreatedAndHeadId(Long userId, Long userHeadId);

    @Query("select new com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId(p.batchId as batchId,p.controlId as controlId,SUM(p.wt) as WT,SUM(p.mtr) as MTR) from BatchData p where p.isProductionPlanned = false AND p.isExtra=false AND p.batchId IS NOT NULL AND p.controlId IS NOT NULL AND p.mergeBatchId =:mergeBatchId GROUP BY p.batchId,p.controlId ")
    List<GetBatchWithControlId> getBatcheAndStockIdByMergeBatchId(String mergeBatchId);

    @Query("select sum(b.mtr) from BatchData b where b.mergeBatchId=:mergeBatchId")
    Double getTotalMtrByMergeBatchId(String mergeBatchId);

    @Query("select sum(b.wt) from BatchData b where b.mergeBatchId=:mergeBatchId")
    Double getTotalWtByMergeBatchId(String mergeBatchId);

    @Query(value = "select * from batch_data where merge_batch_id=:batchId LIMIT 1",nativeQuery = true)
    BatchData getIsMergeBatchId(@Param("batchId") String batchId);

    @Query(value = "select * from batch_data where batch_id=:batchId LIMIT 1",nativeQuery = true)
    BatchData getIsBatchId(@Param("batchId")String batchId);

    @Query("select x from BatchData x where x.batchId=:batchId AND x.mergeBatchId IS NULL")
    List<BatchData> getBatchByBatchId(String batchId);

    @Query("select x from BatchData x where x.mergeBatchId=:mergeBatchId")
    List<BatchData> getBatchByMergeBatchId(String mergeBatchId);

    @Query("select sum(b.wt) from BatchData b where b.batchId=:batchId AND b.mergeBatchId IS NULL")
    Double getTotalWtByBatchId(String batchId);

    @Query("select sum(b.wt) from BatchData b where b.isProductionPlanned =true And b.isFinishMtrSave=false AND b.batchId=:batchId")
    Double getBatchWithoutFinishMtrQTYById(String batchId);
    @Query("select sum(b.wt) from BatchData b where b.isProductionPlanned =true And b.isFinishMtrSave=false AND b.mergeBatchId=:batchId")
    Double getBatchWithoutFinishMtrQTYByMergeId(String batchId);

    @Query("select sum(b.mtr) from BatchData b where b.batchId=:batchId")
    Double getTotalMtrByBatchId(String batchId);

    @Query(value = "select control_id from batch_data where batch_id =:e LIMIT 1",nativeQuery = true)
    Long getControlIdByBatchId(String e);


    //user filter the record
    /*@Query("select new com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId(p.batchId as batchId,p.controlId as controlId,SUM(p.wt) as WT,SUM(p.mtr) as MTR) from BatchData p where p.mergeBatchId=:e AND p.controlId IN (select ss.id from StockMast ss where ss.createdBy=:userId OR ss.userHeadId=:userHeadId) GROUP BY p.batchId ")
    List<GetBatchWithControlId> getAllBatchByMergeBatchId(String e);
    */
    @Query("select new com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId(p.batchId as batchId,p.controlId as controlId,SUM(p.wt) as WT,SUM(p.mtr) as MTR) from BatchData p where p.mergeBatchId=:e GROUP BY p.batchId,p.controlId")
    List<GetBatchWithControlId> getAllBatchByMergeBatchId(String e);

    @Query("select new com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId(p.mergeBatchId,SUM(p.wt) as WT,SUM(p.mtr) as MTR) from BatchData p where p.isProductionPlanned = true AND  p.batchId IS NOT NULL AND p.mergeBatchId IS NOT NULL AND  p.controlId IS NOT NULL GROUP BY p.mergeBatchId ")
    List<GetBatchWithControlId> getAllMergeBatchWithoutBillGenrated();

    @Query("select new com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId(p.mergeBatchId,SUM(p.wt) as WT,SUM(p.mtr) as MTR) from BatchData p where p.isProductionPlanned = true AND p.batchId IS NOT NULL AND p.mergeBatchId IS NOT NULL AND  p.controlId IN (select ss.id from StockMast ss where ss.createdBy=:userId OR ss.userHeadId=:userHeadId) GROUP BY p.mergeBatchId ")
    List<GetBatchWithControlId> getAllMergeBatchWithoutBillGenrated(Long userId, Long userHeadId);

    @Query("select new com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId(p.batchId as batchId,p.controlId as controlId,SUM(p.wt) as WT,SUM(p.mtr)) from BatchData p where p.batchId IS NOT NULL AND p.mergeBatchId=:mergeBatchId GROUP BY p.batchId,p.controlId")
    List<GetBatchWithControlId> getBatchesByMergeBatchId(String mergeBatchId);

    @Query("select x from BatchData x where x.batchId=:batchId AND x.mergeBatchId=:mergeBatchId ")
    List<BatchData> getBatchByBatchIdWithMergeBatchId(String batchId, String mergeBatchId);

    @Query("select x from BatchData x where x.batchId=:batchId AND x.mergeBatchId IS NULL AND x.isBillGenrated=false")
    List<BatchData> findByBatchIdWithoutBillGenerated(String batchId);

    @Query("select x from BatchData x where x.batchId=:batchId AND x.mergeBatchId =:mergeBatchId AND x.isBillGenrated=false AND x.batchId=:batchId")
    List<BatchData> findByMergeBatchIdWithoutBillGenerated(String mergeBatchId,String batchId);

    @Query("select new com.main.glory.model.dispatch.response.GetBatchByInvoice(SUM(b.id)as wt,b.batchId,b.controlId,b.mergeBatchId) from BatchData b where b.isProductionPlanned=true AND b.isBillGenrated=false AND b.controlId=:id AND b.mergeBatchId IS NOT NULL GROUP BY b.batchId,b.controlId,b.mergeBatchId")
    List<GetBatchByInvoice> getMergeBatcheByStockIdWithoutBillGenerated(Long id);

    @Query("select b from BatchData b where b.batchId=:batchId AND b.isFinishMtrSave=true AND b.isBillGenrated=false")
    List<BatchData> getBatchesByBatchIdAndFinishMtrSaveWithoutBillGenrated(String batchId);

    @Query("select x from BatchData x where x.batchId=:batchId AND x.mergeBatchId=:mergeBatchId AND x.isBillGenrated=false AND x.isProductionPlanned=true")
    List<BatchData> getBatchByMergeBatchIdAndBatchIdForFinishMtrSave(String batchId, String mergeBatchId);

    @Query("select new com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId(p.mergeBatchId,SUM(p.wt) as WT,SUM(p.mtr) as MTR) from BatchData p where p.isProductionPlanned = false AND p.isExtra=false AND p.batchId IS NOT NULL AND p.mergeBatchId IS NOT NULL AND p.controlId IS NOT NULL GROUP BY p.mergeBatchId")
    List<GetBatchWithControlId> findAllMergeBatch();

    @Query("select new com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId(p.mergeBatchId,SUM(p.wt) as WT,SUM(p.mtr) as MTR) from BatchData p where p.controlId = :id AND p.isProductionPlanned = false AND p.mergeBatchId IS NOT NULL GROUP BY p.mergeBatchId ")
    List<GetBatchWithControlId> getBatchAndStockListWithoutProductionPlanByStockIdAndBasedOnMergeBatchId(Long id);

    @Query("select new com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId(p.mergeBatchId,SUM(p.wt) as WT,SUM(p.mtr) as MTR) from BatchData p where p.batchId IS NOT NULL AND p.mergeBatchId IS NOT NULL AND p.controlId IS NOT NULL GROUP BY p.mergeBatchId")
    List<GetBatchWithControlId> findAllMergeBatchWithoutFilter();

    @Query("select new com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId(p.mergeBatchId,p.batchId,p.controlId,SUM(p.wt) as WT,SUM(p.mtr) as MTR) from BatchData p where p.batchId IS NOT NULL AND p.mergeBatchId =:mergeBatchId AND p.controlId IS NOT NULL GROUP BY p.batchId,p.controlId")
    List<GetBatchWithControlId> findAllMergeBatchWithoutFilterByMergeBatchId(String mergeBatchId);


/*
    @Query("select new com.main.glory.model.StockDataBatchData.response.GetAllBatch(SUM(b.wt)as WT,b.controlId as controlId,b.batchId,b.isProductionPlanned,b.isBillGenrated,(select p.id from Party p where p.id=(select s.partyId from StockMast s where s.id=b.controlId)) as partyId,(select p.partyName from Party p where p.id=(select s.partyId from StockMast s where s.id=b.controlId))as partyName,(select q.id from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId)) as qId,(select q.qualityId from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId))as qualityId,(select q.qualityName from QualityName q where q.id=(select qq.qualityNameId from Quality qq where qq.id=(select s.qualityId from StockMast s where s.id=b.controlId)))as qualityName,(select q.qualityType from Quality q where q.id=(select s.qualityId from StockMast s where s.id=b.controlId))as qualityType) from BatchData b where GROUP BY b.controlId, b.batchId")
    List<GetAllBatch> getAllBatchWithoutBillGeneratedAndFinishMtrSave();*/
}

