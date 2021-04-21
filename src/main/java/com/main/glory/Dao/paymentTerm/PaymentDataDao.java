package com.main.glory.Dao.paymentTerm;

import com.main.glory.model.paymentTerm.PaymentData;
import com.main.glory.model.paymentTerm.request.GetAllBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface PaymentDataDao extends JpaRepository<PaymentData,Long> {

    @Query("select DISTINCT x.bank from PaymentData x")
    List<String> getAllBankOfPaymentData();
}
