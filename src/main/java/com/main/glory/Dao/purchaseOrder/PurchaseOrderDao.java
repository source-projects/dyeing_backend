package com.main.glory.Dao.purchaseOrder;


import com.main.glory.model.purchaseOrder.PurchaseOrder;
import com.main.glory.model.purchaseOrder.ResponsePurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface PurchaseOrderDao extends JpaRepository<PurchaseOrder,Long> {

    @Query("select new com.main.glory.model.purchaseOrder.ResponsePurchase(p, (select u.userName as uName from UserData u where u.id = p.createdBy)) from PurchaseOrder p")
    List<ResponsePurchase> getAllWithUserName();
}
