package com.main.glory.Dao.paymentTerm;

import com.main.glory.model.PaymentMast;
import com.main.glory.model.paymentTerm.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
@EnableJpaRepositories
public interface PaymentTypeDao extends JpaRepository<PaymentType,Long> {
    @Query("select t from PaymentType t where t.id IS NOT NULL")
    List<PaymentType> getAllPaymentType();


    @Query("select t from PaymentType t where t.id=:payTypeId ")
    PaymentType getPaymentTypeById(Long payTypeId);

    @Query("select t from PaymentType t where LOWER(t.paymentType)=LOWER(:type) ")
    PaymentType getPaymentTypeByName(String type);
}
