package com.main.glory.Dao.StockAndBatch;

import com.main.glory.Dao.FilterDao;
import com.main.glory.filters.StockDataBatchData.StockMastFilter;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.response.GetAllStockWithPartyNameResponse;
import com.main.glory.model.StockDataBatchData.response.PendingBatchDataForExcel;
import com.main.glory.model.dispatch.response.GetBatchByInvoice;
import com.main.glory.model.quality.Quality;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface StockMastDao extends FilterDao<StockMast>  {

 @Query("select new com.main.glory.model.StockDataBatchData.response.GetAllStockWithPartyNameResponse(sm, sm.party.partyName,sm.quality.qualityName.qualityName) from StockMast sm")
 Optional<List<GetAllStockWithPartyNameResponse>> getAllStockWithPartyNameAndQualityName();

 @Query("select new com.main.glory.model.StockDataBatchData.response.GetAllStockWithPartyNameResponse(sm, sm.party.partyName,sm.quality.qualityName.qualityName) from StockMast sm")
 Optional<Page<GetAllStockWithPartyNameResponse>> getAllStockWithPartyNameAndQualityNamePaginated(Pageable pageable);

 @Query("select new com.main.glory.model.StockDataBatchData.response.GetAllStockWithPartyNameResponse(sm, sm.party.partyName,sm.quality.qualityName.qualityName) from StockMast sm")
 Optional<List<GetAllStockWithPartyNameResponse>> getAllStockWithPartyName();


 @Query("select new com.main.glory.model.StockDataBatchData.response.GetAllStockWithPartyNameResponse(sm, sm.party.partyName,sm.quality.qualityName.qualityName) from StockMast sm where createdBy = :createdBy")
 Optional<List<GetAllStockWithPartyNameResponse>> getAllStockWithPartyNameByCreatedBy(Long createdBy);

 @Query("select new com.main.glory.model.StockDataBatchData.response.GetAllStockWithPartyNameResponse(sm, sm.party.partyName,sm.quality.qualityName.qualityName) from StockMast sm where createdBy = :createdBy")
 Optional<Page<GetAllStockWithPartyNameResponse>> getAllStockWithPartyNameByCreatedByPaginated(Pageable pageable,Long createdBy);


 @Query("select new com.main.glory.model.StockDataBatchData.response.GetAllStockWithPartyNameResponse(sm, sm.party.partyName,sm.quality.qualityName.qualityName) from StockMast sm where sm.userHeadId = :userHeadId OR sm.createdBy=:userHeadId")
 Optional<List<GetAllStockWithPartyNameResponse>> getAllStockWithPartyNameByUserHeadId(Long userHeadId);

 List<StockMast> findByQualityId(Long qualityId);

 @Query("select s from StockMast s where s.party.id=:partyId AND s.quality.id=:qualityId")
 List<StockMast> findByQualityIdAndPartyId(Long qualityId,Long partyId);

 @Query("select s from StockMast s where s.party.id=:partyId AND s.quality.id=:qualityId AND (s.createdBy=:userId OR s.userHeadId=:userHeadId)")
 List<StockMast> findByQualityIdAndPartyId(Long qualityId,Long partyId,Long userId,Long userHeadId);


 @Query("select sm from StockMast sm where userHeadId =:userHeadId OR createdBy =:userHeadId")
 List<StockMast> findByUserHeadId(Long userHeadId);


 @Query("select sm from StockMast sm where sm.party.id =:partyId AND :partyId IS NOT NULL")
 List<StockMast> findByPartyId(Long partyId);

 //Get all stock list
 @Query("select s from StockMast s")
 List<StockMast> getAllStock();

 @Query("select s from StockMast s where s.party.id=:partyId AND s.quality.id=:qualityId ")
 List<StockMast> findByPartyIdAndQualityId(Long partyId, Long qualityId);
 //filter api's

 @Query("select s from StockMast s where (:partyId IS NULL OR s.party.id=:partyId) AND (:qualityEntryId IS NULL OR s.quality.id=:qualityEntryId) AND (:billNo IS NULL OR s.billNo<=:billNo) AND (:toDate IS NULL OR s.createdDate<=:toDate) AND (:fromDate IS NULL OR s.createdDate>=:fromDate)")
 List<StockMast> findByQualityIdAndPartyIdAndDateFilter(Long partyId, Long qualityEntryId, String billNo, Date toDate, Date fromDate);

 @Query("select s from StockMast s where s.party.id=:partyId AND s.createdDate<=:toDate AND s.createdDate>=:fromDate")
 List<StockMast> findStockPartyId(Long partyId, Date toDate, Date fromDate);

@Query("select s from StockMast s where s.id=:stockId")
 StockMast findByStockId(Long stockId);

 @Query("select new com.main.glory.model.StockDataBatchData.response.GetAllStockWithPartyNameResponse(sm, sm.party.partyName,sm.quality.qualityName.qualityName) from StockMast sm where sm.userHeadId = :userHeadId OR sm.createdBy=:id")
 Optional<List<GetAllStockWithPartyNameResponse>> getAllStockWithPartyNameByUserHeadIdAndCreatedBy(Long id, Long userHeadId);

 @Query("select new com.main.glory.model.StockDataBatchData.response.GetAllStockWithPartyNameResponse(sm, sm.party.partyName,sm.quality.qualityName.qualityName) from StockMast sm where sm.userHeadId = :userHeadId OR sm.createdBy=:id")
 Optional<Page<GetAllStockWithPartyNameResponse>> getAllStockWithPartyNameByUserHeadIdAndCreatedByPaginated(Pageable pageable,Long id, Long userHeadId);


 @Query("select s from StockMast s where s.id=(select ss.controlId as id,SUM(ss.mtr) from BatchData ss where isProductionPlanned=false AND controlId IS NOT NULL AND batchId IS NOT NULL GROUP BY batchId,controlId )")
 List<StockMast> getAllStockWithoutBatchPlanned();


 @Query("select s from StockMast s where s.party.id=:id")
 List<StockMast> getAllStockByPartyId(Long id);

 @Query("select s from StockMast s where s.quality.id=:id")
 List<StockMast> getAllStockByQualityId(Long id);


 @Query("select sm from StockMast sm where sm.party.id=:partyId AND sm.quality.id=:qualityId")
 List<StockMast> getAllStockByPartyIdAndQualityId(Long partyId, Long qualityId);

 @Query("select x from StockMast x where x.userHeadId=:userHeadId")
 List<StockMast> getAllStockByUserHeadId(Long userHeadId);

    /*@Query("select q from Quality q where q.id=(select s.quality.id from StockBatch s where s.id=:stockId)")
    Quality getQualityByStockId(Long stockId);*/

    @Query("select x from StockMast x where x.receiveDate>=:from AND x.receiveDate<=:to AND x.isProductionPlanned=true")
    List<StockMast> getAllStockByRecievedDate(Date from,Date to);

    @Query("select x from StockMast x where (:id=x.id OR :id IS NULL) AND (:stockInType=x.stockInType OR :stockInType IS NULL) AND (:partyId=x.party.id OR :partyId IS NULL) AND (:billNo=x.billNo OR :billNo IS NULL) AND (:billDate=x.billDate OR :billDate IS NULL) AND (:chlDate=x.chlDate OR :chlDate IS NULL) AND (:unit=x.unit OR :unit IS NULL) AND  (:updatedBy=x.updatedBy OR :updatedBy IS NULL) AND (:createdBy=x.createdBy OR :createdBy IS NULL) AND (:userHeadId=x.userHeadId OR :userHeadId IS NULL) AND (:isProductionPlanned=x.isProductionPlanned OR :isProductionPlanned IS NULL) AND (:createdDate=x.createdDate OR :createdDate IS NULL) AND (Date(:receiveDate)=Date(x.receiveDate) OR :receiveDate IS NULL) AND (:updatedDate=x.updatedDate OR :updatedDate IS NULL) AND (:qualityId=x.quality.id OR :qualityId IS NULL)")
    Page<StockMast> getAllFilteredStockMastPaged(Long id,
    String stockInType,
    Long partyId,
    String billNo,
    Date billDate,
    Date chlDate,
    String unit,
    Long updatedBy,
    Long createdBy,
    Long userHeadId,
    Boolean isProductionPlanned,
    Date createdDate,
    Date receiveDate,
    Date updatedDate,
    Long qualityId,Pageable pageable);




    @Query("select x from StockMast x where (:from IS NULL OR DATE(x.receiveDate)>=DATE(:from)) AND (:to IS NULL OR DATE(x.receiveDate)<=DATE(:to)) AND (:partyId IS NULL OR x.party.id=:partyId) AND (:qualityNameId IS NULL OR x.quality.qualityName.id=:qualityNameId) AND (:qualityEntryId IS NULL OR x.quality.id=:qualityEntryId) AND (:userHeadId IS NULL OR x.party.userHeadData.id=:userHeadId) AND x.id in (select b.controlId from BatchData b where b.isBillGenrated=false)")
    List<StockMast> filterByBatchFilterRequestWithPendingBatch(Date from, Date to, Long partyId, Long qualityNameId, Long qualityEntryId,Long userHeadId);




}
