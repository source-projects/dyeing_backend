package com.main.glory.Dao.paymentTerm;

import com.main.glory.model.PaymentMast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface PaymentMastDao extends JpaRepository<PaymentMast,Long> {
    @Query("select p from PaymentMast p where p.id=:paymentBunchId")
    PaymentMast findByPaymentBunchId(Long paymentBunchId);
}
