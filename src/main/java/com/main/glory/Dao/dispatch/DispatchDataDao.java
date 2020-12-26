package com.main.glory.Dao.dispatch;

import com.main.glory.model.dispatch.DispatchData;
import com.main.glory.model.dispatch.response.GetAllDispatch;
import com.main.glory.model.dispatch.response.GetBatchByInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DispatchDataDao extends JpaRepository<DispatchData, Long> {


    @Query("select new com.main.glory.model.dispatch.response.GetBatchByInvoice(SUM(dd.batchEntryId) as batch,dd.batchId,dd.stockId) from DispatchData dd where dd.invoiceNo=:invoiceNo AND isSendToParty = false GROUP BY dd.batchId,dd.stockId")
    List<GetBatchByInvoice> findBatchAndStockByInvoice(String invoiceNo);
}
