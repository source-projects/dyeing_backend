package com.main.glory.Dao.paymentTerm;

import com.main.glory.model.paymentTerm.AdvancePayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdvancePaymentDao extends JpaRepository<AdvancePayment,Long> {

    @Query("select p from AdvancePayment p where p.partyId=:partyId AND (p.paymentBunchId IS NULL OR p.paymentBunchId=0)")
    List<AdvancePayment> findAdvancePaymentByPartyId(Long partyId);

    @Query("select DISTINCT x.bank from AdvancePayment x")
    List<String> getAllBankOfAdvancePaymentData();
}
