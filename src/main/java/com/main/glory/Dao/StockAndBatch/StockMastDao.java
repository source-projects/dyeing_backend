package com.main.glory.Dao.StockAndBatch;

import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.response.GetAllBatchResponse;
import com.main.glory.model.StockDataBatchData.response.GetAllStockWithPartyNameResponse;
import com.main.glory.model.quality.Quality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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


 //filter api's

 @Query("select s from StockMast s where s.partyId=:partyId AND s.qualityId=:qualityEntryId AND s.createdDate<=:toDate OR s.updatedDate<=:toDate AND s.createdDate>=:fromDate OR s.updatedDate>=:fromDate")
 List<StockMast> findByQualityIdAndPartyIdAndDateFilter(Long partyId, Long qualityEntryId, Date toDate, Date fromDate);
}
