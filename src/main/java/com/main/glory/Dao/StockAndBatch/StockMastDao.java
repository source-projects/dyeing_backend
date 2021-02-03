package com.main.glory.Dao.StockAndBatch;

import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.response.GetAllStockWithPartyNameResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface StockMastDao extends JpaRepository<StockMast, Long> {

 @Query("select new com.main.glory.model.StockDataBatchData.response.GetAllStockWithPartyNameResponse(sm, (Select p.partyName from Party p where p.id = sm.partyId)) from StockMast sm")
 Optional<List<GetAllStockWithPartyNameResponse>> getAllStockWithPartyName();

 @Query("select new com.main.glory.model.StockDataBatchData.response.GetAllStockWithPartyNameResponse(sm, (Select p.partyName from Party p where p.id = sm.partyId)) from StockMast sm where createdBy = :createdBy")
 Optional<List<GetAllStockWithPartyNameResponse>> getAllStockWithPartyNameByCreatedBy(Long createdBy);

 @Query("select new com.main.glory.model.StockDataBatchData.response.GetAllStockWithPartyNameResponse(sm, (Select p.partyName from Party p where p.id = sm.partyId)) from StockMast sm where userHeadId = :userHeadId")
 Optional<List<GetAllStockWithPartyNameResponse>> getAllStockWithPartyNameByUserHeadId(Long userHeadId);

 List<StockMast> findByQualityId(Long qualityId);

 List<StockMast> findByQualityIdAndPartyId(Long qualityId,Long partyId);

 @Query("select sm from StockMast sm where userHeadId =:userHeadId OR createdBy =:userHeadId")
 List<StockMast> findByUserHeadId(Long userHeadId);


 @Query("select sm from StockMast sm where sm.partyId =:partyId AND partyId IS NOT NULL")
 Optional<List<StockMast>> findByPartyId(Long partyId);

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
}
