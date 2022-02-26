package com.main.glory.Dao.paymentTerm;

import com.main.glory.model.paymentTerm.AdvancePayment;
import com.main.glory.model.paymentTerm.request.AdvancePaymentIdList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AdvancePaymentDao extends JpaRepository<AdvancePayment,Long> {

    @Query("select p from AdvancePayment p where p.partyId=:partyId AND (p.paymentBunchId IS NULL OR p.paymentBunchId=0)")
    List<AdvancePayment> findAdvancePaymentByPartyId(Long partyId);

    @Query("select DISTINCT x.bank from AdvancePayment x")
    List<String> getAllBankOfAdvancePaymentData();

    @Query("select new com.main.glory.model.paymentTerm.request.AdvancePaymentIdList(ap.id) from AdvancePayment ap where ap.paymentBunchId = :paymentBunchId AND ap.paymentBunchId IS NOT NULL")
    List<AdvancePaymentIdList> getAdvancePaymentByPaymentBunchId(Long paymentBunchId);

    @Query("select x.id from AdvancePayment x where x.paymentBunchId = :paymentBunchId AND x.paymentBunchId IS NOT NULL")
    List<Long> getAdvancePaymentIdListByPaymentBunchId(Long paymentBunchId);

    @Modifying
    @Transactional
    @Query("update AdvancePayment a set a.paymentBunchId = :paymentBunchId where a.id = :id")
    void updateAdvancePaymentBunchIdById(Long id, Long paymentBunchId);

    @Query("select x from AdvancePayment x where x.paymentBunchId=:id AND x.paymentBunchId IS NOT NULL")
    List<AdvancePayment> findExistingAdvancePaymentByPaymentBunchId(Long id);
}
