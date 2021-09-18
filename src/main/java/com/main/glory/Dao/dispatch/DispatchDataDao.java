package com.main.glory.Dao.dispatch;

import com.main.glory.model.dispatch.DispatchData;
import com.main.glory.model.dispatch.response.GetBatchByInvoice;
import com.main.glory.model.dispatch.response.QualityWithRateAndTotalMtr;
import com.main.glory.model.dispatch.response.UnitDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DispatchDataDao extends JpaRepository<DispatchData, Long> {


    @Query("select new com.main.glory.model.dispatch.response.GetBatchByInvoice(SUM(dd.batchData.id) as batch,dd.batchId,dd.stockId) from DispatchData dd where dd.invoiceNo=:invoiceNo GROUP BY dd.batchId,dd.stockId")
    List<GetBatchByInvoice> findBatchAndStockByInvoice(String invoiceNo);

    @Modifying
    @Transactional
    @Query("delete from DispatchData dd where dd.stockId=:key AND dd.batchId=:value AND dd.stockId IS NOT NULL AND dd.batchId IS NOT NULL")
    void deleteByStockIdAndBatchId(Long key, String value);

    @Modifying
    @Transactional
    @Query("delete from DispatchData dd where dd.batchData.id=:id")
    void deleteByBatchEntryId(Long id);

    @Query(value = "select dd.invoice_no from dispatch_data as dd where dd.invoice_no=:invoiceNo LIMIT 1",nativeQuery = true)
    String findByInvoiceNo(@Param("invoiceNo") String invoiceNo);


    @Modifying
    @Transactional
    @Query("update DispatchData dd set dd.isSendToParty=true where dd.invoiceNo=:invoiceNumberExist")
    void updateStatus(String invoiceNumberExist);


    //for bill with or without bill send to party
    @Query("select new com.main.glory.model.dispatch.response.GetBatchByInvoice(SUM(dd.batchData.id) as batch,dd.batchId,dd.stockId) from DispatchData dd where dd.invoiceNo=:invoiceExist GROUP BY dd.batchId,dd.stockId")
    List<GetBatchByInvoice> findBatchAndStockByInvoiceWithoutStatus(String invoiceExist);


    //get the invoice batch by the invoice no and batch id and stock id
    @Query("select dd from DispatchData dd where dd.stockId =:stockId AND dd.batchId=:batchId AND dd.invoiceNo=:invoiceNo AND dd.batchId IS NOT NULL AND dd.stockId IS NOT NULL AND dd.invoiceNo IS NOT NULL   ")
    List<DispatchData> findByBatchIdAndStockIdAndInviceNo(Long stockId, String batchId, String invoiceNo);

    @Query("select d from DispatchData d")
    List<DispatchData> getAllDispatch();

    @Query(value = "select d.is_send_to_party from dispatch_data as d where d.invoice_no=:invoiceNo LIMIT 1",nativeQuery = true)
    Boolean getSendToPartyFlag(@Param("invoiceNo") String invoiceNo);

    @Modifying
    @Transactional
    @Query("delete from DispatchData dd where dd.invoiceNo=:invoiceNo AND dd.invoiceNo IS NOT NULL")
    void deleteByInvoiceNo(String invoiceNo);

    @Query("select d from DispatchData d where d.invoiceNo=:invoiceNo")
    List<DispatchData> getBatchByInvoiceNo(String invoiceNo);

    @Query("select new com.main.glory.model.dispatch.response.GetBatchByInvoice(COUNT(d.batchData.id) as batchEntryId,d.batchId as batchId,d.stockId as stockId) from DispatchData d where d.invoiceNo=:invoiceNumber GROUP BY d.batchId,d.stockId")
    List<GetBatchByInvoice> getAllStockByInvoiceNumber(String invoiceNumber);

    @Query("select new com.main.glory.model.dispatch.response.QualityWithRateAndTotalMtr(x.quality.id,(select q.qualityName from QualityName q where q.id = (select qq.qualityNameId from Quality qq where qq.id=x.quality.id)),(select q.qualityId from Quality q where q.id=x.quality.id),x.qualityRate) from DispatchData x where x.invoiceNo=:invoiceNo GROUP BY x.quality.id")
    List<QualityWithRateAndTotalMtr> getAllQualityByInvoiceNo(String invoiceNo);

    @Query("select (x.batchData.id) from DispatchData x where x.invoiceNo=:invoiceNo AND x.quality.id=:qualityEntryId")
    List<Long> getAllBatchEntryIdByQualityAndInvoice(Long qualityEntryId, String invoiceNo);

    @Query("select x.batchData.id from DispatchData x where x.invoiceNo=:invoiceNo")
    List<Long> getBatchEntryIdsByInvoiceNo(String invoiceNo);

    @Modifying
    @Transactional
    @Query("delete from DispatchData x where x.invoiceNo=:invoiceNo")
    void deleteBatchEntryIdByInvoiceNo(String invoiceNo);

    @Query("select x.qualityRate from DispatchData x where x.batchData.id=:id AND x.invoiceNo=:invoiceNumber")
    Double getQualityRateByInvoiceAndBatchEntryId(String invoiceNumber, Long id);


    @Modifying
    @Transactional
    @Query("update DispatchData x set x.qualityRate=:rate where x.batchId=:batchId AND x.invoiceNo=:invoiceNo")
    void updateQualityRateWithBatchIdAndInvoiceNo(String invoiceNo, String batchId, Double rate);

    @Query("select x.billingUnit from DispatchData x where x.batchData.id=:id AND x.invoiceNo=:invoiceNumber")
    String getBillingUnitByInvoiceAndBatchEntryId(String invoiceNumber, Long id);

    @Query("select x.wtPer100m from DispatchData x where x.batchData.id=:id AND x.invoiceNo=:invoiceNumber")
    Double getWtPer100mByInvoiceAndBatchEntryId(String invoiceNumber, Long id);

    @Modifying
    @Transactional
    @Query("update DispatchData x set x.qualityRate=:rate where x.pchallanRef=:pchallanRef AND x.invoiceNo=:invoiceNo")
    void updateQualityRateWithPChallanRefAndInvoiceNo(String invoiceNo, String pchallanRef, Double rate);


    @Query("select new com.main.glory.model.dispatch.response.GetBatchByInvoice(SUM(dd.batchData.id) as batch,dd.stockId,dd.pchallanRef) from DispatchData dd where dd.invoiceNo=:invoiceExist GROUP BY dd.pchallanRef,dd.stockId")
    List<GetBatchByInvoice> findPChallanAndStockByInvoice(String invoiceExist);

    //get the invoice batch by the invoice no and batch id and stock id
    @Query("select dd from DispatchData dd where dd.stockId =:stockId AND dd.pchallanRef=:pchallanRef AND dd.invoiceNo=:invoiceNo AND dd.pchallanRef IS NOT NULL AND dd.stockId IS NOT NULL AND dd.invoiceNo IS NOT NULL   ")
    List<DispatchData> findByPChallanRefAndStockIdAndInvoiceNo(Long stockId, String pchallanRef, String invoiceNo);

    @Query("select x.inwardUnit from DispatchData x where x.batchData.id=:id AND x.invoiceNo=:invoiceNumber")
    String getInwardUnitByInvoiceAndBatchEntryId(String invoiceNumber, Long id);

    @Query("select new com.main.glory.model.dispatch.response.UnitDetail(d.billingUnit,d.inwardUnit,d.wtPer100m) from DispatchData d where d.invoiceNo=:invoiceNo AND d.batchData.id=:batchEntryId")
    UnitDetail getUnitDetailByInvoiceNoAndBatchEntryId(String invoiceNo, Long batchEntryId);

    @Query(value = "select * from dispatch_data where invoice_no=:invoiceNo LIMIT 1",nativeQuery = true)
    DispatchData getDispatchDataByInvoiceNumber(@Param("invoiceNo") String invoiceNo);

    @Query("select new com.main.glory.model.dispatch.response.GetBatchByInvoice(SUM(dd.batchData.id) as batch,dd.stockId,dd.pchallanRef,dd.batchId) from DispatchData dd where dd.invoiceNo=:invoiceExist GROUP BY dd.pchallanRef,dd.stockId,dd.batchId")
    List<GetBatchByInvoice> findPChallanAndBatchIdAndStockByInvoice(String invoiceExist);

    //get the invoice batch by the invoice no and batch id and stock id
    @Query("select dd from DispatchData dd where dd.stockId =:stockId AND dd.pchallanRef=:pchallanRef AND dd.invoiceNo=:invoiceNo AND dd.batchId=:batchId AND dd.pchallanRef IS NOT NULL AND dd.stockId IS NOT NULL AND dd.invoiceNo IS NOT NULL")
    List<DispatchData> findByPChallanRefAndBatchIdAndStockIdAndInvoiceNo(Long stockId, String pchallanRef, String batchId, String invoiceNo);

    @Query(value = "select quality_rate from dispatch_data as x where x.batch_id = :batchId AND x.pchallan_ref=:pchallanRef AND x.invoice_no=:invoiceNo LIMIT 1",nativeQuery = true)
    Double getQualityRateByInvoiceNoAndBatchIdAndPchallanRef(String invoiceNo, String pchallanRef, String batchId);


    //get All Distapatch list
    //@Query("select new com.main.glory.model.dispatch.response.BatchListWithInvoice(COUNT(dd.batchData.id) as batchEntryId,(dd.batchId) as batchId,(dd.stockId) as stockId,(dd.invoiceNo) as invoiceNo) from DispatchData dd where (:toDate IS NULL OR dd.createdDate <= :toDate AND :fromDate IS NULL OR dd.createdDate >= :fromDate) GROUP BY dd.batchId,dd.stockId,dd.invoiceNo")
    //List<BatchListWithInvoice> getAllDispatchList(Date toDate, Date fromDate);
}
