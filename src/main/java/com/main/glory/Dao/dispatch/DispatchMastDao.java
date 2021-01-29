package com.main.glory.Dao.dispatch;

import com.main.glory.model.dispatch.DispatchMast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DispatchMastDao extends JpaRepository<DispatchMast,Long> {
    @Query("select MAX(dm.postfix) from DispatchMast dm where prefix=:invoiceType")
    Long getInvoiceNumber(String invoiceType);

    @Query("select d from DispatchMast d")
    List<DispatchMast> getAllInvoiceList();

    @Modifying
    @Transactional
    @Query("delete from DispatchMast d where d.postfix=:substring")
    void deleteByInvoicePostFix(Long substring);

    @Query("select q from DispatchMast q where q.partyId=:partyId AND q.postfix!=0 AND q.paymentBunchId IS NULL")
    List<DispatchMast> getPendingBillByPartyId(Long partyId);

    @Query("select d from DispatchMast d where d.postfix=:substring")
    DispatchMast getDataByInvoiceNumber(Long substring);
}
