package com.main.glory.Dao.dispatch;

import com.main.glory.model.dispatch.DispatchMast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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


    //@Query("select d from DispatchMast d where (d.partyId,d.partyId)=(:partyId,NULL) OR (:from IS NULL OR d.createdDate>=:from) OR (:to IS NULL OR d.createdDate<=:to) OR (:userHeadId IS NULL OR d.userHeadId=:userHeadId)")
    @Query("select d from DispatchMast d where (:from IS NULL OR d.createdDate>=:from) AND (:to IS NULL OR d.createdDate<=:to) AND (:partyId IS NULL OR d.partyId=:partyId) AND (:userHeadId IS NULL OR d.userHeadId=:userHeadId) ")
    List<DispatchMast> getInvoiceByFilter(Date from, Date to, Long partyId, Long userHeadId);

    @Query("select d from DispatchMast d where d.partyId=:id")
    List<DispatchMast> getDipatchByPartyId(Long id);

    @Query("select d from DispatchMast d where d.createdBy=:id OR d.userHeadId=:id1")
    List<DispatchMast> getDispatchByCreatedByAndUserHeadId(Long id, Long id1);

    @Query("select SUM(d.netAmt) from DispatchMast d where d.paymentBunchId IS NULL AND d.partyId=:partyId")
    Double getTotalPendingAmtByPartyId(Long partyId);

    @Query(value = "select * from dispatch_mast as x where x.party_id =:partyId AND x.payment_bunch_id IS NULL order by x.id DESC LIMIT 1",nativeQuery = true)
    DispatchMast getLastPendingDispatchByPartyId(@Param("partyId") Long partyId);
}
