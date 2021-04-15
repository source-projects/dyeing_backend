package com.main.glory.Dao.paymentTerm;

import com.main.glory.model.PaymentMast;
import com.main.glory.model.paymentTerm.request.GetAllBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface PaymentMastDao extends JpaRepository<PaymentMast,Long> {
    @Query("select p from PaymentMast p where p.id=:paymentBunchId")
    PaymentMast findByPaymentBunchId(Long paymentBunchId);

    @Query("select l from PaymentMast l where l.partyId=:partyId")
    List<PaymentMast> findByPartyId(Long partyId);

   /* @Query("select new com.main.glory.model.paymentTerm.request.GetAllBank() from")
    List<GetAllBank> getAllBankName();*/
}
