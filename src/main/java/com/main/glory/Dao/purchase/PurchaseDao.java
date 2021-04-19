package com.main.glory.Dao.purchase;

import com.main.glory.model.purchase.Purchase;
import com.main.glory.model.purchase.response.PurchaseResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface PurchaseDao extends JpaRepository<Purchase,Long> {

    @Query("select p from Purchase p where p.id=:id")
    Purchase getPurchaseById(Long id);


    @Query("select new com.main.glory.model.purchase.response.PurchaseResponse(p,(select d.name from Department d where d.id=p.departmentId) as deptName,(select d.name from Authorize d where d.id=p.receiverById) as rname,(select d.name from ApprovedBy d where d.id=p.approvedById) as aname) from Purchase p")
    List<PurchaseResponse> getAllPurchaseRecord();

    @Query("select new com.main.glory.model.purchase.response.PurchaseResponse(p,(select d.name from Department d where d.id=p.departmentId) as deptName,(select d.name from Authorize d where d.id=p.receiverById) as rname,(select d.name from ApprovedBy d where d.id=p.approvedById) as aname) from Purchase p where p.id=:id")
    PurchaseResponse getPurchaseResponseById(Long id);

    @Query("select s from Purchase s where s.receiverById=:id")
    List<Purchase> getAllPurchaseByReceiverById(Long id);

    @Modifying
    @Transactional
    @Query("update Purchase p set p.checked=:flag where p.id=:id")
    void updateStatus(Long id, Boolean flag);

    @Query("select new com.main.glory.model.purchase.response.PurchaseResponse(p,(select d.name from Department d where d.id=p.departmentId) as deptName,(select d.name from Authorize d where d.id=p.receiverById) as rname,(select d.name from Authorize d where d.id=p.approvedById) as aname) from Purchase p where p.checked=:flag")
    List<PurchaseResponse> getAllPurchaseRecordBasedOnFlag(Boolean flag);

    @Modifying
    @Transactional
    @Query("delete from Purchase p where p.id=:id")
    void deleteByPurchaseId(Long id);
}
