package com.main.glory.Dao.dispatch;

import com.main.glory.Dao.FilterDao;
import com.main.glory.model.dispatch.DispatchMast;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Primary
@Repository
public interface DispatchMastDao extends FilterDao<DispatchMast> {
    @Query("select MAX(dm.postfix) from DispatchMast dm where prefix=:invoiceType")
    Long getInvoiceNumber(String invoiceType);

    @Query("select d from DispatchMast d")
    List<DispatchMast> getAllInvoiceList();

    @Modifying
    @Transactional
    @Query("delete from DispatchMast d where d.postfix=:substring")
    void deleteByInvoicePostFix(String substring);

    @Query("select q from DispatchMast q where q.party.id=:partyId AND q.postfix!=0 AND q.paymentBunchId IS NULL")
    List<DispatchMast> getPendingBillByPartyId(Long partyId);

    @Query("select d from DispatchMast d where d.postfix=:substring")
    DispatchMast getDataByInvoiceNumber(String substring);

    // @Query("select d from DispatchMast d where
    // (d.partyId,d.partyId)=(:partyId,NULL) OR (:from IS NULL OR
    // d.createdDate>=:from) OR (:to IS NULL OR d.createdDate<=:to) OR (:userHeadId
    // IS NULL OR d.userHeadData.id=:userHeadId)")
    @Query("select d from DispatchMast d where (:from IS NULL OR d.createdDate>=:from) AND (:to IS NULL OR d.createdDate<=:to) AND (:partyId IS NULL OR d.party.id=:partyId) AND (:userHeadId IS NULL OR d.userHeadData.id=:userHeadId) ")
    List<DispatchMast> getInvoiceByFilter(Date from, Date to, Long partyId, Long userHeadId);

    @Query("select d from DispatchMast d where d.party.id=:id")
    List<DispatchMast> getDipatchByPartyId(Long id);

    @Query("select d from DispatchMast d where d.createdBy=:id OR d.userHeadData.id=:id1")
    List<DispatchMast> getDispatchByCreatedByAndUserHeadId(Long id, Long id1);

    @Query("select SUM(d.netAmt) from DispatchMast d where d.paymentBunchId IS NULL AND d.party.id=:partyId")
    Double getTotalPendingAmtByPartyId(Long partyId);

    @Query(value = "select * from dispatch_mast as x where x.party_id =:partyId AND x.payment_bunch_id IS NULL order by x.id DESC LIMIT 1", nativeQuery = true)
    DispatchMast getLastPendingDispatchByPartyId(@Param("partyId") Long partyId);

    @Query("select x from DispatchMast x where Date(x.createdDate)>=Date(:fromDate) AND Date(x.createdDate)<=Date(:toDate)")
    List<DispatchMast> getAllDispatchByDateFilter(Date fromDate, Date toDate);

    @Query("select d from DispatchMast d where Date(d.createdDate)>=Date(:from) AND Date(d.createdDate)<=Date(:to)")
    List<DispatchMast> getInvoiceByDateFilter(Date from, Date to);

    @Query("select x from DispatchMast x where x.postfix=:invoiceNo")
    DispatchMast getDispatchMastByInvoiceNo(String invoiceNo);

    @Query("select x from DispatchMast x where x.signByParty=:signByParty")
    List<DispatchMast> getAllInvoiceListBySignByParty(Boolean signByParty);

    @Query("select d from DispatchMast d where (Date(d.createdDate)>=Date(:from) OR :from IS NULL) AND (Date(d.createdDate)<=Date(:to) OR :to IS NULL) AND (d.userHeadData.id=:userHeadId OR :userHeadId IS NULL) AND (d.signByParty=:signByParty OR :signByParty IS NULL) AND (d.party.id=:partyId OR :partyId IS NULL) order by d.createdDate ASC")
    List<DispatchMast> getInvoiceByDispatchFilter(Date from, Date to, Long userHeadId, Long partyId, Boolean signByParty);

    @Query("select d from DispatchMast d where (Date(d.createdDate)>=Date(:from) OR :from IS NULL) AND (Date(d.createdDate)<=Date(:to) OR :to IS NULL) AND (d.userHeadData.id=:userHeadId OR :userHeadId IS NULL) AND (d.signByParty=:signByParty OR :signByParty IS NULL) AND (d.party.id=:partyId OR :partyId IS NULL) AND (d.party.id=:partyId OR :partyId IS NULL) AND (d.party.id=:partyId OR :partyId IS NULL) AND d.party IS NOT NULL AND d.userHeadData IS NOT NULL")
    List<DispatchMast> getInvoiceByFilter(Date from, Date to, Long userHeadId, Long partyId, Boolean signByParty);

    @Query("select d from DispatchMast d where (:from IS NULL OR Date(d.createdDate)>=Date(:from)) AND (:to IS NULL OR Date(d.createdDate)<=Date(:to)) AND d.paymentBunchId IS NOT NULL")
    List<DispatchMast> getInvoiceByDateFilterAndPaymentBunchIdNotNull(Date from, Date to);

    @Query("select d from DispatchMast d where (:from IS NULL OR Date(d.createdDate)>=Date(:from)) AND (:to IS NULL OR Date(d.createdDate)<=Date(:to)) AND d.paymentBunchId IS NULL")
    List<DispatchMast> getInvoiceByDateFilterAndPaymentBunchIdNull(Date from, Date to);

    @Query(value = "select * from dispatch_mast where postfix=:invoiceNo LIMIT 1",nativeQuery = true)
    DispatchMast getDispatchMastByInvoiceNumber(@Param("invoiceNo") String invoiceNo);

}
