package com.main.glory.Dao.dispatch;

import com.main.glory.model.dispatch.DispatchData;
import com.main.glory.model.dispatch.response.GetAllDispatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DispatchDataDao extends JpaRepository<DispatchData, Long> {


    //get All dispatchList
    @Query("select new com.main.glory.model.dispatch.response.GetAllDispatch(SUM(dd.stockId) as count,dd.invoiceNo,dd.isSendToParty,dd.createdDate) from DispatchData dd where invoiceNo IS NOT NULL GROUP BY  invoiceNo")
    List<GetAllDispatch> getAllDispatch();
}
