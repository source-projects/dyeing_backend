package com.main.glory.Dao.paymentTerm;

import com.main.glory.Dao.FilterDao;
import com.main.glory.model.paymentTerm.PaymentMast;
import com.main.glory.model.paymentTerm.response.GetAllPayment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface PaymentMastDao extends FilterDao<PaymentMast> {

    @Query("select p from PaymentMast p where p.id = :paymentBunchId")
    PaymentMast findByPaymentBunchId(Long paymentBunchId);

    @Query("select l from PaymentMast l where l.party.id=:partyId")
    List<PaymentMast> findByPartyId(Long partyId);

    @Query("select new com.main.glory.model.paymentTerm.response.GetAllPayment(x,(select p.partyName from Party p where p.id=x.party.id)) from PaymentMast x")
    List<GetAllPayment> getAllPaymentWithPartyName();

    @Query("Select p from PaymentMast p where p.id = :id")
    Optional<PaymentMast> findByPayId(Long id);

    @Query("select x from PaymentMast x where x.id = :id")
    PaymentMast getPaymentMastById(Long id);

   /* @Query("select new com.main.glory.model.paymentTerm.request.GetAllBank() from")
    List<GetAllBank> getAllBankName();*/
}
