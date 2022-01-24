package com.main.glory.Dao.dispatch;


import com.main.glory.model.StockDataBatchData.response.BatchWithTotalMTRandFinishMTR;
import com.main.glory.model.dispatch.DispatchData;
import com.main.glory.model.dispatch.DispatchMast;
import com.main.glory.model.dispatch.response.MonthlyDispatchReport;
import com.main.glory.model.dispatch.response.report.ConsolidatedBillDataForExcel;
import com.main.glory.model.dispatch.response.GetBatchByInvoice;
import com.main.glory.model.dispatch.response.QualityWithRateAndTotalMtr;
import com.main.glory.model.dispatch.response.UnitDetail;
import com.main.glory.model.dispatch.response.report.ConsolidatedBillDataForPDF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
    @Query("select dd from DispatchData dd where dd.stockId =:stockId AND dd.batchId=:batchId AND dd.invoiceNo=:invoiceNo AND dd.batchId IS NOT NULL AND dd.stockId IS NOT NULL  ")
    List<DispatchData> findByBatchIdAndStockIdAndInviceNo(Long stockId, String batchId, String invoiceNo);

    @Query("select d from DispatchData d")
    List<DispatchData> getAllDispatch();

    @Query(value = "select d.is_send_to_party from dispatch_data as d where d.invoice_no=:invoiceNo LIMIT 1",nativeQuery = true)
    Boolean getSendToPartyFlag(@Param("invoiceNo") String invoiceNo);

    @Modifying
    @Transactional
    @Query("delete from DispatchData dd where dd.invoiceNo=:invoiceNo")
    void deleteByInvoiceNo(String invoiceNo);

    @Query("select d from DispatchData d where d.invoiceNo=:invoiceNo")
    List<DispatchData> getBatchByInvoiceNo(String invoiceNo);

    @Query("select new com.main.glory.model.dispatch.response.GetBatchByInvoice(COUNT(d.batchData.id) as batchEntryId,d.batchId as batchId,d.stockId as stockId) from DispatchData d where d.invoiceNo=:invoiceNumber GROUP BY d.batchId,d.stockId")
    List<GetBatchByInvoice> getAllStockByInvoiceNumber(String invoiceNumber);

    @Query("select new com.main.glory.model.dispatch.response.GetBatchByInvoice(COUNT(d.batchData.id) as batchEntryId,d.stockId as stockId,d.pchallanRef,d.batchId as batchId) from DispatchData d where d.invoiceNo=:invoiceNumber GROUP BY d.batchId,d.stockId,d.pchallanRef")
    List<GetBatchByInvoice> getAllStockByInvoiceNumberWithPchallen(String invoiceNumber);

    @Query("select new com.main.glory.model.dispatch.response.QualityWithRateAndTotalMtr(x.quality.id,(select q.qualityName from QualityName q where q.id = (select qq.qualityName.id from Quality qq where qq.id=x.quality.id)),(select q.qualityId from Quality q where q.id=x.quality.id),x.qualityRate) from DispatchData x where x.invoiceNo=:invoiceNo GROUP BY x.quality.id")
    List<QualityWithRateAndTotalMtr> getAllQualityByInvoiceNo(String invoiceNo);

    @Query("select (x.batchData.id) from DispatchData x where x.invoiceNo=:invoiceNo AND x.quality.id=:qualityEntryId")
    List<Long> getAllBatchEntryIdByQualityAndInvoice(Long qualityEntryId, String invoiceNo);

    @Query("select x.batchData.id from DispatchData x where x.invoiceNo=:invoiceNo")
    List<Long> getBatchEntryIdsByInvoiceNo(String invoiceNo);

    /*@Modifying
    @Transactional
    @Query("delete from DispatchData x where x.invoiceNo=:invoiceNo")
    void deleteBatchEntryIdByInvoiceNo(String invoiceNo);
*/
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


    @Query("select new com.main.glory.model.dispatch.response.GetBatchByInvoice(COUNT(dd.batchData.id) as batch,dd.stockId,dd.pchallanRef,dd.batchId) from DispatchData dd where dd.invoiceNo=:invoiceExist GROUP BY dd.batchId,dd.pchallanRef,dd.stockId")
    List<GetBatchByInvoice> findPChallanAndStockByInvoice(String invoiceExist);

    //get the invoice batch by the invoice no and batch id and stock id
    @Query("select dd from DispatchData dd where dd.stockId =:stockId AND dd.pchallanRef=:pchallanRef AND dd.invoiceNo=:invoiceNo AND dd.pchallanRef IS NOT NULL AND dd.stockId IS NOT NULL   ")
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
    @Query("select dd from DispatchData dd where dd.stockId =:stockId AND dd.pchallanRef=:pchallanRef AND dd.invoiceNo=:invoiceNo AND dd.batchId=:batchId AND dd.pchallanRef IS NOT NULL AND dd.stockId IS NOT NULL")
    List<DispatchData> findByPChallanRefAndBatchIdAndStockIdAndInvoiceNo(Long stockId, String pchallanRef, String batchId, String invoiceNo);

    @Query(value = "select quality_rate from dispatch_data as x where x.batch_id = :batchId AND x.pchallan_ref=:pchallanRef AND x.invoice_no=:invoiceNo LIMIT 1",nativeQuery = true)
    Double getQualityRateByInvoiceNoAndBatchIdAndPchallanRef(String invoiceNo, String pchallanRef, String batchId);


    @Query(value = "select quality_rate from dispatch_data as x where x.batch_id = :batchId AND x.pchallan_ref=:pchallanRef AND x.invoice_no=:invoiceNo LIMIT 1",nativeQuery = true)
    Double getShadeRateByInvoiceNoandBatchIdAndPchallanRef(String invoiceNo, String pchallanRef, String batchId);

    @Modifying
    @Transactional
    @Query("update DispatchData x set x.qualityRate=:rate where x.pchallanRef=:pchallanRef AND x.invoiceNo=:invoiceNo AND x.batchId=:batchId")
    void updateQualityRateWithPChallanRefAndBatchIdAndInvoiceNo(String invoiceNo, String pchallanRef, Double rate, String batchId);

    @Query("select dd from DispatchData dd where dd.stockId =:stockId AND dd.pchallanRef=:pchallanRef AND dd.invoiceNo=:invoiceNo AND dd.batchId=:batchId AND dd.pchallanRef IS NOT NULL AND dd.stockId IS NOT NULL")
    List<DispatchData> findByPChallanRefAndStockIdAndBatchIdInvoiceNo(Long stockId, String pchallanRef, String invoiceNo, String batchId);
    //get All Distapatch list
    //@Query("select new com.main.glory.model.dispatch.response.BatchListWithInvoice(COUNT(dd.batchData.id) as batchEntryId,(dd.batchId) as batchId,(dd.stockId) as stockId,(dd.invoiceNo) as invoiceNo) from DispatchData dd where (:toDate IS NULL OR dd.createdDate <= :toDate AND :fromDate IS NULL OR dd.createdDate >= :fromDate) GROUP BY dd.batchId,dd.stockId,dd.invoiceNo")
    //List<BatchListWithInvoice> getAllDispatchList(Date toDate, Date fromDate);

    @Query("select new com.main.glory.model.StockDataBatchData.response.BatchWithTotalMTRandFinishMTR(b.batchId,b.stockId,SUM(b.batchData.wt),SUM(b.batchData.mtr),SUM(b.batchData.finishMtr),COUNT(b.id)) from DispatchData b where b.stockId=:stockId AND b.batchId=:batchId AND b.pchallanRef=:pchallanRef AND b.invoiceNo=:invoiceNo")
    BatchWithTotalMTRandFinishMTR getAllBatchWithTotalMtrAndTotalFinishMtr(String batchId, Long stockId,String pchallanRef,String invoiceNo);

    @Query("select new com.main.glory.model.dispatch.response.GetBatchByInvoice(COUNT(d.batchData.id) as batchEntryId,d.batchId as batchId,d.stockId as stockId,(select x from StockMast x where x.id=d.stockId)) from DispatchData d where d.invoiceNo=:invoiceNumber GROUP BY d.batchId,d.stockId")
    List<GetBatchByInvoice> getAllStockAndBatchByInvoiceNumber(String invoiceNumber);

    //    //query calculation with billing unit with inner join of batch Data
    @Query("select new com.main.glory.model.dispatch.response.report.ConsolidatedBillDataForExcel(dd.batchId,count(dd.batchData.id),dm.createdDate,dm.postfix,dd.quality.qualityId,dm.discount,dm.percentageDiscount,dm.netAmt,SUM(dd.batchData.finishMtr),CASE dd.billingUnit WHEN 'weight' THEN ((SUM(dd.batchData.mtr) / 100) * dd.wtPer100m) ELSE SUM(dd.batchData.mtr) END,dd.qualityRate,dm.party.city,dm.party.state,dm.party.GSTIN,dm.party.partyName,dm.party.partyAddress1,dm.party.partyAddress2,dm.party.contactNo,dd.billingUnit,dd.inwardUnit,dm.deliveryMode,dd.wtPer100m,dd.quality.qualityName.qualityName,dm.party.userHeadData.userName,(dd.qualityRate * SUM(dd.batchData.finishMtr)) as AMT,(dd.qualityRate * SUM(dd.batchData.finishMtr) * dm.percentageDiscount / 100) AS discountAmt,(dd.qualityRate * SUM(dd.batchData.finishMtr) - (dd.qualityRate * SUM(dd.batchData.finishMtr) *dm.percentageDiscount)/100) AS taxAmt) from DispatchData dd INNER JOIN DispatchMast dm ON dd.controlId = dm.id where (:signByParty IS NULL OR dm.signByParty=:signByParty) AND (:qualityEntryId IS NULL OR dd.quality.id=:qualityEntryId) AND (:qualityNameId IS NULL OR dd.quality.qualityName.id=:qualityNameId) AND (Date(dm.createdDate)>=Date(:from) OR :from IS NULL) AND (Date(dm.createdDate)<=Date(:to) OR :to IS NULL) AND (:userHeadId IS NULL OR dm.userHeadData.id=:userHeadId) AND (:partyId IS NULL OR dm.party.id=:partyId) GROUP BY dd.batchId,dd.invoiceNo,dd.quality,dd.qualityRate,dd.wtPer100m,dd.inwardUnit,dd.billingUnit")
    List<ConsolidatedBillDataForExcel> getAllConsolidateResponseByFilter(Date from, Date to, Long userHeadId, Long partyId, Long qualityNameId, Long qualityEntryId,Boolean signByParty);


    @Query("select new com.main.glory.model.dispatch.response.report.ConsolidatedBillDataForPDF(dd.batchId,count(dd.batchData.id),dm.createdDate,dm.postfix,dd.quality.qualityId,SUM(dd.batchData.finishMtr),CASE dd.billingUnit WHEN 'weight' THEN ((SUM(dd.batchData.mtr) / 100) * dd.wtPer100m) ELSE SUM(dd.batchData.mtr) END,dd.qualityRate,dd.quality.qualityName.qualityName,dm.party.partyName,dm.party.userHeadData.userName,(dd.qualityRate * SUM(dd.batchData.finishMtr) - (dd.qualityRate * SUM(dd.batchData.finishMtr) *dm.percentageDiscount)/100) AS taxAmt) from DispatchData dd INNER JOIN DispatchMast dm ON dd.controlId = dm.id where (:signByParty IS NULL OR dm.signByParty=:signByParty) AND (:qualityEntryId IS NULL OR dd.quality.id=:qualityEntryId) AND (:qualityNameId IS NULL OR dd.quality.qualityName.id=:qualityNameId) AND (Date(dm.createdDate)>=Date(:from) OR :from IS NULL) AND (Date(dm.createdDate)<=Date(:to) OR :to IS NULL) AND (:userHeadId IS NULL OR dm.userHeadData.id=:userHeadId) AND (:partyId IS NULL OR dm.party.id=:partyId) GROUP BY dd.batchId,dd.invoiceNo,dd.quality,dd.qualityRate,dd.billingUnit")
    List<ConsolidatedBillDataForPDF> getAllConsolidateResponseForPDFReportByFilter(Date from, Date to, Long userHeadId, Long partyId, Long qualityNameId, Long qualityEntryId,Boolean signByParty);

    //@Query("select new com.main.glory.model.dispatch.response.MonthlyDispatchReport(SUM(dd.batchData.finishMtr),SUM(dm.taxAmt),SUM(dm.discount),function('date_format', dm.createdDate, '%Y, %m') as DateMonth,SUM(dm.netAmt),dd.billingUnit) from DispatchData dd INNER JOIN DispatchMast dm ON dd.controlId = dm.id where (Date(dm.createdDate)>=Date(:from) OR :from IS NULL) AND (Date(dm.createdDate)<=Date(:to) OR :to IS NULL) AND (:userHeadId IS NULL OR dm.userHeadData.id=:userHeadId) GROUP BY function('date_format', dd.createdDate, '%Y, %m'),dd.billingUnit")
    //@Query("select new com.main.glory.model.dispatch.response.MonthlyDispatchReport(MONTH(dm.createdDate),SUM(dd.batchData.finishMtr),SUM(dm.taxAmt),SUM(dm.discount),YEAR(dm.createdDate),SUM(dm.netAmt),dd.billingUnit) from DispatchData dd INNER JOIN DispatchMast dm ON dd.controlId = dm.id where (Date(dm.createdDate)>=Date(:from) OR :from IS NULL) AND (Date(dm.createdDate)<=Date(:to) OR :to IS NULL) AND (:userHeadId IS NULL OR dm.userHeadData.id=:userHeadId) GROUP BY MONTH(dm.createdDate),YEAR(dm.createdDate),dd.billingUnit")
    @Query("select new com.main.glory.model.dispatch.response.MonthlyDispatchReport(SUM(dd.batchData.finishMtr),concat(Month(dm.createdDate),', ',Year(dm.createdDate)),dm) from DispatchData dd INNER JOIN DispatchMast dm ON dd.controlId = dm.id where (Date(dm.createdDate)>=Date(:from) OR :from IS NULL) AND (Date(dm.createdDate)<=Date(:to) OR :to IS NULL) AND (:userHeadId IS NULL OR dm.userHeadData.id=:userHeadId) GROUP by dm.id")
    List<MonthlyDispatchReport> monthWisePDFReportByFilter(Date from, Date to, Long userHeadId);


    //for get group by record
    //SET GLOBAL sql_mode=(SELECT REPLACE(@@sql_mode,'ONLY_FULL_GROUP_BY',''));

}
