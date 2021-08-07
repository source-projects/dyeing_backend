package com.main.glory.Dao.StockAndBatch;

import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.response.GetAllStockWithPartyNameResponse;
import com.main.glory.model.dispatch.response.GetBatchByInvoice;
import com.main.glory.model.quality.Quality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface StockMastDao extends JpaRepository<StockMast, Long> {

 @Query("select new com.main.glory.model.StockDataBatchData.response.GetAllStockWithPartyNameResponse(sm, (Select p.partyName from Party p where p.id = sm.partyId),(select q.qualityName from QualityName q where q.id = (select qq.qualityNameId from Quality qq where qq.id = sm.qualityId))) from StockMast sm")
 Optional<List<GetAllStockWithPartyNameResponse>> getAllStockWithPartyNameAndQualityName();

 @Query("select new com.main.glory.model.StockDataBatchData.response.GetAllStockWithPartyNameResponse(sm, (Select p.partyName from Party p where p.id = sm.partyId),(select q.qualityName from QualityName q where q.id = (select qq.qualityNameId from Quality qq where qq.id = sm.qualityId))) from StockMast sm")
 Optional<List<GetAllStockWithPartyNameResponse>> getAllStockWithPartyName();


 @Query("select new com.main.glory.model.StockDataBatchData.response.GetAllStockWithPartyNameResponse(sm, (Select p.partyName from Party p where p.id = sm.partyId),(select q.qualityName from QualityName q where q.id = (select qq.qualityNameId from Quality qq where qq.id = sm.qualityId))) from StockMast sm where createdBy = :createdBy")
 Optional<List<GetAllStockWithPartyNameResponse>> getAllStockWithPartyNameByCreatedBy(Long createdBy);

 @Query("select new com.main.glory.model.StockDataBatchData.response.GetAllStockWithPartyNameResponse(sm, (Select p.partyName from Party p where p.id = sm.partyId),(select q.qualityName from QualityName q where q.id = (select qq.qualityNameId from Quality qq where qq.id = sm.qualityId))) from StockMast sm where sm.userHeadId = :userHeadId OR sm.createdBy=:userHeadId")
 Optional<List<GetAllStockWithPartyNameResponse>> getAllStockWithPartyNameByUserHeadId(Long userHeadId);

 List<StockMast> findByQualityId(Long qualityId);

 @Query("select s from StockMast s where s.partyId=:partyId AND s.qualityId=:qualityId")
 List<StockMast> findByQualityIdAndPartyId(Long qualityId,Long partyId);

 @Query("select s from StockMast s where s.partyId=:partyId AND s.qualityId=:qualityId AND (s.createdBy=:userId OR s.userHeadId=:userHeadId)")
 List<StockMast> findByQualityIdAndPartyId(Long qualityId,Long partyId,Long userId,Long userHeadId);


 @Query("select sm from StockMast sm where userHeadId =:userHeadId OR createdBy =:userHeadId")
 List<StockMast> findByUserHeadId(Long userHeadId);


 @Query("select sm from StockMast sm where sm.partyId =:partyId AND partyId IS NOT NULL")
 List<StockMast> findByPartyId(Long partyId);

 //Get all stock list
 @Query("select s from StockMast s")
 List<StockMast> getAllStock();

 @Query("select s from StockMast s where s.partyId=:partyId AND s.qualityId=:qualityId ")
 List<StockMast> findByPartyIdAndQualityId(Long partyId, Long qualityId);
 //filter api's

 @Query("select s from StockMast s where (:partyId IS NULL OR s.partyId=:partyId) AND (:qualityEntryId IS NULL OR s.qualityId=:qualityEntryId) AND (:billNo IS NULL OR s.billNo<=:billNo) AND (:toDate IS NULL OR s.createdDate<=:toDate) AND (:fromDate IS NULL OR s.createdDate>=:fromDate)")
 List<StockMast> findByQualityIdAndPartyIdAndDateFilter(Long partyId, Long qualityEntryId, String billNo, Date toDate, Date fromDate);

 @Query("select s from StockMast s where s.partyId=:partyId AND s.createdDate<=:toDate AND s.createdDate>=:fromDate")
 List<StockMast> findStockPartyId(Long partyId, Date toDate, Date fromDate);

@Query("select s from StockMast s where s.id=:stockId")
 StockMast findByStockId(Long stockId);

 @Query("select new com.main.glory.model.StockDataBatchData.response.GetAllStockWithPartyNameResponse(sm, (Select p.partyName from Party p where p.id = sm.partyId),(select q.qualityName from QualityName q where q.id = (select qq.qualityNameId from Quality qq where qq.id = sm.qualityId))) from StockMast sm where sm.userHeadId = :userHeadId OR sm.createdBy=:id")
 Optional<List<GetAllStockWithPartyNameResponse>> getAllStockWithPartyNameByUserHeadIdAndCreatedBy(Long id, Long userHeadId);

 @Query("select s from StockMast s where s.id=(select ss.controlId as id,SUM(ss.mtr) from BatchData ss where isProductionPlanned=false AND controlId IS NOT NULL AND batchId IS NOT NULL GROUP BY batchId,controlId )")
 List<StockMast> getAllStockWithoutBatchPlanned();


 @Query("select s from StockMast s where s.partyId=:id")
 List<StockMast> getAllStockByPartyId(Long id);

 @Query("select s from StockMast s where s.qualityId=:id")
 List<StockMast> getAllStockByQualityId(Long id);


 @Query("select sm from StockMast sm where sm.partyId=:partyId AND sm.qualityId=:qualityId")
 List<StockMast> getAllStockByPartyIdAndQualityId(Long partyId, Long qualityId);

 @Query("select x from StockMast x where x.userHeadId=:userHeadId")
 List<StockMast> getAllStockByUserHeadId(Long userHeadId);

    /*@Query("select q from Quality q where q.id=(select s.qualityId from StockBatch s where s.id=:stockId)")
    Quality getQualityByStockId(Long stockId);*/
}
