package com.main.glory.Dao.paymentTerm;

import com.main.glory.model.paymentTerm.PaymentData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface PaymentDataDao extends JpaRepository<PaymentData,Long> {
}
