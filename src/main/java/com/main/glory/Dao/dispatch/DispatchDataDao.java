package com.main.glory.Dao.dispatch;

import com.main.glory.model.dispatch.DispatchData;
import com.main.glory.model.dispatch.request.BatchAndStockId;
import com.main.glory.model.dispatch.response.BatchListWithInvoice;
import com.main.glory.model.dispatch.response.GetAllDispatch;
import com.main.glory.model.dispatch.response.GetBatchByInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface DispatchDataDao extends JpaRepository<DispatchData, Long> {


    @Query("select new com.main.glory.model.dispatch.response.GetBatchByInvoice(SUM(dd.batchEntryId) as batch,dd.batchId,dd.stockId) from DispatchData dd where dd.invoiceNo=:invoiceNo GROUP BY dd.batchId,dd.stockId")
    List<GetBatchByInvoice> findBatchAndStockByInvoice(String invoiceNo);

    @Modifying
    @Transactional
    @Query("delete from DispatchData dd where dd.stockId=:key AND dd.batchId=:value AND dd.stockId IS NOT NULL AND dd.batchId IS NOT NULL")
    void deleteByStockIdAndBatchId(Long key, String value);

    @Modifying
    @Transactional
    @Query("delete from DispatchData dd where dd.batchEntryId=:id")
    void deleteByBatchEntryId(Long id);

    @Query(value = "select dd.invoice_no from dispatch_data as dd where dd.invoice_no=:invoiceNo LIMIT 1",nativeQuery = true)
    String findByInvoiceNo(@Param("invoiceNo") String invoiceNo);


    @Modifying
    @Transactional
    @Query("update DispatchData dd set dd.isSendToParty=true where dd.invoiceNo=:invoiceNumberExist")
    void updateStatus(String invoiceNumberExist);


    //for bill with or without bill send to party
    @Query("select new com.main.glory.model.dispatch.response.GetBatchByInvoice(SUM(dd.batchEntryId) as batch,dd.batchId,dd.stockId) from DispatchData dd where dd.invoiceNo=:invoiceExist GROUP BY dd.batchId,dd.stockId")
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

    @Query("select new com.main.glory.model.dispatch.response.GetBatchByInvoice(SUM(d.batchEntryId) as batchEntryId,d.batchId as batchId,d.stockId as stockId) from DispatchData d where d.invoiceNo=:invoiceNumber GROUP BY d.batchId,d.stockId")
    List<GetBatchByInvoice> getAllStockByInvoiceNumber(String invoiceNumber);

    //get All Distapatch list
    //@Query("select new com.main.glory.model.dispatch.response.BatchListWithInvoice(COUNT(dd.batchEntryId) as batchEntryId,(dd.batchId) as batchId,(dd.stockId) as stockId,(dd.invoiceNo) as invoiceNo) from DispatchData dd where (:toDate IS NULL OR dd.createdDate <= :toDate AND :fromDate IS NULL OR dd.createdDate >= :fromDate) GROUP BY dd.batchId,dd.stockId,dd.invoiceNo")
    //List<BatchListWithInvoice> getAllDispatchList(Date toDate, Date fromDate);
}
