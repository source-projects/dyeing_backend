package com.main.glory.Dao.dispatch;

import com.main.glory.model.dispatch.DispatchMast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DispatchMastDao extends JpaRepository<DispatchMast,Long> {
    @Query("select MAX(dm.postfix) from DispatchMast dm where prefix=:invoiceType")
    Long getInvoiceNumber(String invoiceType);
}
