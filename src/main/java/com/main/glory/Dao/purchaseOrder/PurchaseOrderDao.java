package com.main.glory.Dao.purchaseOrder;


import com.main.glory.model.purchaseOrder.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface PurchaseOrderDao extends JpaRepository<PurchaseOrder,Long> {
}
